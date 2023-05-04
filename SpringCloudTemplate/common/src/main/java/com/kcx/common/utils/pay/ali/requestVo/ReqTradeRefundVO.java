package com.kcx.common.utils.pay.ali.requestVo;

import lombok.Data;

import java.util.List;

/**
 * 订单退款
 */
@Data
public class ReqTradeRefundVO {

    /**
     * 商户订单号。
     * 订单支付时传入的商户订单号，商家自定义且保证商家系统中唯一。与支付宝交易号 trade_no 不能同时为空。
     */
    private String outTradeNo;

    /**
     * 支付宝交易号。
     * 和商户订单号 out_trade_no 不能同时为空。
     */
    private String tradeNo;

    /**
     * 退款金额。
     * 需要退款的金额，该金额不能大于订单金额，单位为元，支持两位小数。
     * 注：如果正向交易使用了营销，该退款金额包含营销金额，支付宝会按业务规则分配营销和买家自有资金分别退多少，默认优先退买家的自有资金。
     * 如交易总金额100元，用户支付时使用了80元自有资金和20元无资金流的营销券，商家实际收款80元。
     * 如果首次请求退款60元，则60元全部从商家收款资金扣除退回给用户自有资产；如果再请求退款40元，
     * 则从商家收款资金扣除20元退回用户资产以及把20元的营销券退回给用户（券是否可再使用取决于券的规则配置）。 必填
     */
    private String refundAmount;

    /**
     * 退款原因说明。
     * 商家自定义，将在会在商户和用户的pc退款账单详情中展示 选填
     */
    private String refundReason;

    /**
     * 退款请求号。
     * 标识一次退款请求，需要保证在交易号下唯一，如需部分退款，则此参数必传。
     * 注：针对同一次退款请求，如果调用接口失败或异常了，重试时需要保证退款请求号不能变更，防止该笔交易重复退款。支付宝会保证同样的退款请求号多次请求只会退一次。选填
     */
    private String outRequestNo;

    /**
     * 退分账明细信息。
     * 注： 1.当面付且非直付通模式无需传入退分账明细，系统自动按退款金额与订单金额的比率，从收款方和分账收入方退款，不支持指定退款金额与退款方。
     * 2.直付通模式，电脑网站支付，手机 APP 支付，手机网站支付产品，须在退款请求中明确是否退分账，从哪个分账收入方退，退多少分账金额；
     * 如不明确，默认从收款方退款，收款方余额不足退款失败。不支持系统按比率退款。 选填
     */
    private List<RefundRoyaltyParameters> refundRoyaltyParameters;

    /**
     * 查询选项。选填
     * 商户通过上送该参数来定制同步需要额外返回的信息字段，数组格式。支持：refund_detail_item_list：退款使用的资金渠道；deposit_back_info：触发银行卡冲退信息通知；
     */
    private List<String> queryOptions;


    @Data
    public static class RefundRoyaltyParameters{
        /**
         * 分账类型.
         * 普通分账为：transfer;
         * 补差为：replenish;
         * 为空默认为分账transfer;
         */
        private String royaltyType;

        /**
         * 支出方账户。如果支出方账户类型为userId，本参数为支出方的支付宝账号对应的支付宝唯一用户号，
         * 以2088开头的纯16位数字；如果支出方类型为loginName，本参数为支出方的支付宝登录号。
         * 泛金融类商户分账时，该字段不要上送。
         */
        private String transOut;

        /**
         * 支出方账户类型。userId表示是支付宝账号对应的支付宝唯一用户号;loginName表示是支付宝登录号； 泛金融类商户分账时，该字段不要上送
         */
        private String transOutType;

        /**
         * 收入方账户类型。userId表示是支付宝账号对应的支付宝唯一用户号;cardAliasNo表示是卡编号;loginName表示是支付宝登录号；
         */
        private String transInType;

        /**
         * 收入方账户。如果收入方账户类型为userId，本参数为收入方的支付宝账号对应的支付宝唯一用户号，以2088开头的纯16位数字；
         * 如果收入方类型为cardAliasNo，本参数为收入方在支付宝绑定的卡编号；如果收入方类型为loginName，
         * 本参数为收入方的支付宝登录号；
         */
        private String transIn;

        /**
         * 分账的金额，单位为元
         */
        private String amount;

        /**
         * 分账描述
         */
        private String desc;

        /**
         * 可选值：达人佣金、平台服务费、技术服务费、其他
         */
        private String royaltyScene;

        /**
         * 分账收款方姓名，上送则进行姓名与支付宝账号的一致性校验，校验不一致则分账失败。不上送则不进行姓名校验
         */
        private String transInName;
    }

}
