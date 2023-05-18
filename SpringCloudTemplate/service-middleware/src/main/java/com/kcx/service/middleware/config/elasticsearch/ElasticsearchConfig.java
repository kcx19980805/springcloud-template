package com.kcx.service.middleware.config.elasticsearch;

import com.kcx.common.utils.elasticsearch.ElasticsearchBaseConfig;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix="spring.elasticsearch.rest")
@Data
public class ElasticsearchConfig  extends ElasticsearchBaseConfig {

}
