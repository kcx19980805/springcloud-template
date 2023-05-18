package com.kcx.common.utils.elasticsearch;

import lombok.Data;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;

import javax.annotation.PostConstruct;

/**
 * ElasticSearch基础配置,使用时继承此类然后从配置文件注入参数值
 * 继承类可以加上@Configuration @ConfigurationProperties(prefix="elasticsearch") @Data
 */
@Data
public class ElasticsearchBaseConfig extends AbstractElasticsearchConfiguration {

    /**
     * ip地址
     */
    private String uris;
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 请求方式https/http
     */
    private String scheme;
    /**
     * 连接池最大并发连接数,设置过小并发请求会串行执行，此时后面被阻塞的请求可能会抛出请求超时异常
     */
    private Integer maxConnTotal;
    /**
     * 单域名下最大连接数
     */
    private Integer maxConnPerRoute;

    /**
     * 注入操作类
     */
    @Override
    public RestHighLevelClient elasticsearchClient() {
        String[] hostArray = uris.split(",");
        HttpHost[] httpHost = new HttpHost[hostArray.length];
        for (int i = 0; i < hostArray.length; i++) {
            String ipAddr = hostArray[i].split(":")[0];
            String port = hostArray[i].split(":")[1];
            httpHost[i] = new HttpHost(ipAddr, Integer.parseInt(port), scheme);
        }
        //连接池配置
        RestClientBuilder restClientBuilder = RestClient.builder(httpHost);
        restClientBuilder.setHttpClientConfigCallback(b -> {
            if (null != maxConnTotal) {
                b.setMaxConnTotal(maxConnTotal);
            }
            if (null != maxConnPerRoute) {
                b.setMaxConnPerRoute(maxConnPerRoute);
            }
            //设置认证信息
            if(null != username && null != password){
                //访问凭证
                CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
                credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(username, password));
                b.setDefaultCredentialsProvider(credentialsProvider);
            }
            return b;
        });
        RestHighLevelClient client = new RestHighLevelClient(restClientBuilder);
        return client;
    }
}
