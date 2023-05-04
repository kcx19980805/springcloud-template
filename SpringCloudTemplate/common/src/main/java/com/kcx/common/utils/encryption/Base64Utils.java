package com.kcx.common.utils.encryption;

import java.util.Base64;

/**
 * base64 加密 解密
 */
public class Base64Utils {

    /**
     * base64 加密
     */
    public static String base64Encoder(String str) {
        return new String(Base64.getEncoder().encode(str.getBytes()));
    }

    /**
     * base64 解密
     */
    public static String base64Decoder(String str) {
        return new String(Base64.getDecoder().decode(str));

    }

    public static void main(String[] args) {
        String s = base64Encoder("123456");
        System.out.println(s);
        System.out.println(base64Decoder(s));
    }
}
