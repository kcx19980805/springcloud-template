package com.kcx.common.utils.pay.wx.v3.requestVo;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 查看退款
 * https://pay.weixin.qq.com/wiki/doc/apiv3/apis/chapter3_4_10.shtml
 */
@Data
public class ReqRefundsViewVO {
    /**
     * 商户退款单号
     */
    @NotNull(message = "退款单号不能为空")
    private String outRefundNo;
}
