package com.kcx.api.admin.config.pay.alibaba;
import com.kcx.common.utils.pay.ali.AlipayBaseConfig;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * 支付宝支付配置
 */
@Configuration
//读取配置文件
@PropertySource("classpath:pay.properties")
@ConfigurationProperties(prefix="alipay")
@Data
public class AlipayConfig extends AlipayBaseConfig {

}
