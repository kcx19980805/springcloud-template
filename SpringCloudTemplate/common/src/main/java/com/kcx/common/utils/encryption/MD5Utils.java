package com.kcx.common.utils.encryption;

import java.security.MessageDigest;

/**
 * md5不可逆加密，不可直接解密，只能通过穷举解密
 */
public final class MD5Utils {
    //小写加密
    private static final char hexDigits[] = {'0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    /**
     * MD5加密字符串
     * 32位
     */
    public static String getMD5String(String str) {
        try {
            return getMD5String(str.getBytes("utf-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * MD5加密以byte
     */
    public static String getMD5String(byte[] bytes) {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
            md5.update(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return toHex(md5.digest());
    }

    private static String toHex(byte[] bytes) {
        StringBuffer str = new StringBuffer(32);
        for (byte b : bytes) {
            str.append(hexDigits[(b & 0xf0) >> 4]);
            str.append(hexDigits[(b & 0x0f)]);
        }
        return str.toString();
    }
    public static void main(String[] args) {
        System.out.println(getMD5String("123456"));
    }
}
