package com.kcx.common.utils.pay.ali.responseVo;

import lombok.Data;

/**
 * 生成支付二维码
 * https://opendocs.alipay.com/open/02ekfg?ref=api&scene=19
 */
@Data
public class ResTradePreCreateVO {
    /**
     * 商户的订单号
     */
    private String outTradeNo;
    /**
     * 当前预下单请求生成的二维码码串，有效时间2小时，可以用二维码生成工具根据该码串值生成对应的二维码
     */
    private String qrCode;
}
