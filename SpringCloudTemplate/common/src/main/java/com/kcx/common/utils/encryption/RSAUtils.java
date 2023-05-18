package com.kcx.common.utils.encryption;

import javax.crypto.Cipher;
import java.io.*;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;

/**
 * RSA非对称加密算法,数据传输中如果被篡改解密会抛出异常
 * 非对称加密：加密和解密使用的不是相同的密钥，只有同一个公钥-私钥对才能正常加解密。
 * 非对称加密的优点：对称加密需要协商密钥，而非对称加密可以安全地公开各自的公钥，在N个人之间通信的时候：
 * 使用非对称加密只需要N个密钥对，每个人只管理自己的密钥对。而使用对称加密需要则需要N*(N-1)/2个密钥，
 * 因此每个人需要管理N-1个密钥，密钥管理难度大，而且非常容易泄漏。
 * 非对称加密的缺点：运算速度非常慢，比对称加密要慢很多。
 * 微信支付就是使用这个算法
 */
public class RSAUtils {

    /**
     * 生成秘钥对并保存在文件
     * @param publicKeyPath 公钥文件全路径 xxx.pem
     * @param privateKeyPath 私钥文件全路径 xxx.pem
     */
    public static void generateKeyPairToFile(String publicKeyPath,String privateKeyPath){
        BufferedWriter publicKeyBW = null;
        BufferedWriter privateKeyBW = null;
        try {
            publicKeyBW = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(publicKeyPath)));
            privateKeyBW = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(privateKeyPath)));
            List<byte[]> keyPair = generateKeyPair();
            String publicKey = new String(Base64.getEncoder().encode(keyPair.get(0)));
            String privateKey = new String(Base64.getEncoder().encode(keyPair.get(1)));
            //写公钥
            publicKeyBW.write("-----BEGIN PUBLIC KEY-----");
            publicKeyBW.newLine();
            writeKeyToFile(publicKeyBW,publicKey);
            publicKeyBW.write("-----END PUBLIC KEY-----");
            //写私钥
            privateKeyBW.write("-----BEGIN PRIVATE KEY-----");
            privateKeyBW.newLine();
            writeKeyToFile(privateKeyBW,privateKey);
            privateKeyBW.write("-----END PRIVATE KEY-----");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if(null != publicKeyBW){
                    publicKeyBW.close();
                }
                if(null != privateKeyBW){
                    privateKeyBW.close();
                }
            } catch (IOException e) {
                //忽略
            }
        }
    }

    /**
     * 生成秘钥对，每次都会生成不同的
     * @return list0为公钥 list1为私钥
     */
    public static List<byte[]> generateKeyPair(){
        // 生成公钥／私钥对:
        KeyPairGenerator kpGen = null;
        try {
            kpGen = KeyPairGenerator.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        kpGen.initialize(1024);
        KeyPair kp = kpGen.generateKeyPair();
        // 公钥:
        byte[] publicKey = kp.getPublic().getEncoded();
        // 私钥:
        byte[] privateKey = kp.getPrivate().getEncoded();
        return new ArrayList<byte[]>(){{add(publicKey);add(privateKey);}};
    }

    /**
     * 将秘钥写入流
     * @param bufferedWriter 文件输出对象
     * @param key 秘钥字符串
     */
    public static void writeKeyToFile(BufferedWriter bufferedWriter,String key){
        int lineCount = 65;
        for (int i = 0; i < key.length(); i+=lineCount) {
            int end = i+lineCount;
            try {
                bufferedWriter.write(key.substring(i,end>key.length()?key.length():end));
                bufferedWriter.newLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 使用公钥文件进行RSA加密
     * @param content 原文字符串
     * @param publicKeyPath 公钥文件全路径 xxx.pem
     * @return 密文字符串
     */
    public static String publicKeyEncrypt(String content, String publicKeyPath){
        return publicKeyEncrypt(content,getPublicKey(publicKeyPath));
    }

    /**
     * 使用私钥文件进行RSA解密
     * @param content 密文字符串
     * @param privateKeyPath 私钥文件全路径 xxx.pem
     * @return 原文字符串
     */
    public static String privateKeyDecrypt(String content, String privateKeyPath){
        return privateKeyDecrypt(content,getPrivateKey(privateKeyPath));
    }

    /**
     * 使用公钥进行RSA加密
     * @param content 原文字符串
     * @param publicKey 公钥
     * @return 密文字符串
     */
    public static String publicKeyEncrypt(String content, PublicKey publicKey){
        //RSA加密
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            //当长度过长的时候，需要分割后加密 117个字节
            byte[] resultBytes = getEncryptOrDecrypt(content.getBytes(),cipher,117);
            String outStr = new String(Base64.getEncoder().encode(resultBytes));
            return outStr;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 使用私钥进行RSA解密
     * @param content 密文字符串
     * @param privateKey 私钥
     * @return 原文字符串
     */
    public static String privateKeyDecrypt(String content, PrivateKey privateKey){
        //RSA解密
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            //当长度过长的时候，需要分割后解密 128个字节
            String outStr = new String(getEncryptOrDecrypt(Base64.getDecoder().decode(content.getBytes("UTF-8")),cipher,128));
            return outStr;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 分段执行加密解密
     * @param bytes 原文/密文
     * @param cipher 加密或解密对象
     * @param max 每段最大字节数
     * @return 原文/密文
     */
    private static byte[] getEncryptOrDecrypt(byte[] bytes, Cipher cipher,int max){
        byte[] inputArray = bytes;
        int inputLength = inputArray.length;
        // 最大加密字节数，超出最大字节数需要分组加密
        int MAX_ENCRYPT_BLOCK = max;
        // 标识
        int offSet = 0;
        byte[] resultBytes = {};
        byte[] cache = {};
        try {
            while (inputLength - offSet > 0) {
                if (inputLength - offSet > MAX_ENCRYPT_BLOCK) {
                    cache = cipher.doFinal(inputArray, offSet, MAX_ENCRYPT_BLOCK);
                    offSet += MAX_ENCRYPT_BLOCK;
                } else {
                    cache = cipher.doFinal(inputArray, offSet, inputLength - offSet);
                    offSet = inputLength;
                }
                resultBytes = Arrays.copyOf(resultBytes, resultBytes.length + cache.length);
                System.arraycopy(cache, 0, resultBytes, resultBytes.length - cache.length, cache.length);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return resultBytes;
    }

    /**
     * 从公钥文件读取公钥对象
     * @param publicKeyPath 公钥文件全路径 xxx.pem
     * @return 公钥对象
     */
    public static PublicKey getPublicKey(String publicKeyPath){
        try {
            String publicKey = readFileToString(publicKeyPath);
            publicKey = publicKey.replace("-----BEGIN PUBLIC KEY-----", "")
                    .replace("-----END PUBLIC KEY-----", "")
                    .replaceAll("\\s+", "");
            return getPublicKey(Base64.getDecoder().decode(publicKey));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 从私钥文件读取私钥对象
     * @param privateKeyPath 私钥文件全路径 xxx.pem
     * @return 私钥对象
     */
    public static PrivateKey getPrivateKey(String privateKeyPath){
        try {
            String privateKey = readFileToString(privateKeyPath);
            privateKey = privateKey.replace("-----BEGIN PRIVATE KEY-----", "")
                    .replace("-----END PRIVATE KEY-----", "")
                    .replaceAll("\\s+", "");
            return getPrivateKey(Base64.getDecoder().decode(privateKey));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将公钥/私钥文件读取为String
     * @param filePath 文件路径
     * @return 公钥/私钥字符串
     */
    public static String readFileToString(String filePath){
        BufferedInputStream bufferedInputStream = null;
        ByteArrayOutputStream os = null;
        try {
            bufferedInputStream = new BufferedInputStream(new FileInputStream(filePath));
            os = new ByteArrayOutputStream(2048);
            byte[] buffer = new byte[1024];
            int length;
            while((length = bufferedInputStream.read(buffer)) != -1) {
                os.write(buffer, 0, length);
            }
            return os.toString("UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if(null != bufferedInputStream){
                    bufferedInputStream.close();
                }
                if(null != os){
                    os.close();
                }
            } catch (IOException e) {
                //忽略
            }
        }
        return null;
    }

    /**
     * 将byte[]转为公钥对象
     * @param publicKey
     * @return
     */
    public static PublicKey getPublicKey(byte[] publicKey){
        try {
            KeyFactory kf = KeyFactory.getInstance("RSA");
            return kf.generatePublic(new X509EncodedKeySpec(publicKey));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据byte[]转为私钥对象
     * @param privateKey
     * @return
     */
    public static PrivateKey getPrivateKey(byte[] privateKey){
        try {
            KeyFactory kf = KeyFactory.getInstance("RSA");
            return kf.generatePrivate(new PKCS8EncodedKeySpec(privateKey));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }



    public static void main(String[] args) throws Exception {
/*        String plain = "RSA加密原文";
        generateKeyPairToFile("D:\\public.pem","D:\\private.pem");
        String encrypted = publicKeyEncrypt(plain,"D:\\public.pem");
        System.out.println("公钥加密:"+encrypted);
        String decrypt = privateKeyDecrypt(encrypted,"D:\\private.pem");
        System.out.println("私钥解密: "+decrypt);*/
    }
}
