package com.kcx.common.utils.pay.wx.v3.requestVo;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 申请退款
 * https://pay.weixin.qq.com/wiki/doc/apiv3/apis/chapter3_4_9.shtml
 */
@Data
public class ReqRefundsApplyVO {

    /**
     * 微信订单号,与outTradeNo二选一
     */
    private String transactionId;

    /**
     * 商户订单号
     */
    private String outTradeNo;

    @NotNull(message = "商户退款单号不能为空")
    private String outRefundNo;

    @NotNull(message = "退款原因不能为空")
    private String reason;

    /**
     * 退款结果回调url不能为空
     */
    private String notifyUrl;

    /**
     * 退款资金来源,非必填
     */
    private String refundAccount;

    @Valid
    @NotNull(message = "退款金额不能为空")
    private Amount amount;

    /**
     * 退款商品,非必填
     */
    private List<GoodsDetail> goodsDetail;

    @Data
    public static class Amount {

        /**
         * 退款使用 退款金额,单位为分，必填
         */
        private Integer refund;

        /**
         * 退款需要从指定账户出资时，传递此参数指定出资金额（币种的最小单位，只能为整数）。
         * 同时指定多个账户出资退款的使用场景需要满足以下条件：
         *   1、未开通退款支出分离产品功能；
         *   2、订单属于分账订单，且分账处于待分账或分账中状态。
         * 参数传递需要满足条件：
         *   1、基本账户可用余额出资金额与基本账户不可用余额出资金额之和等于退款金额；
         *   2、账户类型不能重复。
         * 上述任一条件不满足将返回错误
         */
        private List<From> from;

        /**
         * 原支付交易的订单总金额，单位为分，必填
         */
        @NotNull(message = "订单总金额不能为空")
        private Integer total;

        /**
         * 货币类型,CNY：人民币，境内商户号仅支持人民币。必填
         */
        private String currency;

    }

    @Data
    public static class From {
        /**
         * 出资账户类型
         * 枚举值：
         * AVAILABLE : 可用余额
         * UNAVAILABLE : 不可用余额
         */
        private String account;

        /**
         * 对应账户出资金额
         */
        private Integer amount;
    }

    @Data
    public static class GoodsDetail {
        /**
         * 商户侧商品编码
         */
        @NotNull(message = "商户侧商品编码不能为空")
        private String merchantGoodsId;
        /**
         * 微信支付商品编码
         */
        private String wechatpayGoodsId;
        /**
         * 商品名称
         */
        private String goodsName;
        /**
         * 商品单价
         */
        @NotNull(message = "商品单价不能为空")
        private Integer unitPrice;
        /**
         * 商品退款金额
         */
        @NotNull(message = "商品退款金额不能为空")
        private Integer refundAmount;
        /**
         * 商品退货数量
         */
        @NotNull(message = "商品退货数量不能为空")
        private Integer refundQuantity;

    }
}
