package com.kcx.common.utils.encryption;


import org.apache.commons.lang3.RandomStringUtils;
import org.apache.shiro.crypto.hash.SimpleHash;

/**
 * sha不可逆加密
 * 对于任意长度的消息，SHA256都会产生一个256位的哈希值，称作消息摘要。
 * 这个摘要相当于是个长度为32个字节的数组，共256位，通常由一个长度为64的十六进制字符串来表示，其中1个字节=8位，一个十六进制的字符的长度为4位
 * 主要用于shiro权限认证
 */
public class SHAUtils {
    /**
     * 密码加密盐值长度 随机字符
     */
    public final static int SALT_LENGTH = 20;
    /**
     * 加密算法 SHA-512
     */
    public final static String HASH_ALGORITHM_NAME_SHA512 = "SHA-512";
    /**
     * 加密算法 SHA-256
     */
    public final static String HASH_ALGORITHM_NAME_SHA256 = "SHA-256";
    /**
     * 循环次数
     */
    public final static int hashIterations = 16;

    /**
     * 生成密码 SHA-256
     * @param password 密码
     * @param salt 盐，即自定义的一个字符串
     * @return
     */
    public static String sha256(String password, String salt) {
        return new SimpleHash(HASH_ALGORITHM_NAME_SHA256, password, salt, hashIterations).toString();
    }

    /**
     * 生成密码 SHA-512
     * @param password 密码
     * @param salt 盐，即自定义的一个字符串
     * @return
     */
    public static String sha512(String password, String salt) {
        return new SimpleHash(HASH_ALGORITHM_NAME_SHA512, password, salt, hashIterations).toString();
    }

    /**
     * 校验SHA-256加密的密码输入是否正确
     * @param encryptionPassword 之前加密过的密码
     * @param password 未加密的密码
     * @param salt 盐，即自定义的一个字符串
     * @return true两个密码相同 false两个密码不同
     */
    public static boolean checkPasswordSha256(String encryptionPassword, String password, String salt) {
        password = new SimpleHash(HASH_ALGORITHM_NAME_SHA256, password, salt, hashIterations).toString();
        return encryptionPassword.equals(password);
    }

    /**
     * 校验SHA-512加密的密码输入是否正确
     * @param encryptionPassword 之前加密过的密码
     * @param password 未加密的密码
     * @param salt 盐，即自定义的一个字符串
     * @return true两个密码相同 false两个密码不同
     */
    public static boolean checkPasswordSha512(String encryptionPassword, String password, String salt) {
        password = new SimpleHash(HASH_ALGORITHM_NAME_SHA512, password, salt, hashIterations).toString();
        return encryptionPassword.equals(password);
    }

    /**
     * 随机生成加密盐
     * 字母大小写不含数字
     */
    public static String getGenerateSalt() {
        return RandomStringUtils.randomAlphabetic(SALT_LENGTH);
    }

    public static void main(String[] args) {
        String password = "123456";
        String salt = getGenerateSalt();
        String encryptPassword = sha256(password, salt);
        System.out.println(password);
        System.out.println(salt);
        System.out.println(encryptPassword);
    }
}
