package com.kcx.common.utils.pay.wx.v3.responseVo;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 生成小程序调起支付的支付参数
 */
@Data
@Accessors(chain = true)
public class ResTuningPaymentVO {
    /**
     * appid
     */
    private String appid;
    /**
     * 时间戳
     */
    private String timeStamp;

    /**
     * 随机字符串
     */
    private String nonceStr;

    /**
     * 订单详情扩展字符串
     */
    private String packages;

    /**
     * 签名方式 仅支持RSA
     */
    private String signType;

    /**
     * 签名
     */
    private String paySign;

}
