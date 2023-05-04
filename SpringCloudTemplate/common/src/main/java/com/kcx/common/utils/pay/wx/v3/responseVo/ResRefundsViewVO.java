package com.kcx.common.utils.pay.wx.v3.responseVo;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 查看退款
 */
@Data
@Accessors(chain = true)
public class ResRefundsViewVO {
    /**
     * 微信退款单号
     */
    private String refundId;

    /**
     * 商户退款单号
     */
    private String outRefundNo;
    /**
     * 微信订单号
     */
    private String transactionId;
    /**
     * 商户订单号
     */
    private String outTradeNo;
    /**
     * 退款渠道
     */
    private String channel;
    /**
     * 退款入账账户
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
     * CLOSE：退款关闭
     * PROCESSING：退款处理中
     * ABNORMAL：退款异常，
     */
    private String status;

    /**
     * 金额信息
     */
    private ResRefundsApplyVO.Amount amount;

    /**
     * 优惠退款信息
     */
    private ResRefundsApplyVO.PromotionDetail promotionDetail;
}
