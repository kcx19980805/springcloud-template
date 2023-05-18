package com.kcx.common.utils.encryption;

import java.util.Base64;

/**
 * base64 编码 解码
 * Base64是网络上最常见的用于传输8Bit字节代码的编码方式之一，Base64并不是安全领域的加密算法，其实Base64只能算是一个编码算法，对数据内容进行编码来适合传输。
 * 标准Base64编码解码无需额外信息即完全可逆，Base64编码本质上是一种将二进制数据转成文本数据的方案。
 * 对于非二进制数据，是先将其转换成二进制形式，然后每连续6比特（2的6次方=64）计算其十进制值，根据该值在A--Z,a--z,0--9,+,/
 * 这64个字符中找到对应的字符，最终得到一个文本字符串。
 */
public class Base64Utils {

    /**
     * base64 编码
     */
    public static String base64Encoder(String str) {
        return new String(Base64.getEncoder().encode(str.getBytes()));
    }

    /**
     * base64 解码
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
