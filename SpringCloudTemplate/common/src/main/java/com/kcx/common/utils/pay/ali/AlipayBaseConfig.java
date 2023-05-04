package com.kcx.common.utils.pay.ali;

import com.alipay.api.*;
import lombok.Data;
import org.springframework.context.annotation.Bean;

/**
 * 支付宝支付基础配置,使用时继承此类然后从配置文件注入参数值
 * 继承类可以加上@Configuration @PropertySource("classpath:pay.properties") @ConfigurationProperties(prefix="alipay") @Data
 */
@Data
public class AlipayBaseConfig {
    /**
     * APPID
     */
    public String appId;
    /**
     * 支付宝网关
     */
    public String gatewayUrl;
    /**
     * 支付宝公钥
     */
    public String alipayPublicKey;

    /**
     * 开发者私钥
     */
    public String privateKey;

    /**
     * 得到能够调用所有支付宝支付api的AlipayClient
     */
    @Bean(name = "alipayClient")
    public AlipayClient alipayClient() throws AlipayApiException {
        AlipayConfig alipayConfig = new AlipayConfig();
        //设置网关地址
        alipayConfig.setServerUrl(gatewayUrl);
        //设置应用Id
        alipayConfig.setAppId(appId);
        //设置应用私钥
        alipayConfig.setPrivateKey(privateKey);
        //设置请求格式，固定值json
        alipayConfig.setFormat(AlipayConstants.FORMAT_JSON);
        //设置字符集
        alipayConfig.setCharset(AlipayConstants.CHARSET_UTF8);
        //设置支付宝公钥
        alipayConfig.setAlipayPublicKey(alipayPublicKey);
        //设置签名类型
        alipayConfig.setSignType(AlipayConstants.SIGN_TYPE_RSA2);
        //构造client
        AlipayClient alipayClient = new DefaultAlipayClient(alipayConfig);
        return alipayClient;
    }
}
