package com.kcx.api.admin.config.websocket;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import javax.annotation.Resource;

/**
 * WebSocket配置
 */
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Resource
    private WebSocketInterceptor webSocketInterceptor;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        //设置拦截路径
        registry.addHandler(webSocketHandlerCustomBean(), "/socket/client").addInterceptors(webSocketInterceptor).setAllowedOrigins("*");
        //设置拦截路径
        registry.addHandler(WebSocketHandlerPayBean(), "/socket/pay").addInterceptors(webSocketInterceptor).setAllowedOrigins("*");
    }

    /**
     * 注入WebSocket处理类
     * @return
     */
    @Bean
    public WebSocketHandler webSocketHandlerCustomBean() {
        return new WebSocketHandlerChat();
    }

    /**
     * 注入WebSocket处理类
     * @return
     */
    @Bean
    public WebSocketHandler WebSocketHandlerPayBean() {
        return new WebSocketHandlerPay();
    }
}
