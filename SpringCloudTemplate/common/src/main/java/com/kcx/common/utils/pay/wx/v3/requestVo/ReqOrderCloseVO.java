package com.kcx.common.utils.pay.wx.v3.requestVo;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 关闭订单
 */
@Data
public class ReqOrderCloseVO {

    @NotBlank(message = "商户号不能为空")
    private String mchid;

    @NotBlank(message = "商户订单号不能为空")
    private String outTradeNo;
}
