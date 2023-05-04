package com.kcx.common.utils.pay.wx.v3;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 微信支付常量
 */
@Component
@SuppressWarnings("all")
public class WxPayV3Constant {
    /*----------------------------------------------商户API----------------------------------------------*/
    /**
     * 小程序下单 POST
     */
    public static String MERCHANT_TRANSACTIONS_JSAPI = "https://api.mch.weixin.qq.com/v3/pay/transactions/jsapi";
    /**
     * native下单 POST
     */
    public static String MERCHANT_TRANSACTIONS_NATIVE = "https://api.mch.weixin.qq.com/v3/pay/transactions/native";
    /**
     * 查询订单 GET
     */
    public static String MERCHANT_ORDER_VIEW = "https://api.mch.weixin.qq.com/v3/pay/transactions/out-trade-no/%s?mchid=%s";
    /**
     * 关闭订单 POST
     */
    public static String MERCHANT_ORDER_CLOSE = "https://api.mch.weixin.qq.com/v3/pay/transactions/out-trade-no/%s/close";
    /**
     * 退款 POST
     */
    public static String MERCHANT_REFUNDS_APPLY= "https://api.mch.weixin.qq.com/v3/refund/domestic/refunds";
    /**
     * 查询退款  GET
     */
    public static String MERCHANT_REFUNDS_VIEW= "https://api.mch.weixin.qq.com/v3/refund/domestic/refunds/%s";
    /**
     * 申请交易账单  GET
     */
    public static final String TRADE_BILLS="https://api.mch.weixin.qq.com/v3/bill/tradebill";

    /**
     * 申请资金账单  GET
     */
    public static final String FUND_FLOW_BILLS="https://api.mch.weixin.qq.com/v3/bill/fundflowbill";
}

