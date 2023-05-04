package com.kcx.common.utils.pay.ali.requestVo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 网页支付
 * https://opendocs.alipay.com/open/028r8t?scene=22
 */
@Data
public class ReqPagePayVO {

    /**
     * 服务器同步通知页面路径
     */
    @NotNull(message = "服务器同步通知页面路径不能为空")
    private String returnUrl;

    /**
     * 服务器异步通知页面路径
     */
    @NotNull(message = "服务器异步通知页面路径不能为空")
    private String notifyUrl;

    /**
     * 	商户订单号。由商家自定义，64个字符以内，仅支持字母、数字、下划线且需保证在商户端不重复。必填
     */
    private String outTradeNo;
    /**
     * 订单总金额，单位为元，精确到小数点后两位，取值范围为 [0.01,100000000]，金额不能为 0。如果同时传入了【可打折金额】，
     * 【不可打折金额】，【订单总金额】三者，则必须满足如下条件：【订单总金额】=【可打折金额】+【不可打折金额】。必填
     */
    private String totalAmount;
    /**
     * 订单标题。注意：不可使用特殊字符，如 /，=，& 等。必填
     */
    private String subject;
    /**
     * 销售产品码。如果签约的是当面付快捷版，则传 OFFLINE_PAYMENT；其它支付宝当面付产品传 FACE_TO_FACE_PAYMENT；
     * 不传则默认使用 FACE_TO_FACE_PAYMENT。选填
     */
    private String productCode;

    /**
     * PC扫码支付的方式。
     * 支持前置模式和跳转模式。
     * 前置模式是将二维码前置到商户的订单确认页的模式。需要商户在自己的页面中以 iframe 方式请求支付宝页面。具体支持的枚举值有以下几种：
     * 0：订单码-简约前置模式，对应 iframe 宽度不能小于600px，高度不能小于300px；
     * 1：订单码-前置模式，对应iframe 宽度不能小于 300px，高度不能小于600px；
     * 3：订单码-迷你前置模式，对应 iframe 宽度不能小于 75px，高度不能小于75px；
     * 4：订单码-可定义宽度的嵌入式二维码，商户可根据需要设定二维码的大小。
     *
     * 跳转模式下，用户的扫码界面是由支付宝生成的，不在商户的域名下。支持传入的枚举值有：
     * 2：订单码-跳转模式 选填
     */
    private String qrPayMode;
    /**
     * 商户自定义二维码宽度。
     * 注：qr_pay_mode=4时该参数有效
     */
    private String qrcodeWidth;
    /**
     * 订单包含的商品列表信息，为 JSON 格式，其它说明详见商品明细说明 选填
     */
    private List<GoodsDetail> goodsDetail;
    /**
     * 业务扩展参数 选填
     */
    private ExtendParams extendParams;
    /**
     * 可打折金额。参与优惠计算的金额，单位为元，精确到小数点后两位，取值范围为 [0.01,100000000]。
     * 如果该值未传入，但传入了【订单总金额】和【不可打折金额】，则该值默认为【订单总金额】-【不可打折金额】 选填
     */
    private String discountableAmount;
    /**
     * 商户传入业务信息，具体值要和支付宝约定，应用于安全，营销等参数直传场景，格式为json格式 选填
     */
    private String businessParams;
    /**
     * 优惠参数。为 JSON 格式。注：仅与支付宝协商后可用 选填
     */
    private String promoParams;
    /**
     * 请求后页面的集成方式。
     * 枚举值：
     * ALIAPP：支付宝钱包内
     * PCWEB：PC端访问
     * 默认值为PCWEB。选填
     */
    private String integrationType;
    /**
     * 请求来源地址。如果使用ALIAPP的集成方式，用户中途取消支付会返回该地址。。 选填
     */
    private String requestFromUrl;
    /**
     * 商户门店编号。
     * 指商户创建门店时输入的门店编号。。 选填
     */
    private String storeId;
    /**
     * 	商户原始订单号，最大长度限制 32 位 选填
     */
    private String merchantOrderNo;

    /**
     * 	开票信息	 选填
     */
    private InvoiceInfo invoiceInfo;

    @Data
    public static class GoodsDetail{
        /**
         * 商品的编号	必填
         */
        private String goodsId;
        /**
         * 支付宝定义的统一商品编号 选填
         */
        private String alipayGoodsId;
        /**
         * 商品名称 必填
         */
        private String goodsName;
        /**
         * 商品数量 必填
         */
        private Integer quantity;
        /**
         * 商品单价，单位为元 必填
         */
        private String price;
        /**
         * 商品类目 选填
         */
        private String goodsCategory;
        /**
         * 商品类目树，从商品类目根节点到叶子节点的类目id组成，类目id值使用|分割	 选填
         */
        private String categoriesTree;
        /**
         * 商品的展示地址	选填
         */
        private String showUrl;
    }

    @Data
    public static class ExtendParams{
        /**
         * 系统商编号该参数作为系统商返佣数据提取的依据，请填写系统商签约协议的PID 选填
         */
        private String sysServiceProviderId;
        /**
         * 使用花呗分期要进行的分期数 选填
         */
        private String hbFqNum;
        /**
         * 使用花呗分期需要卖家承担的手续费比例的百分值，传入100代表100% 选填
         */
        private String hbFqSellerPercent;
        /**
         * 行业数据回流信息, 详见：地铁支付接口参数补充说明 选填
         */
        private String industryRefluxInfo;
        /**
         * 卡类型 选填
         */
        private String cardType;
        /**
         * 特殊场景下，允许商户指定交易展示的卖家名称
         */
        private String specifiedSellerName;

    }

    @Data
    public static class InvoiceInfo{
        /**
         * 开票关键信息
         */
        private KeyInfo keyInfo;
        /**
         * 开票内容
         * 注：json数组格式
         */
        private String details;
    }

    @Data
    public static class KeyInfo{
        /**
         * 该交易是否支持开票
         */
        private Boolean isSupportInvoice;
        /**
         * 开票商户名称：商户品牌简称|商户门店简称
         */
        private String invoiceMerchantName;
        /**
         * 税号
         */
        private String taxNum;
    }
}
