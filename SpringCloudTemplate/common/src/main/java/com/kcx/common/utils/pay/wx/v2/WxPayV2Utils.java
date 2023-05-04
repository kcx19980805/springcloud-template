package com.kcx.common.utils.pay.wx.v2;

import com.alibaba.fastjson.JSONObject;
import com.kcx.common.exception.CustomException;
import com.github.wxpay.sdk.WXPayUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 商户平台微信支付V2工具类
 */
@Slf4j
public class WxPayV2Utils {

    /**
     * 微信支付-native-生成支付二维码，需要前端或后端把字符串转换为二维码图片
     *
     * @param request
     * @param notifyUrl    接收微信回调通知的接口地址
     * @param appid        微信应用的APPID
     * @param mchId        商户号
     * @param description  描述
     * @param out_trade_no 订单号
     * @param total        总金额，单位为分
     * @param attach       自定义参数
     * @param partnerKey   APIv2密钥
     * @return 支付二维码
     */
    public String transactionsNativeV2(HttpServletRequest request, String notifyUrl, String appid, String mchId, String description, String out_trade_no, String total, JSONObject attach, String partnerKey) throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        //组装接口参数
        Map<String, String> params = new HashMap<>();
        params.put("appid", appid);//关联的公众号的appid
        params.put("mch_id", mchId);//商户号
        params.put("nonce_str", WXPayUtil.generateNonceStr());//生成随机字符串
        params.put("body", description);
        params.put("out_trade_no", out_trade_no);
        //注意，这里必须使用字符串类型的参数（总金额：分）
        params.put("total_fee", total);
        params.put("spbill_create_ip", request.getRemoteAddr());
        params.put("notify_url", notifyUrl);
        params.put("trade_type", "NATIVE");
        //自定义参数
        params.put("attach", attach.toJSONString());
        //将参数转换成xml字符串格式：生成带有签名的xml格式字符串
        String xmlParams = WXPayUtil.generateSignedXml(params, partnerKey);
        log.info("微信支付V2请求参数：" + xmlParams);
        String resultXml = restTemplate.postForObject(WxPayV2Constants.NATIVE_PAY_V2, xmlParams, String.class);
        log.info("微信支付V2返回参数：" + resultXml);
        //将xml响应结果转成map对象
        Map<String, String> resultMap = WXPayUtil.xmlToMap(resultXml);
        //错误处理
        if ("FAIL".equals(resultMap.get("return_code")) || "FAIL".equals(resultMap.get("result_code"))) {
            log.error("微信native生成二维码失败,结果==>" + resultXml);
            throw new CustomException("微信native生成二维码失败");
        }
        //二维码
        return resultMap.get("code_url");
    }

    /**
     * 微信回调t通知处理，必须将只含有code，message的map转为WXPayUtil.mapToXml的字符串返回给微信
     * 遇到异常请回滚事务
     *
     * @param request       网页请求对象
     * @param partnerKey    APIv2密钥
     * @param reentrantLock 在整个处理回调方法外的一把锁
     * @param callback      执行具体的业务逻辑 Long返回数据库订单总金额 单位为分，String商户订单号，String微信订单号，
     *                      具体业务逻辑使用ReentrantLock加锁，微信回调会非常频繁的发送，防止并发修改数据库
     * @return 返回给微信方一个字符串
     */
    public static String notifyV2CallBack(HttpServletRequest request, String partnerKey, ReentrantLock reentrantLock, WxV2CallBackFunction<Long> callback) {
        //应答对象
        Map<String, String> returnMap = new HashMap<>();
        //处理通知参数
        String body = readData(request);
        //通知验签,证明是微信发送的回调而非其它程序
        try {
            if (!WXPayUtil.isSignatureValid(body, partnerKey)) {
                log.error("微信v2通知验签失败,参数：{}", body);
                //失败应答
                returnMap.put("return_code", "FAIL");
                returnMap.put("return_msg", "验签失败");
                return WXPayUtil.mapToXml(returnMap);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("微信v2通知验签异常：{},参数：{}", e.getMessage(), body);
            //失败应答
            returnMap.put("return_code", "FAIL");
            returnMap.put("return_msg", "验签异常");
            try {
                return WXPayUtil.mapToXml(returnMap);
            } catch (Exception exception) {
                //忽略
            }
        }
        log.error("微信v2通知验签成功,参数：{}", body);
        //解析xml数据
        Map<String, String> parseMap = null;
        try {
            parseMap = WXPayUtil.xmlToMap(body);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("微信v2解析参数异常：{},参数：{}", e.getMessage(), body);
        }
        //判断通信和业务是否成功
        if (!"SUCCESS".equals(parseMap.get("return_code")) || !"SUCCESS".equals(parseMap.get("result_code"))) {
            log.error("微信v2回调业务失败,参数：{}", body);
            //失败应答
            returnMap.put("return_code", "FAIL");
            returnMap.put("return_msg", "失败");
            try {
                return WXPayUtil.mapToXml(returnMap);
            } catch (Exception exception) {
                //忽略
            }
        }
        //执行具体业务逻辑
        if (reentrantLock.tryLock()) {
            try {
                Long total = callback.apply(parseMap);
                //校验返回的订单金额是否与商户侧的订单金额是否一致
                if (total != Long.parseLong(parseMap.get("total_fee"))) {
                    log.error("微信v2支付通知金额校验失败,商户侧金额：{},微信侧金额：{}", total, parseMap.get("total_fee"));
                    throw new CustomException("支付订单金额校验失败");
                }
            } finally {
                //要主动释放锁
                reentrantLock.unlock();
            }
        }
        returnMap.put("return_code", "SUCCESS");
        returnMap.put("return_msg", "OK");
        try {
            return WXPayUtil.mapToXml(returnMap);
        } catch (Exception exception) {
            //忽略
        }
        return null;
    }

    /**
     * 将通知参数转化为字符串
     *
     * @param request
     * @return
     */
    private static String readData(HttpServletRequest request) {
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

/*    public static void main(String[] args) {
        ReentrantLock reentrantLock = new ReentrantLock();
        notifyV2CallBack(null, null,reentrantLock,new WxV2CallBackFunction<Long>(){
            @Override
            public Long apply(Map<String, String> parseResultMap) {
                return 0L;
            }
        });
    }*/
}
