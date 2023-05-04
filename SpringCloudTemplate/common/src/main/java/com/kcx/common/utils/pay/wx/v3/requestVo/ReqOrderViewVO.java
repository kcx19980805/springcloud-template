package com.kcx.common.utils.pay.wx.v3.requestVo;

import lombok.Data;

/**
 * 查看订单
 * https://pay.weixin.qq.com/wiki/doc/apiv3/apis/chapter3_4_2.shtml
 */
@Data
public class ReqOrderViewVO {

    /**
     * 不填，从配置中获取
     */
    private String mchid;

    /**
     * 商家订单号
     */
    private String outTradeNo;
}
