package com.kcx.common.utils.pay.ali.responseVo;

import lombok.Data;

/**
 * 订单取消
 */
@Data
public class ResTradeCancelVO {

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

    /**
     * 是否需要重试 示例值 N
     */
    private String retryFlag;

    /**
     * 本次撤销触发的交易动作,接口调用成功且交易存在时返回。可能的返回值：
     * close：交易未支付，触发关闭交易动作，无退款；
     * refund：交易已支付，触发交易退款动作；
     * 未返回：未查询到交易，或接口调用失败；
     */
    private String action;
}
