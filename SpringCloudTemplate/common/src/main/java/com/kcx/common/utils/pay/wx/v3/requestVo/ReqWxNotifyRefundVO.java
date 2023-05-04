package com.kcx.common.utils.pay.wx.v3.requestVo;

import lombok.Data;

/**
 * 微信退款通知
 */
@Data
public class ReqWxNotifyRefundVO {
    /**
     * 直连商户号
     */
    private String mchid;

    /**
     * 商户订单号
     */
    private String outTradeNo;

    /**
     * 微信支付订单号
     */
    private String transactionId;

    /**
     * 商户退款单号
     */
    private String outRefundNo;

    /**
     * 微信退款单号
     */
    private String refundId;

    /**
     * 退款状态
     * 枚举值：
     * SUCCESS：退款成功
     * CLOSED：退款关闭
     * ABNORMAL：退款异常，退款到银行发现
     */
    private String refundStatus;

    /**
     * 退款成功时间
     */
    private String successTime;

    /**
     * 退款入账账户
     */
    private String 	userReceivedAccount;

    /**
     * 金额信息
     */
    private Amount amount;

    @Data
    public static class Amount {

        /**
         * 订单金额
         */
        private Integer total;

        /**
         * 退款金额
         */
        private Integer refund;

        /**
         * 用户支付金额
         */
        private Integer payerTotal;

        /**
         * 用户退款金额
         */
        private String payerRefund;
    }
}
