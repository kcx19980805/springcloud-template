package com.kcx.common.utils.pay.wx.v2;

import java.util.Map;

/**
 * 处理微信V2通知回调的业务处理
 * @param <R>
 */
@FunctionalInterface
public interface WxV2CallBackFunction<R> {

     /**
      * 处理回调的具体业务逻辑,请实现幂等性：无论接口被调用多少次，产生的结果是一致的。
      * @param parseResultMap 解析出来的返回参数，包括商户侧订单号out_trade_no，微信侧订单号transaction_id，自定义参数attach，
      *                       订单支付金额attach等
      * @return 商户侧保存的订单金额，单位分
      */
     R apply(Map<String, String> parseResultMap);
}
