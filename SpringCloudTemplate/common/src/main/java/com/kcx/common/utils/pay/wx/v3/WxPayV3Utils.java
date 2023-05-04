package com.kcx.common.utils.pay.wx.v3;

import com.alibaba.fastjson.JSONObject;
import com.github.wxpay.sdk.WXPayUtil;
import com.kcx.common.exception.CustomException;
import com.kcx.common.utils.Result;
import com.kcx.common.utils.bean.BeanConvertUtils;
import com.kcx.common.utils.date.UnixUtils;
import com.kcx.common.utils.pay.wx.v2.WxV2CallBackFunction;
import com.kcx.common.utils.pay.wx.v3.requestVo.*;
import com.kcx.common.utils.pay.wx.v3.responseVo.ResRefundsApplyVO;
import com.kcx.common.utils.pay.wx.v3.responseVo.ResRefundsViewVO;
import com.kcx.common.utils.pay.wx.v3.responseVo.ResTuningPaymentVO;
import com.kcx.common.utils.random.RandomUtils;
import com.wechat.pay.contrib.apache.httpclient.auth.Verifier;
import com.wechat.pay.contrib.apache.httpclient.util.AesUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.util.Base64Utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.PrivateKey;
import java.security.Signature;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 商户平台微信支付V3工具类
 * 指引文档https://pay.weixin.qq.com/wiki/doc/apiv3/open/pay/chapter1_1_1.shtml
 * 开发文档https://pay.weixin.qq.com/wiki/doc/apiv3/apis/chapter3_5_4.shtml
 */
@Slf4j
public class WxPayV3Utils {


    /**
     * 微信小程序跳转支付-下单
     *
     * @param wxPayClient                   商户私钥并验签过的请求对象
     * @param reqMerchantTransactionsMiniVO 请求参数
     * @return prepay_id 预支付交易会话标识，然后再调用下面的tuningPayment
     */
    public Result<JSONObject> transactionsMini(CloseableHttpClient wxPayClient, ReqTransactionsMiniVO reqMerchantTransactionsMiniVO) {
        JSONObject json = BeanConvertUtils.lowercaseHumpToUnderline(reqMerchantTransactionsMiniVO);
        String result = executePost(wxPayClient, WxPayV3Constant.MERCHANT_TRANSACTIONS_JSAPI, json.toJSONString());
        JSONObject dataEntity = BeanConvertUtils.dataToEntity(result, JSONObject.class);
        if (StringUtils.isNotBlank(dataEntity.getString("code"))) {
            return Result.error(dataEntity.getString("message"));
        }
        return Result.success(dataEntity);
    }

    /**
     * 微信小程序跳转支付-小程序调起支付
     *
     * @param reqMerchantTuningPaymentVO 请求参数
     * @return 文档https://pay.weixin.qq.com/wiki/doc/apiv3/apis/chapter3_5_4.shtml，
     * 使用返回的参数在小程序调用wx.requestPayment(OBJECT)发起微信支付
     */
    public Result<ResTuningPaymentVO> tuningPayment(ReqTuningPaymentVO reqMerchantTuningPaymentVO) {
        // 当前时间戳
        String timestamp = String.valueOf(UnixUtils.currentDateFormatUnix());
        // 随机字符串
        String nonceStr = String.valueOf(RandomUtils.randomNumLong(15));
        // 订单详情扩展字段
        String packages = String.format("prepay_id=%s", reqMerchantTuningPaymentVO.getPrepayId());
        // 签名
        String paySign = paySign(reqMerchantTuningPaymentVO.getAppid(), timestamp, nonceStr, packages, reqMerchantTuningPaymentVO.getPrivateKey());
        ResTuningPaymentVO tuningPaymentVO = new ResTuningPaymentVO()
                .setAppid(reqMerchantTuningPaymentVO.getAppid())
                .setTimeStamp(timestamp)
                .setNonceStr(nonceStr)
                .setPackages(packages)
                .setSignType("RSA")
                .setPaySign(paySign);
        return Result.success(tuningPaymentVO);
    }

    /**
     * 生成支付二维码
     *
     * @param wxPayClient             商户私钥并验签过的请求对象
     * @param reqTransactionsNativeVO 请求参数
     * @return code_url 二维码链接
     */
    public Result<JSONObject> transactionsNative(CloseableHttpClient wxPayClient, ReqTransactionsNativeVO reqTransactionsNativeVO) {
        JSONObject json = BeanConvertUtils.lowercaseHumpToUnderline(reqTransactionsNativeVO);
        String result = executePost(wxPayClient, WxPayV3Constant.MERCHANT_TRANSACTIONS_NATIVE, json.toJSONString());
        JSONObject dataEntity = BeanConvertUtils.dataToEntity(result, JSONObject.class);
        if (StringUtils.isNotBlank(dataEntity.getString("code"))) {
            return Result.error(dataEntity.getString("message"));
        }
        return Result.success(dataEntity);
    }

    /**
     * 查看订单支付结果
     *
     * @param wxPayClient    商户私钥并验签过的请求对象
     * @param reqOrderViewVO 请求参数
     * @return 订单在微信侧当前的所有信息
     */
    public Result<JSONObject> orderView(CloseableHttpClient wxPayClient, ReqOrderViewVO reqOrderViewVO) {
        String url = String.format(WxPayV3Constant.MERCHANT_ORDER_VIEW, reqOrderViewVO.getOutTradeNo(), reqOrderViewVO.getMchid());
        String result = executeGet(wxPayClient, url);
        JSONObject dataEntity = BeanConvertUtils.dataToEntity(result, JSONObject.class);
        if (StringUtils.isNotBlank(dataEntity.getString("code"))) {
            return Result.error(dataEntity.getString("message"));
        }
        return Result.success(dataEntity);
    }

    /**
     * 关闭订单
     * 以下情况需要调用关单接口：
     * 1、商户订单支付失败需要生成新单号重新发起支付，要对原订单号调用关单，避免重复支付；
     * 2、系统下单后，用户支付超时，系统退出不再受理，避免用户继续，请调用关单接口。
     *
     * @param wxPayClient     商户私钥并验签过的请求对象
     * @param reqOrderCloseVO 请求参数
     * @return 无数据（Http状态码为204）
     */
    public Result orderClose(CloseableHttpClient wxPayClient, ReqOrderCloseVO reqOrderCloseVO) {
        String url = String.format(WxPayV3Constant.MERCHANT_ORDER_CLOSE, reqOrderCloseVO.getOutTradeNo());
        reqOrderCloseVO.setOutTradeNo(null);
        JSONObject json = BeanConvertUtils.lowercaseHumpToUnderline(reqOrderCloseVO);
        String result = executePost(wxPayClient, url, json.toJSONString());
        JSONObject dataEntity = BeanConvertUtils.dataToEntity(result, JSONObject.class);
        if (StringUtils.isNotBlank(dataEntity.getString("code"))) {
            return Result.error(dataEntity.getString("message"));
        }
        return Result.success(dataEntity);
    }

    /**
     * 申请退款
     *
     * @param wxPayClient         商户私钥并验签过的请求对象
     * @param reqWxRefundsApplyVO 请求参数
     * @return
     */
    public Result<ResRefundsApplyVO> refundsApply(CloseableHttpClient wxPayClient, ReqRefundsApplyVO reqWxRefundsApplyVO) {
        // 校验参数
        if (StringUtils.isBlank(reqWxRefundsApplyVO.getTransactionId()) && StringUtils.isBlank(reqWxRefundsApplyVO.getOutTradeNo())) {
            return Result.error("请输入微信订单号或商户订单号");
        }
        ReqRefundsApplyVO.Amount amount = reqWxRefundsApplyVO.getAmount();
        if (null == amount.getRefund()) {
            return Result.error("退款金额不能为空");
        }
        JSONObject json = BeanConvertUtils.lowercaseHumpToUnderline(reqWxRefundsApplyVO);
        String result = executePost(wxPayClient, WxPayV3Constant.MERCHANT_REFUNDS_APPLY, json.toJSONString());
        JSONObject dataEntity = BeanConvertUtils.dataToEntity(result, JSONObject.class);
        if (StringUtils.isNotBlank(dataEntity.getString("code"))) {
            return Result.error(dataEntity.getString("message"));
        }
        return Result.success(BeanConvertUtils.dataToEntity(dataEntity, ResRefundsApplyVO.class));
    }

    /**
     * 查看退款结果
     *
     * @param wxPayClient      商户私钥并验签过的请求对象
     * @param reqRefundsViewVO 请求参数
     * @return
     */
    public Result<ResRefundsViewVO> merchantRefundsView(CloseableHttpClient wxPayClient, ReqRefundsViewVO reqRefundsViewVO) {
        String outRefundNo = reqRefundsViewVO.getOutRefundNo();
        String url = String.format(WxPayV3Constant.MERCHANT_REFUNDS_VIEW, outRefundNo);
        String result = executeGet(wxPayClient, url);
        JSONObject dataEntity = BeanConvertUtils.dataToEntity(result, JSONObject.class);
        if (StringUtils.isNotBlank(dataEntity.getString("code"))) {
            return Result.error(dataEntity.getString("message"));
        }
        return Result.success(BeanConvertUtils.dataToEntity(dataEntity, ResRefundsViewVO.class));
    }

    /**
     * 申请账单，建议直接调用下载账单
     *
     * @param wxPayNoSignClient 商户私钥无需验签的请求对象
     * @param billDate          日期 格式yyyy-MM-dd 仅支持三个月内的账单下载申请
     * @param type              账单类型 tradebill交易账单 fundflowbill资金账单
     * @return 账单下载地址 URL有效期30s
     */
    public String queryBill(CloseableHttpClient wxPayNoSignClient, String billDate, String type) {
        String url = "";
        if ("tradebill".equals(type)) {
            url = WxPayV3Constant.TRADE_BILLS;
        } else if ("fundflowbill".equals(type)) {
            url = WxPayV3Constant.FUND_FLOW_BILLS;
        } else {
            throw new RuntimeException("不支持的账单类型");
        }
        url = url.concat("?bill_date=").concat(billDate);
        String result = executeGet(wxPayNoSignClient, url);
        JSONObject dataEntity = BeanConvertUtils.dataToEntity(result, JSONObject.class);
        return dataEntity.get("download_url").toString();
    }

    /**
     * 下载账单
     * 前端可以这样调用
     * const element = document.createElement('a')
     * element.setAttribute('href', 'data:application/vnd.ms-excel;charset=utf-8,' + encodeURIComponent(result.data))
     * element.setAttribute('download', this.billDate + '-' + type)
     * element.style.display = 'none'
     * element.click()
     *
     * @param wxPayNoSignClient 获取商户私钥无需验签的请求对象
     * @param billDate          日期 格式yyyy-MM-dd 仅支持三个月内的账单下载申请
     * @param type              账单类型
     * @return
     */
    public Result<String> downloadBill(CloseableHttpClient wxPayNoSignClient, String billDate, String type) {
        //获取账单url地址
        String downloadUrl = queryBill(wxPayNoSignClient, billDate, type);
        String result = executeGet(wxPayNoSignClient, downloadUrl);
        return Result.success(result);
    }

    /**
     * 微信支付V3-发送POST请求
     *
     * @param wxPayClient 构造的执行请求的客户端
     * @param url         微信方的接口地址
     * @param jsonString  json格式的请求参数
     * @return 返回数据
     */
    public String executePost(CloseableHttpClient wxPayClient, String url, String jsonString) {
        String body = null;
        HttpPost post = new HttpPost(url);
        CloseableHttpResponse response = null;
        // 设置超时时间
        RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(3 * 1000)
                .setSocketTimeout(3 * 1000)
                .setConnectTimeout(3 * 1000)
                .build();
        post.setConfig(requestConfig);
        post.addHeader("Content-Type", "application/json;charset=UTF-8");
        post.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/102.0.0.0 Safari/537.36");
        post.addHeader("Accept", "application/json");
        post.setEntity(new StringEntity(jsonString, "UTF-8"));
        try {
            response = wxPayClient.execute(post);
            body = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);//响应体
            int statusCode = response.getStatusLine().getStatusCode();//响应状态码
            if (statusCode != HttpServletResponse.SC_OK && statusCode != HttpServletResponse.SC_NO_CONTENT) {
                log.error("执行微信V3post请求响应错误，响应码：{}，返回结果{}", statusCode, body);
            }
            return body;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("执行微信V3post请求失败，原因：{}", e.getMessage());
        } finally {
            //主动释放连接
            post.releaseConnection();
        }
        return body;
    }

    /**
     * 微信支付V3-发送GET请求
     *
     * @param wxPayClient 构造的执行请求的客户端
     * @param url         微信方的接口地址,需要把参数拼接好
     * @return 返回数据
     */
    public String executeGet(CloseableHttpClient wxPayClient, String url) {
        String body = null;
        HttpGet get = new HttpGet(url);
        CloseableHttpResponse response = null;
        // 设置超时时间
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(3 * 1000)
                .setSocketTimeout(3 * 1000)
                .setConnectTimeout(3 * 1000).build();
        get.setConfig(requestConfig);
        get.addHeader("Content-Type", "application/json;charset=UTF-8");
        get.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/102.0.0.0 Safari/537.36");
        get.addHeader("Accept", "application/json");
        try {
            response = wxPayClient.execute(get);
            body = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
            int statusCode = response.getStatusLine().getStatusCode();//响应状态码
            if (statusCode != HttpServletResponse.SC_OK && statusCode != HttpServletResponse.SC_NO_CONTENT) {
                log.error("执行微信V3get请求响应错误，响应码：{}，返回结果{}", statusCode, body);
            }
            return body;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("执行微信V3get请求失败，原因：{}", e.getMessage());
        } finally {
            //主动释放连接
            get.releaseConnection();
        }
        return body;
    }

    /**
     * 微信支付签名.
     *
     * @param appid      小程序appId
     * @param timestamp  当前时间戳
     * @param nonceStr   随机字符串  要和TOKEN中的保持一致
     * @param packages   订单详情扩展字段
     * @param privateKey 商户API证书私钥
     * @return the string
     */
    @SneakyThrows
    public static String paySign(String appid, String timestamp, String nonceStr, String packages, PrivateKey privateKey) {
        String signatureStr = Stream.of(appid, timestamp, nonceStr, packages)
                .collect(Collectors.joining("\n", "", "\n"));
        Signature sign = Signature.getInstance("SHA256withRSA");
        sign.initSign(privateKey);
        sign.update(signatureStr.getBytes(StandardCharsets.UTF_8));
        return Base64Utils.encodeToString(sign.sign());
    }


    /**
     * 微信支付回调处理
     * @param request 网页请求对象
     * @param response 网页响应对象
     * @param wxPayVerifier 签名验证器
     * @param apiV3Key API V3密钥
     * @param reentrantLock 在整个处理回调方法外的一把锁
     * @param callback 执行具体的业务逻辑 ReqWxNotifyPayVO是微信传入的信息，具体业务逻辑使用ReentrantLock加锁，微信回调会非常频繁的发送，防止并发修改数据库
     * @return 返回给微信方信息
     */
    public static JSONObject payNotifyCallBack(HttpServletRequest request, HttpServletResponse response, Verifier wxPayVerifier, String apiV3Key, ReentrantLock reentrantLock, WxV3CallBackFunction<ReqWxNotifyPayVO> callback) {
        //处理通知参数
        String body = readData(request);
        Map<String, Object> bodyMap = BeanConvertUtils.dataToEntity(body, HashMap.class);
        String requestId = (String) bodyMap.get("id");
        //通知验签,证明是微信发送的回调而非其它程序
        try {
            WechatPay2Validator wechatPay2ValidatorForRequest = new WechatPay2Validator(wxPayVerifier, requestId, body);
            if (!wechatPay2ValidatorForRequest.validate(request)) {
                log.error("微信v3支付通知验签失败,参数：{}", body);
                return getReturnError(response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("微信v3支付通知验签异常：{},参数：{}", e.getMessage(), body);
            return getReturnError(response);
        }
        log.error("微信v3支付通知验签成功,参数：{}", body);
        //解密报文
        String text = decryptFromResource(bodyMap, apiV3Key);
        ReqWxNotifyPayVO wxNotifyPay = BeanConvertUtils.dataToEntity(text, ReqWxNotifyPayVO.class);
        //执行具体业务逻辑
        if (reentrantLock.tryLock()) {
            try {
                callback.apply(wxNotifyPay);
            } catch (Exception e) {
                e.printStackTrace();
                return getReturnError(response);
            } finally {
                //要主动释放锁
                reentrantLock.unlock();
            }
        }
        return getReturnSuccess(response);
    }

    /**
     * 微信退款回调处理
     * @param request 网页请求对象
     * @param response 网页响应对象
     * @param wxPayVerifier 签名验证器
     * @param apiV3Key API V3密钥
     * @param reentrantLock 在整个处理回调方法外的一把锁
     * @param callback 执行具体的业务逻辑 ReqWxNotifyRefundVO是微信传入的信息，具体业务逻辑使用ReentrantLock加锁，微信回调会非常频繁的发送，防止并发修改数据库
     * @return 返回给微信方信息
     */
    public static JSONObject refundNotifyCallBack(HttpServletRequest request, HttpServletResponse response, Verifier wxPayVerifier, String apiV3Key, ReentrantLock reentrantLock, WxV3CallBackFunction<ReqWxNotifyRefundVO> callback) {
        //处理通知参数
        String body = readData(request);
        Map<String, Object> bodyMap = BeanConvertUtils.dataToEntity(body, HashMap.class);
        String requestId = (String) bodyMap.get("id");
        //通知验签,证明是微信发送的回调而非其它程序
        try {
            WechatPay2Validator wechatPay2ValidatorForRequest = new WechatPay2Validator(wxPayVerifier, requestId, body);
            if (!wechatPay2ValidatorForRequest.validate(request)) {
                log.error("微信v3退款通知验签失败,参数：{}", body);
                return getReturnError(response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("微信v3退款通知验签异常：{},参数：{}", e.getMessage(), body);
            return getReturnError(response);
        }
        log.error("微信v3退款通知验签成功,参数：{}", body);
        //解密报文
        String text = decryptFromResource(bodyMap, apiV3Key);
        ReqWxNotifyRefundVO wxNotifyPay = BeanConvertUtils.dataToEntity(text, ReqWxNotifyRefundVO.class);
        //执行具体业务逻辑
        if (reentrantLock.tryLock()) {
            try {
                callback.apply(wxNotifyPay);
            } catch (Exception e) {
                e.printStackTrace();
                return getReturnError(response);
            } finally {
                //要主动释放锁
                reentrantLock.unlock();
            }
        }
        return getReturnSuccess(response);
    }

    /**
     * 微信通知处理成功消息应答
     *
     * @param response 设置此response状态码为200 代表成功
     */
    private static JSONObject getReturnSuccess(HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_OK);
        return null;
    }

    /**
     * 微信通知处理失败消息应答
     *
     * @param response 设置此response状态码为5xx/4xx 代表失败
     */
    private static JSONObject getReturnError(HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        JSONObject wxPayReturn = new JSONObject();
        wxPayReturn.put("code", "FALL");
        wxPayReturn.put("message", "失败");
        return wxPayReturn;
    }

    /**
     * 对称解密
     *
     * @param bodyMap  请求参数
     * @param apiV3Key API V3密钥
     * @return
     */
    public static String decryptFromResource(Map<String, Object> bodyMap, String apiV3Key) {
        //通知数据
        Map<String, String> resourceMap = (Map) bodyMap.get("resource");
        //数据密文
        String ciphertext = resourceMap.get("ciphertext");
        //随机串
        String nonce = resourceMap.get("nonce");
        //附加数据
        String associatedData = resourceMap.get("associated_data");
        log.info("微信通知密文解密,密文 ===> {}", ciphertext);
        AesUtil aesUtil = new AesUtil(apiV3Key.getBytes(StandardCharsets.UTF_8));
        String plainText = null;
        try {
            plainText = aesUtil.decryptToString(associatedData.getBytes(StandardCharsets.UTF_8),
                    nonce.getBytes(StandardCharsets.UTF_8),
                    ciphertext);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
            log.error("微信通知密文解密,失败，原因:{}", e.getMessage());
        }
        log.info("微信通知密文解密,明文 ===> {}", plainText);
        return plainText;
    }

    /**
     * 将通知参数转化为字符串
     *
     * @param request
     * @return
     */
    public static String readData(HttpServletRequest request) {
        BufferedReader br = null;
        try {
            StringBuilder result = new StringBuilder();
            br = request.getReader();
            for (String line; (line = br.readLine()) != null; ) {
                if (result.length() > 0) {
                    result.append("\n");
                }
                result.append(line);
            }
            return result.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
