package com.kcx.api.user.config.jwt;

import com.kcx.common.utils.token.JWTBaseConfig;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * token 相关配置
 *
 */
@Component
@Data
@ConfigurationProperties(prefix = "token")
public class JWTConfig extends JWTBaseConfig {


}
