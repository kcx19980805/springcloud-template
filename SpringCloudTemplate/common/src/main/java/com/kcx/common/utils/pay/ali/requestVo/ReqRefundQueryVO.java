package com.kcx.common.utils.pay.ali.requestVo;

import lombok.Data;

import java.util.List;

/**
 * 查询订单退款结果
 */
@Data
public class ReqRefundQueryVO {

    /**
     * 订单支付时传入的商户订单号,和支付宝交易号不能同时为空。trade_no,out_trade_no如果同时存在优先取trade_no
     */
    private String outTradeNo;

    /**
     * 支付宝交易号，和商户订单号不能同时为空
     */
    private String tradeNo;

    /**
     * 退款请求号。
     * 请求退款接口时，传入的退款请求号，如果在退款请求时未传入，则该值为创建交易时的商户订单号。 必填
     */
    private String outRequestNo;

    /**
     * 查询选项，商户通过上送该参数来定制同步需要额外返回的信息字段，数组格式。支持枚举如下：
     * trade_settle_info：返回的交易结算信息，包含分账、补差等信息；
     * fund_bill_list：交易支付使用的资金渠道；
     * voucher_detail_list：交易支付时使用的所有优惠券信息；
     * discount_goods_detail：交易支付所使用的单品券优惠的商品优惠信息；
     * mdiscount_amount：商家优惠金额 选填
     */
    private List<String> queryOptions;
}
