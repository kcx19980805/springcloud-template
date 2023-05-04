package com.kcx.api.user.config.pay.wechat;

import com.kcx.common.utils.pay.wx.v3.WxPayV3BaseConfig;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * 微信支付配置
 */
@Configuration
//读取配置文件
@PropertySource("classpath:pay.properties")
@ConfigurationProperties(prefix="wxpay")
@Data
public class WxPayConfig extends WxPayV3BaseConfig {

}
