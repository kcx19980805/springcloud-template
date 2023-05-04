package com.kcx.common.utils.pay.wx.v3.requestVo;

import lombok.Data;

/**
 * 微信支付通知
 */
@Data
public class ReqWxNotifyPayVO {
    /**
     * 直连商户应用ID
     */
    private String appid;

    /**
     * 商户号
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
     * 交易类型交易类型，
     * 枚举值：
     * JSAPI：公众号支付
     * NATIVE：扫码支付
     * APP：APP支付
     * MICROPAY：付款码支付
     * MWEB：H5支付
     * FACEPAY：刷脸支付
     */
    private String tradeType;

    /**
     * 交易状态
     * 枚举值：
     * SUCCESS：支付成功
     * REFUND：转入退款
     * NOTPAY：未支付
     * CLOSED：已关闭
     * REVOKED：已撤销（付款码支付）
     * USERPAYING：用户支付中（付款码支付）
     * PAYERROR：支付失败(其他原因，如银行返回失败)
     */
    private String tradeState;
    /**
     * 交易状态描述
     */
    private String tradeStateDesc;
    /**
     * 付款银行
     */
    private String bankType;
    /**
     * 附加数据，在查询API和支付通知中原样返回
     */
    private String attach;
    /**
     * 支付完成时间,示例值：2018-06-08T10:34:56+08:00
     */
    private String successTime;

    /**
     * 支付者信息
     */
    private ReqTransactionsMiniVO.Payer payer;

    /**
     * 订单金额
     */
    private Amount amount;

    /**
     * 支付场景信息描述
     */
    private SceneInfo sceneInfo;

    /**
     * 优惠功能
     */
    private PromotionDetail promotionDetail;

    @Data
    public static class SceneInfo {
        /**
         * 商户端设备号
         */
        private String deviceId;
    }

    @Data
    public static class Amount {

        /**
         * 总金额
         */
        private Integer total;

        /**
         * 用户支付金额
         */
        private Integer payerTotal;

        /**
         * 货币类型
         */
        private String currency;

        /**
         * 用户支付币种
         */
        private String payerCurrency;
    }

    @Data
    public static class PromotionDetail {
        /**
         * 券ID
         */
        private String couponId;
        /**
         * 优惠名称
         */
        private String name;
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
         * 活动ID
         */
        private String stockId;
        /**
         * 微信出资
         */
        private Integer wechatpayContribute;
        /**
         * 商户出资
         */
        private Integer merchantContribute;
        /**
         * 其他出资
         */
        private Integer otherContribute;
        /**
         * 优惠币种
         */
        private String currency;
        /**
         * 单品列表信息
         */
        private GoodsDetail goodsDetail;
    }

    @Data
    public static class GoodsDetail {
        /**
         * 商品编码
         */
        private String goodsId;

        /**
         * 商品数量
         */
        private Integer quantity;
        /**
         * 商品单价
         */
        private Integer unitPrice;

        /**
         * 商品优惠金额
         */
        private Integer discountAmount;

        /**
         * 商品备注
         */
        private String goodsRemark;
    }
}
