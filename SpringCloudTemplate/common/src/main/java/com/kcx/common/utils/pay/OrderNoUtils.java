package com.kcx.common.utils.pay;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * 订单号工具类
 */
public class OrderNoUtils {

    /**
     * 生成订单编号
     * @return
     */
    public static String getOrderNo(String prefix) {
        return prefix+"ORDER_" + getNo();
    }

    /**
     * 生成退款单编号
     * @return
     */
    public static String getRefundNo(String prefix) {
        return prefix+"REFUND_" + getNo();
    }

    /**
     * 获取编号
     * @return
     */
    public static String getNo() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String newDate = sdf.format(new Date());
        String result = "";
        Random random = new Random();
        for (int i = 0; i < 4; i++) {
            result += random.nextInt(10);
        }
        return newDate + result;
    }
}
