package com.kcx.common.utils.pay.wx.v3.requestVo;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 小程序下单
 * <a href="https://pay.weixin.qq.com/wiki/doc/apiv3/apis/chapter3_1_1.shtml">小程序下单</a>
 */
@Data
public class ReqTransactionsMiniVO {
    /**
     * 应用ID，必填
     */
    private String appid;
    /**
     * 直连商户号，必填
     */
    private String mchid;
    /**
     * 商品描述，必填
     */
    @NotNull(message = "商品描述不能为空")
    private String description;
    /**
     * 商户订单号，必填
     */
    @NotNull(message = "商户订单号不能为空")
    private String outTradeNo;
    /**
     * 交易结束时间
     */
    private String timeExpire;
    /**
     * 附加数据
     */
    private String attach;
    /**
     * 通知地址，必填
     */
    @NotNull(message = "通知地址不能为空")
    private String notifyUrl;
    /**
     * 订单优惠标记
     */
    private String goodsTag;
    /**
     * 电子发票入口开放标识,传入true时，支付成功消息和支付详情页将出现开票入口。需要在微信支付商户平台或微信公众平台开通电子发票功能，传此字段才可生效。
     */
    private Boolean supportFapiao;
    /**
     * 订单金额，必填
     */
    @NotNull(message = "订单金额不能为空")
    @Valid
    private Amount amount;
    /**
     * 支付者，必填
     */
    @NotNull(message = "支付者不能为空")
    private Payer payer;
    /**
     * 优惠功能
     */
    private Detail detail;
    /**
     * 场景信息
     */
    private SceneInfo sceneInfo;

    /**
     * 结算信息
     */
    private SettleInfo settleInfo;


    @Data
    public static class Amount {

        /**
         * 总金额
         */
        @NotNull(message = "订单总金额不能为空")
        private Integer total;

        /**
         * 货币类型,CNY：人民币，境内商户号仅支持人民币。
         */
        private String currency;

    }

    @Data
    public static class Payer {
        /**
         * 用户在直连商户appid下的唯一标识。 下单前需获取到用户的Openid，必填
         */
        private String openid;
    }

    @Data
    public static class Detail {
        /**
         * 订单原价
         */
        private Integer costPrice;

        /**
         * 商品小票ID
         */
        private String invoiceId;

        @Valid
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
         * 商品数量
         */
        @NotNull(message = "商品数量不能为空")
        private String quantity;
        /**
         * 商品单价
         */
        @NotNull(message = "商品单价不能为空")
        private String unitPrice;
    }

    @Data
    public static class SceneInfo {
        /**
         * 用户终端IP
         */
        private String payerClientIp;
        /**
         * 商户端设备号
         */
        private String deviceId;

        @Valid
        private StoreInfo storeInfo;

    }

    @Data
    public static class StoreInfo {

        /**
         * 门店编号
         */
        @NotNull(message = "门店编号不能为空")
        private String id;

        /**
         * 门店名称
         */
        private String name;
        /**
         * 地区编码
         */
        private String areaCode;
        /**
         * 详细地址
         */
        private String address;

    }

    @Data
    public static class SettleInfo {

        /**
         * 是否指定分账
         */
        private Boolean profitSharing;
    }
}
