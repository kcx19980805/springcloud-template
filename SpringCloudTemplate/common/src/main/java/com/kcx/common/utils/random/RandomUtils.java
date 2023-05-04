package com.kcx.common.utils.random;

import java.util.Random;

/**
 * 随机数工具类
 */
public class RandomUtils {
    /**
     * 生成指定长度的随机整数
     */
    public static long randomNumLong(int len) {
        Random random = new Random();
        if (len > 9) {
            double d = Math.pow(10, 9 - 1);
            int i = random.nextInt((int) (Math.pow(10, 9) - d)) + (int) d;

            double d2 = Math.pow(10, len - 9 - 1);
            int i2 = random.nextInt((int) (Math.pow(10, len - 9) - d2)) + (int) d2;

            long l = (long) (i + i2 * Math.pow(10, 9));
            return l;
        } else {
            double d = Math.pow(10, len - 1);
            return random.nextInt((int) (Math.pow(10, len) - d)) + (int) d;
        }
    }


    /**
     * 获取随机字符串 a-z,A-Z,0-9
     * @param length 长度
     * @return
     */
    @SuppressWarnings("all")
    public static String randomString(int length) {
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; ++i) {
            int number = random.nextInt(62);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        System.out.println(randomNumLong(8));
        System.out.println(randomNumLong(20));
    }
}
