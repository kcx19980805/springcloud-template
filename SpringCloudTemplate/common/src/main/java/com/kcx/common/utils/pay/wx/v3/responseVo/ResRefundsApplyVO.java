package com.kcx.common.utils.pay.wx.v3.responseVo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 申请退款
 */
@Data
public class ResRefundsApplyVO {
    /**
     * 微信退款单号
     */
    private String refundId;

    /**
     * 商户退款单号
     */
    private String outRefundNo;

    /**
     * 微信支付订单号
     */
    private String transactionId;

    /**
     * 商户订单号
     */
    private String outTradeNo;

    /**
     * 退款渠道
     * 枚举值：
     * ORIGINAL：原路退款
     * BALANCE：退回到余额
     * OTHER_BALANCE：原账户异常退到其他余额账户
     * OTHER_BANKCARD：原银行卡异常退到其他银行卡
     */
    private String channel;

    /**
     * 退款入账账户
     * 取当前退款单的退款入账方，有以下几种情况：
     * 1）退回银行卡：{银行名称}{卡类型}{卡尾号}
     * 2）退回支付用户零钱:支付用户零钱
     * 3）退还商户:商户基本账户商户结算银行账户
     * 4）退回支付用户零钱通:支付用户零钱通
     */
    private String userReceivedAccount;

    /**
     * 退款成功时间
     */
    private String successTime;

    /**
     * 退款创建时间
     */
    private String createTime;

    /**
     * 退款状态
     * 枚举值：
     * SUCCESS：退款成功
     * CLOSED：退款关闭
     * PROCESSING：退款处理中
     * ABNORMAL：退款异常
     */
    private String status;

    /**
     * 资金账户
     * 枚举值：
     * UNSETTLED : 未结算资金
     * AVAILABLE : 可用余额
     * UNAVAILABLE : 不可用余额
     * OPERATION : 运营户
     * BASIC : 基本账户（含可用余额和不可用余额）
     */
    private String fundsAccount;

    /**
     * 订单金额
     */
    private Amount amount;

    /**
     * 优惠退款详情
     */
    private PromotionDetail promotionDetail;


    @Data
    public static class Amount {

        /**
         * 原支付交易的订单总金额，单位为分，必填
         */
        @NotNull(message = "订单总金额不能为空")
        private Integer total;

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
         * 用户支付金额 必填
         */
        private Integer payerTotal;

        /**
         * 用户退款金额 必填
         */
        private Integer payerrefund;

        /**
         * 应结退款金额 必填
         */
        private Integer settlementRefund;

        /**
         * 应结订单金额 必填
         */
        private Integer settlementTotal;

        /**
         * 优惠退款金额 必填
         */
        private Integer discountRefund;

        /**
         * 货币类型,CNY：人民币，境内商户号仅支持人民币。必填
         */
        private String currency;

        /**
         * 手续费退款金额 必填
         */
        private Integer refundFee;

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
    public static class PromotionDetail{

        /**
         * 券ID
         */
        private String promotionId;
        /**
         * 优惠范围
         */
        private String scope;
        /**
         * 优惠类型
         */
        private String type;
        /**
         * 优惠券面额
         */
        private Integer amount;
        /**
         * 优惠退款金额
         */
        private Integer refundAmount;
        /**
         * 优惠商品发生退款时返回商品信息
         */
        private List<GoodsDetail> goodsDetail;
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
