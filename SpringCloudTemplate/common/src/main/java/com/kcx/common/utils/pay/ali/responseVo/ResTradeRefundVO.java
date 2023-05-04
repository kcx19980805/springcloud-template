package com.kcx.common.utils.pay.ali.responseVo;

import lombok.Data;

import java.util.List;

/**
 * 订单退款
 */
@Data
public class ResTradeRefundVO {

    /**
     * 支付宝交易号。必填
     */
    private String tradeNo;

    /**
     * 商户订单号。必填
     */
    private String outTradeNo;

    /**
     * 用户的登录id。 必填
     */
    private String buyerLogonId;

    /**
     * 本次退款是否发生了资金变化 必填
     */
    private String fundChange;

    /**
     * 退款总金额。
     * 指该笔交易累计已经退款成功的金额。必填
     */
    private String refundFee;

    /**
     * 退款使用的资金渠道。只有在签约中指定需要返回资金明细，或者入参的query_options中指定时才返回该字段信息。。选填
     */
    private List<RefundDetailItemList> refundDetailItemList;

    /**
     * 交易在支付时候的门店名称	 选填
     */
    private String storeName;

    /**
     * 买家在支付宝的用户id 选填
     */
    private String buyerUserId;

    /**
     * 本次商户实际退回金额。
     * 说明：如需获取该值，需在入参query_options中传入 refund_detail_item_list 选填
     */
    private String sendBackFee;

    /**
     * 本次请求退惠营宝金额	 选填
     */
    private String refundHybAmount;

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
