package com.kcx.common.utils.encryption;

import org.apache.commons.lang3.RandomStringUtils;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * AES加密解密
 * 对称加密算法
 */
public class AESUtils {
    /**
     * AES加密
     * @param key 类似盐，即自定义的一个字符串
     * @param password 密码
     * @return
     */
    public static String encryptByAES(String key, String password){
        SecretKeySpec secretKeySpec = new SecretKeySpec (key.getBytes(), "AES");
        try {
            Cipher cipher = Cipher.getInstance ("AES");
            cipher.init (Cipher.ENCRYPT_MODE, secretKeySpec);
            // 加密
            byte[] bytes = cipher.doFinal(password.getBytes());
            return Base64.getEncoder().encodeToString(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //AES 解密 返回的是base64格式
    public static String decryptByAES(String key, String password){
        SecretKeySpec secretKeySpec = new SecretKeySpec (key.getBytes(), "AES");
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance ("AES");
            cipher.init (Cipher.DECRYPT_MODE, secretKeySpec);
            // 解密
            byte[] bytes = cipher.doFinal(Base64.getDecoder().decode(password));
            return new String (bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 随机生成加密盐
     * 字母大小写不含数字
     */
    public static String getGenerateKey() {
        return RandomStringUtils.randomAlphabetic(16);
    }

    public static void main(String[] args) {
        String key = getGenerateKey();
        String encryptPassword = encryptByAES(key,"123456");
        String decryptPassword = decryptByAES(key,encryptPassword);
        System.out.println(encryptPassword);
        System.out.println(decryptPassword);
    }
}
