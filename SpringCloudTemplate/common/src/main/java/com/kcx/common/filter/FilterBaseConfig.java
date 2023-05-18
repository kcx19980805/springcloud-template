package com.kcx.common.filter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

import javax.servlet.DispatcherType;

/**
 * 网址过滤基类，子类继承并@Configuration注入
 * springboot过滤器FilterRegistrationBean可以注册多个过滤器
 */
public class FilterBaseConfig {

    /**
     *XSS攻击过滤
     */
    @Bean
    public FilterRegistrationBean xssFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setDispatcherTypes(DispatcherType.REQUEST);
        registration.setFilter(new XssFilter());
        registration.addUrlPatterns("/*");
        registration.setName("xssFilter");
        registration.setOrder(1);
        return registration;
    }

}
