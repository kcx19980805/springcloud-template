package com.kcx.job.config.redis;

import com.kcx.common.utils.redis.RedisBaseConfig;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

/**
 * RedisTemplate配置
 */
@Configuration
@EnableCaching
public class RedisConfig extends RedisBaseConfig {

}
