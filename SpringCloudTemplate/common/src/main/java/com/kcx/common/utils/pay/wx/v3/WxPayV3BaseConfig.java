package com.kcx.common.utils.pay.wx.v3;
import com.wechat.pay.contrib.apache.httpclient.WechatPayHttpClientBuilder;
import com.wechat.pay.contrib.apache.httpclient.auth.PrivateKeySigner;
import com.wechat.pay.contrib.apache.httpclient.auth.Verifier;
import com.wechat.pay.contrib.apache.httpclient.auth.WechatPay2Credentials;
import com.wechat.pay.contrib.apache.httpclient.auth.WechatPay2Validator;
import com.wechat.pay.contrib.apache.httpclient.cert.CertificatesManager;
import com.wechat.pay.contrib.apache.httpclient.util.PemUtil;
import lombok.Data;
import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.context.annotation.Bean;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;

/**
 * 微信商户平台支付基础配置,使用时继承此类然后从配置文件注入参数值
 * 继承类可以加上@Configuration @PropertySource("classpath:pay.properties") @ConfigurationProperties(prefix="wxpay") @Data
 */
@Data
public class WxPayV3BaseConfig {
    /**
     * 小程序appid
     */
    public String appid;
    /**
     * 商户号
     */
    public String merchantId;

    /**
     * API V3密钥
     */
    public String apiV3Key;

    /**
     * 商户证书序列号
     */
    public String merchantSerialNumber;

    /**
     * 商户私钥文件
     */
    public String privateKeyPath;

    /**
     * 私钥
     */
    @Bean(name = "wxPayPrivateKey")
    public PrivateKey getPrivateKey() {
        try {
            return PemUtil.loadPrivateKey(new FileInputStream(privateKeyPath));
        } catch (FileNotFoundException e) {
            throw new RuntimeException("私钥文件不存在："+privateKeyPath);
        }
    }

    /**
     * 获取签名验证器
     */
    @Bean(name = "wxPayVerifier")
    public Verifier getVerifier(PrivateKey wxPayPrivateKey) throws Exception {
        //证书管理器
        CertificatesManager certificatesManager = CertificatesManager.getInstance();
        certificatesManager.putMerchant(merchantId, new WechatPay2Credentials(merchantId,
                        new PrivateKeySigner(merchantSerialNumber, wxPayPrivateKey)),
                apiV3Key.getBytes(StandardCharsets.UTF_8));
        return certificatesManager.getVerifier(merchantId);
    }

    /**
     * 获取商户私钥并验签过的请求对象
     */
    @Bean(name = "wxPayClient")
    public CloseableHttpClient getHttpClient(Verifier wxPayVerifier, PrivateKey wxPayPrivateKey) {
        // 通过WechatPayHttpClientBuilder构造的HttpClient，会自动的处理签名和验签，并进行证书自动更新
        WechatPayHttpClientBuilder builder = WechatPayHttpClientBuilder.create()
                .withMerchant(merchantId, merchantSerialNumber, wxPayPrivateKey)
                .withValidator(new WechatPay2Validator(wxPayVerifier));
        return builder.build();
    }

    /**
     * 获取商户私钥无需验签的请求对象
     */
    @Bean(name = "wxPayNoSignClient")
    public CloseableHttpClient getWxPayNoSignClient(PrivateKey wxPayPrivateKey){
        //用于构造HttpClient
        WechatPayHttpClientBuilder builder = WechatPayHttpClientBuilder.create()
                //设置商户信息
                .withMerchant(merchantId, merchantSerialNumber, wxPayPrivateKey)
                //无需进行签名验证、通过withValidator((response) -> true)实现
                .withValidator((response) -> true);
        // 通过WechatPayHttpClientBuilder构造的HttpClient，会自动的处理签名和验签，并进行证书自动更新
        return builder.build();
    }
}
