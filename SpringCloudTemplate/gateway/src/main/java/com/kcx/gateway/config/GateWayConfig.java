package com.kcx.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 网关配置
 * 除了在yml的routes中配置外，还可以使用代码配置
 */
@Configuration
public class GateWayConfig
{
    /**
     * 配置了一个id为route-name的路由规则，
     * 当访问地址 http://127.0.0.1:8897/search/albumsdetail时会自动转发到地址：
     * https://image.baidu.com/search/albumsdetail
     * 当访问地址
     * http://127.0.0.1:8897//search/albumsdetail?tn=albumsdetail&word=城市建筑摄影专题&fr=searchindex_album%20&album_tab=建筑
     * &album_id=7&rn=30时，等同于访问https://image.baidu.com/search/albumsdetail?tn=albumsdetail&word=城市建筑摄影专题
     * &fr=searchindex_album%20&album_tab=建筑&album_id=7&rn=30
     * 这里最后访问的地址
     * @param routeLocatorBuilder
     * @return
     */
/*    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder routeLocatorBuilder)
    {
        RouteLocatorBuilder.Builder routes = routeLocatorBuilder.routes();
        routes.route("testId",
                r -> r.path("/search/albumsdetail").uri("https://image.baidu.com")).build();
        return routes.build();
    }*/
}
