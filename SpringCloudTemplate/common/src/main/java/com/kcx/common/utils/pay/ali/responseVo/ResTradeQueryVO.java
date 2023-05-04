package com.kcx.common.utils.pay.ali.responseVo;

import lombok.Data;

import java.util.List;

@Data
public class ResTradeQueryVO {

    /**
     * 支付宝交易号 必填
     */
    private String tradeNo;

    /**
     * 商家订单号 必填
     */
    private String outTradeNo;

    /**
     * 买家支付宝账号 必填
     */
    private String buyerLogonId;

    /**
     * 交易状态：WAIT_BUYER_PAY（交易创建，等待买家付款）、
     * TRADE_CLOSED（未付款交易超时关闭，或支付完成后全额退款）、
     * TRADE_SUCCESS（交易支付成功）、
     * TRADE_FINISHED（交易结束，不可退款） 必填
     */
    private String tradeStatus;

    /**
     * 交易的订单金额，单位为元，两位小数。该参数的值为支付时传入的total_amount 必填
     */
    private String totalAmount;

    /**
     * 买家实付金额，单位为元，两位小数。该金额代表该笔交易买家实际支付的金额，不包含商户折扣等金额 选填
     */
    private String buyerPayAmount;

    /**
     * 积分支付的金额，单位为元，两位小数。该金额代表该笔交易中用户使用积分支付的金额，比如集分宝或者支付宝实时优惠等 选填
     */
    private String pointAmount;

    /**
     * 交易中用户支付的可开具发票的金额，单位为元，两位小数。该金额代表该笔交易中可以给用户开具发票的金额 选填
     */
    private String invoiceAmount;

    /**
     * 本次交易打款给卖家的时间	2014-11-27 15:45:57 选填
     */
    private String sendPayDate;

    /**
     * 实收金额，单位为元，两位小数。该金额为本笔交易，商户账户能够实际收到的金额 选填
     */
    private String receiptAmount;

    /**
     * 商户门店编号 选填
     */
    private String storeId;

    /**
     * 商户机具终端编号 选填
     */
    private String terminalId;

    /**
     * 商户门店编号 必填
     */
    private List<TradeFundBill> fundBillList;

    /**
     * 请求交易支付中的商户店铺的名称 选填
     */
    private String storeName;

    /**
     * 买家在支付宝的用户id 必填
     */
    private String buyerUserId;

    /**
     * 买家用户类型。CORPORATE:企业用户；PRIVATE:个人用户。 选填
     */
    private String buyerUserType;

    /**
     * 商家优惠金额 选填
     */
    private String mdiscountAmount;

    /**
     * 平台优惠金额 选填
     */
    private String discountAmount;

    /**
     * 交易额外信息，特殊场景下与支付宝约定返回。json格式。 选填
     */
    private String extInfos;


    @Data
    public static class TradeFundBill{
        /**
         * 交易使用的资金渠道，详见 支付渠道列表
         */
        private String fundChannel;
        /**
         * 该支付工具类型所使用的金额
         */
        private String amount;
        /**
         * 渠道实际付款金额
         */
        private String realAmount;
    }
}
