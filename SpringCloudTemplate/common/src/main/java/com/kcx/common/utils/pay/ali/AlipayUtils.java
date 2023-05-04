package com.kcx.common.utils.pay.ali;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.AlipayConstants;
import com.alipay.api.domain.*;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.*;
import com.alipay.api.response.*;
import com.google.gson.internal.LinkedTreeMap;
import com.kcx.common.utils.Result;
import com.kcx.common.utils.bean.BeanConvertUtils;
import com.kcx.common.utils.pay.ali.requestVo.*;
import com.kcx.common.utils.pay.ali.responseVo.*;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;

/**
 * 支付宝支付api工具类
 */
@Slf4j
public class AlipayUtils {

    /**
     * 电脑网站跳转支付页面支付
     * 前端调用如下
     * const divForm = document.getElementsByTagName("div");
     * if (divForm.length) {
     *  document.body.removeChild(divForm[0]);
     * }
     * const div = document.createElement("div");
     * div.innerHTML = res; // res就是接口返回的form 表单字符串
     * document.body.appendChild(div);
     * document.forms[0].setAttribute("target", "_self");
     * document.forms[0].submit();
     * @param alipayClient 构造的执行请求的客户端
     * @param reqPagePayVO 请求参数
     * @return
     */
    public static Result<String> pagePay(AlipayClient alipayClient, ReqPagePayVO reqPagePayVO) {
        AlipayTradePagePayModel model = BeanConvertUtils.dataToEntity(reqPagePayVO,AlipayTradePagePayModel.class);
        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
        request.setReturnUrl(reqPagePayVO.getReturnUrl());
        request.setNotifyUrl(reqPagePayVO.getNotifyUrl());
        request.setBizModel(model);
        try {
            AlipayTradePagePayResponse response = alipayClient.execute(request);
            if(response.isSuccess()){
                return Result.success(response.getBody());
            }else{
                return Result.error(response.getSubMsg());
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
            return Result.error(e.getErrMsg());
        }
    }

    /**
     * 扫码支付-生成支付二维码
     * @param alipayClient 构造的执行请求的客户端
     * @param reqTradePreCreateVO 请求参数
     * @return
     */
    public static Result<ResTradePreCreateVO> tradePreCreate(AlipayClient alipayClient, ReqTradePreCreateVO reqTradePreCreateVO) {
        AlipayTradePrecreateModel model = BeanConvertUtils.dataToEntity(reqTradePreCreateVO,AlipayTradePrecreateModel.class);
        AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();
        request.setNotifyUrl(reqTradePreCreateVO.getNotifyUrl());
        request.setBizModel(model);
        try {
            AlipayTradePrecreateResponse response = alipayClient.execute(request);
            if(response.isSuccess()){
                ResTradePreCreateVO resTradePreCreateBO = BeanConvertUtils.dataToEntity(response,ResTradePreCreateVO.class);
                return Result.success(resTradePreCreateBO);
            }else{
                return Result.error(response.getSubMsg());
            }

        } catch (AlipayApiException e) {
            e.printStackTrace();
            return Result.error(e.getErrMsg());
        }
    }

    /**
     * 查询订单
     * @param alipayClient 构造的执行请求的客户端
     * @param reqTradeQueryBO 请求参数
     * @return
     */
    public static Result<ResTradeQueryVO> tradeQuery(AlipayClient alipayClient, ReqTradeQueryVO reqTradeQueryBO) {
        AlipayTradeQueryModel model = BeanConvertUtils.dataToEntity(reqTradeQueryBO,AlipayTradeQueryModel.class);
        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
        request.setBizModel(model);
        try {
            AlipayTradeQueryResponse response = alipayClient.execute(request);
            if(response.isSuccess()){
                ResTradeQueryVO resTradeQueryVOr = BeanConvertUtils.dataToEntity(response,ResTradeQueryVO.class);
                return Result.success(resTradeQueryVOr);
            }else{
                return Result.error(response.getSubMsg());
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
            return Result.error(e.getErrMsg());
        }
    }

    /**
     * 退款
     * @param alipayClient 构造的执行请求的客户端
     * @param reqTradeRefundVO 请求参数
     * @return
     */
    public static Result<ResTradeRefundVO> tradeRefund(AlipayClient alipayClient, ReqTradeRefundVO reqTradeRefundVO) {
        AlipayTradeRefundModel model = BeanConvertUtils.dataToEntity(reqTradeRefundVO,AlipayTradeRefundModel.class);
        AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
        request.setBizModel(model);
        try {
            AlipayTradeRefundResponse response = alipayClient.execute(request);
            if(response.isSuccess()){
                ResTradeRefundVO resTradeRefundBO = BeanConvertUtils.dataToEntity(response,ResTradeRefundVO.class);
                return Result.success(resTradeRefundBO);
            }else{
                return Result.error(response.getSubMsg());
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
            return Result.error(e.getErrMsg());
        }
    }

    /**
     * 查询退款
     * @param alipayClient 构造的执行请求的客户端
     * @param reqRefundQueryVO 请求参数
     * @return
     */
    public static Result<ResRefundQueryVO> refundQuery(AlipayClient alipayClient, ReqRefundQueryVO reqRefundQueryVO) {
        AlipayTradeFastpayRefundQueryModel model = BeanConvertUtils.dataToEntity(reqRefundQueryVO,AlipayTradeFastpayRefundQueryModel.class);
        AlipayTradeFastpayRefundQueryRequest request = new AlipayTradeFastpayRefundQueryRequest();
        request.setBizModel(model);
        try {
            AlipayTradeFastpayRefundQueryResponse response = alipayClient.execute(request);
            if(response.isSuccess()){
                ResRefundQueryVO resRefundQueryBO = BeanConvertUtils.dataToEntity(response,ResRefundQueryVO.class);
                return Result.success(resRefundQueryBO);
            }else{
                return Result.error(response.getSubMsg());
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
            return Result.error(e.getErrMsg());
        }
    }

    /**
     * 扫码支付-取消订单
     * 只有发生支付系统超时或者支付结果未知时可调用撤销，其他正常支付的单如需实现相同功能请调用申请退款API。
     * 提交支付交易后调用【查询订单API】，没有明确的支付结果再调用【撤销订单API】。
     * @return
     */
    public static Result tradeCancel(AlipayClient alipayClient, ReqTradeCancelVO reqTradeCancelVO) {
        AlipayTradeCancelModel model = BeanConvertUtils.dataToEntity(reqTradeCancelVO,AlipayTradeCancelModel.class);
        AlipayTradeCancelRequest request = new AlipayTradeCancelRequest();
        request.setBizModel(model);
        try {
            AlipayTradeCancelResponse response = alipayClient.execute(request);
            if(response.isSuccess()){
                ResTradeCancelVO resTradeCancelBO = BeanConvertUtils.dataToEntity(response,ResTradeCancelVO.class);
                return Result.success(resTradeCancelBO);
            }else{
                return Result.error(response.getSubMsg());
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
            return Result.error(e.getErrMsg());
        }
    }


    /**
     * 支付宝支付-申请当前账户的账单
     * @param alipayClient 构造的执行请求的客户端
     * @param billDate 日期 日账单格式为yyyy-MM-dd,月账单格式为yyyy-MM
     * @param type 账单类型 trade：商户基于支付宝交易收单的业务账单；signcustomer：基于商户支付宝余额收入及支出等资金变动的账务账单。
     * @return
     */
    public static Result<String> queryBill(AlipayClient alipayClient,String billDate, String type){
        AlipayDataDataserviceBillDownloadurlQueryRequest request = new AlipayDataDataserviceBillDownloadurlQueryRequest();
        AlipayDataDataserviceBillDownloadurlQueryModel model = new AlipayDataDataserviceBillDownloadurlQueryModel();
        model.setBillDate(type);
        model.setBillType(billDate);
        request.setBizModel(model);
        try {
            AlipayDataDataserviceBillDownloadurlQueryResponse response = alipayClient.execute(request);
            if(response.isSuccess()){
                //获取账单下载地址
                HashMap<String, LinkedTreeMap> map = JSONObject.parseObject(response.getBody(), HashMap.class);
                LinkedTreeMap billDownloadurlResponse = map.get("alipay_data_dataservice_bill_downloadurl_query_response");
                String billDownloadUrl = (String)billDownloadurlResponse.get("bill_download_url");
                return Result.success(billDownloadUrl);
            } else {
                return Result.error(response.getSubMsg());
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
            return Result.error(e.getErrMsg());
        }
    }

    /**
     * 支付宝异步的支付通知，注意支付宝没有退款通知
     * @param alipayClientBaseConfig 构造的执行请求的客户端
     * @param params 从回调接口接收的参数，包含out_trade_no，trade_no，total_amount等，trade_status=TRADE_SUCCESS表示支付成功
     * @param reentrantLock 在整个处理回调方法外的一把锁
     * @param callback 需要执行的具体的业务逻辑，返回值为null就行，具体业务逻辑使用ReentrantLock加锁，微信回调会非常频繁的发送，防止并发修改数据库
     * @return 返回给支付宝方一个字符串
     */
    public static String payNotifyCallBack(AlipayBaseConfig alipayClientBaseConfig, Map<String, String> params, ReentrantLock reentrantLock, Supplier<String> callback){
        //通知验签,证明是支付宝发送的回调而非其它程序
        try {
            boolean signVerified = AlipaySignature.rsaCheckV1(params,alipayClientBaseConfig.alipayPublicKey, AlipayConstants.CHARSET_UTF8,AlipayConstants.SIGN_TYPE_RSA2);
            if(!signVerified){
                log.error("支付宝通知验签失败,参数：{}",params.toString());
                return "failure";
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
            log.error("支付宝通知验签异常：{},参数：{}",e.getErrMsg(),params.toString());
            return "failure";
        }
        log.error("支付宝通知验签成功,参数：{}",params.toString());
        //执行具体业务逻辑
        if(reentrantLock.tryLock()){
            try {
                callback.get();
            }catch (Exception e){
                e.printStackTrace();
                return "failure";
            } finally {
                //要主动释放锁
                reentrantLock.unlock();
            }
        }
        return "success";
    }

/*    public static void main(String[] args) {
        ReentrantLock reentrantLock = new ReentrantLock();
        payNotifyCallBack(null, null, reentrantLock, new Supplier<String>() {
            @Override
            public String get() {
                return null;
            }
        });
    }*/
}
