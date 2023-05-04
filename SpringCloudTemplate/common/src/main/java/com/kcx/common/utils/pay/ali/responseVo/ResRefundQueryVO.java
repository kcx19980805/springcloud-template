package com.kcx.common.utils.pay.ali.responseVo;

import lombok.Data;

import java.util.List;

/**
 * 查询订单退款结果
 */
@Data
public class ResRefundQueryVO {
    /**
     * 支付宝交易号。选填
     */
    private String tradeNo;

    /**
     * 商户订单号。选填
     */
    private String outTradeNo;

    /**
     * 本笔退款对应的退款请求号 选填
     */
    private String outRequestNo;

    /**
     * 该笔退款所对应的交易的订单金额 选填
     */
    private String totalAmount;

    /**
     * 本次退款请求，对应的退款金额 选填
     */
    private String refundAmount;

    /**
     * 退款状态。枚举值：
     * REFUND_SUCCESS 退款处理成功；
     * 未返回该字段表示退款请求未收到或者退款失败；
     * 注：如果退款查询发起时间早于退款时间，或者间隔退款发起时间太短，可能出现退款查询时还没处理成功，后面又处理成功的情况，建议商户在退款发起后间隔10秒以上再发起退款查询请求。
     * 选填
     */
    private String refundStatus;

    /**
     * 退分账明细信息	 选填
     */
    private List<RefundRoyaltys> refundRoyaltys;

    /**
     * 退款时间。默认不返回该信息，需要在入参的query_options中指定"gmt_refund_pay"值时才返回该字段信息。 选填
     */
    private String gmtRefundPay;

    /**
     * 本次退款使用的资金渠道；
     * 默认不返回该信息，需要在入参的query_options中指定"refund_detail_item_list"值时才返回该字段信息。
     */
    private List<RefundDetailItemList> refundDetailItemList;

    /**
     * 本次商户实际退回金额；
     * 默认不返回该信息，需要在入参的query_options中指定"refund_detail_item_list"值时才返回该字段信息	 选填
     */
    private String sendBackFee;

    /**
     * 银行卡冲退信息；
     * 默认不返回该信息，需要在入参的query_options中指定"deposit_back_info"值时才返回该字段信息。 选填
     */
    private DepositBackInfo depositBackInfo;

    /**
     * 本次请求退惠营宝金额	 选填
     */
    private String refundHybAmount;

    @Data
    public static class RefundRoyaltys{
        /**
         * 退分账金额 必填
         */
        private String refundAmount;
        /**
         * 分账类型.
         * 普通分账为：transfer;
         * 补差为：replenish;
         * 为空默认为分账transfer; 选填
         */
        private String royaltyType;
        /**
         * 退分账结果码 选填
         */
        private String resultCode;
        /**
         * 转出人支付宝账号对应用户ID 选填
         */
        private String transOut;
        /**
         * 转出人支付宝账号 选填
         */
        private String transOutEmail;
        /**
         * 转入人支付宝账号对应用户ID 选填
         */
        private String transIn;
        /**
         * 转入人支付宝账号 选填
         */
        private String transInEmail;
    }

    @Data
    public static class DepositBackInfo{
        /**
         * 是否存在银行卡冲退信息。选填
         */
        private String hasDepositBack;
        /**
         * 银行卡冲退状态。S-成功，F-失败，P-处理中。银行卡冲退失败，资金自动转入用户支付宝余额。。选填
         */
        private String dbackStatus;
        /**
         * 银行卡冲退金额	。选填
         */
        private String dbackAmount;
        /**
         * 银行响应时间，格式为yyyy-MM-dd HH:mm:ss。选填
         */
        private String bankAckTime;
        /**
         * 预估银行到账时间，格式为yyyy-MM-dd HH:mm:ss选填
         */
        private String estBankReceiptTime;
        /**
         * 是否包含因公付资产 选填
         */
        private Boolean isUseEnterprisePay;
    }

    @Data
    public static class RefundDetailItemList{
        /**
         * 交易使用的资金渠道，详见 支付渠道列表 必填
         */
        private String fundChannel;
        /**
         * 该支付工具类型所使用的金额 必填
         */
        private String amount;
        /**
         * 渠道实际付款金额 选填
         */
        private String realAmount;
        /**
         * 渠道所使用的资金类型,目前只在资金渠道(fund_channel)是银行卡渠道(BANKCARD)的情况下才返回该
         * 信息(DEBIT_CARD:借记卡,CREDIT_CARD:信用卡,MIXED_CARD:借贷合一卡) 选填
         */
        private String fund_type;
    }
}
