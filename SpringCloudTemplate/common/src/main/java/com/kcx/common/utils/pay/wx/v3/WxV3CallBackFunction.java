package com.kcx.common.utils.pay.wx.v3;

/**
 * 处理微信V3通知回调的业务处理
 */
@FunctionalInterface
public interface WxV3CallBackFunction<T> {
    /**
     * 处理回调的具体业务逻辑,请实现幂等性：无论接口被调用多少次，产生的结果是一致的。
     * @param req 微信方发过来的参数
     */
    void apply(T req);
}
