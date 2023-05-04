package com.kcx.common.utils.pay.ali.requestVo;

import lombok.Data;

/**
 * 取消订单
 */
@Data
public class ReqTradeCancelVO {

    /**
     * 商户订单号。
     * 订单支付时传入的商户订单号，商家自定义且保证商家系统中唯一。与支付宝交易号 trade_no 不能同时为空。
     */
    private String outTradeNo;

    /**
     * 支付宝交易号。
     * 和商户订单号 out_trade_no 不能同时为空。
     */
    private String tradeNo;
}
