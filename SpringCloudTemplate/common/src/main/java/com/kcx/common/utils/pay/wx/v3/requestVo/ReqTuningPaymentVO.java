package com.kcx.common.utils.pay.wx.v3.requestVo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.security.PrivateKey;

/**
 * 生成小程序调起支付的支付参数
 */
@Data
public class ReqTuningPaymentVO {

    @NotNull(message = "prepayId预支付交易会话标识不能为空")
    private String prepayId;

    @NotNull(message = "小程序appid不能为空")
    private String appid;

    @NotNull(message = "私钥不能为空")
    PrivateKey privateKey;
}
