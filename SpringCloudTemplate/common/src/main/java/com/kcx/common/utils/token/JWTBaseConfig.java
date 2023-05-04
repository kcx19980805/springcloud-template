package com.kcx.common.utils.token;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * jwt基础配置，子类继承此类，然后
 * @Component
 * @Data
 * @ConfigurationProperties(prefix = "token")注入
 */
@Data
public class JWTBaseConfig {
    /**
     * 自定义令牌标识
     */
    public String tokenName;

    /**
     * 自定义令牌密钥
     */
    public String secret;

    /**
     * 自定义令牌payload参数
     */
    public String payload;

    /**
     * 自定义令牌有效期
     */
    public Integer expireTime;
}
