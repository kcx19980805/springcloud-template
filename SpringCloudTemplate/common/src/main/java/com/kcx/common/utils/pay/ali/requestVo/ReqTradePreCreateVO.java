package com.kcx.common.utils.pay.ali.requestVo;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 生成支付二维码
 * https://opendocs.alipay.com/open/02ekfg?ref=api&scene=19
 */
@Data
public class ReqTradePreCreateVO {
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
     * 卖家支付宝用户 ID。 如果该值为空，则默认为商户签约账号对应的支付宝用户 ID。不允许收款账号与付款方账号相同 选填
     */
    private String sellerId;
    /**
     * 订单附加信息。如果请求时传递了该参数，将在异步通知、对账单中原样返回，同时会在商户和用户的pc账单详情中作为交易描述展示 选填
     */
    private String body;
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
     * 商户门店编号。指商户创建门店时输入的门店编号。 选填
     */
    private String storeId;
    /**
     * 商户操作员编号。 选填
     */
    private String operatorId;
    /**
     * 商户机具终端编号。 选填
     */
    private String terminalId;
    /**
     * 	商户原始订单号，最大长度限制 32 位 选填
     */
    private String merchantOrderNo;

    @Data
    public static class GoodsDetail{
        /**
         * 商品的编号	必填
         */
        private String goodsId;
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
         * 卡类型 选填
         */
        private String cardType;
        /**
         * 特殊场景下，允许商户指定交易展示的卖家名称
         */
        private String specifiedSellerName;

    }

}
