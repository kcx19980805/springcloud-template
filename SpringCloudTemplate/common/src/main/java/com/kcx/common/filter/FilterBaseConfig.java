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
     * 处理所有请求参数特殊字符过滤器
     */
    @Bean
    public FilterRegistrationBean paramsFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        //设置调度类型为请求
        registration.setDispatcherTypes(DispatcherType.REQUEST);
        //设置过滤器
        registration.setFilter(new ParamsFilter());
        //设置拦截路径
        registration.addUrlPatterns("/*");
        //设置过滤器名称
        registration.setName("paramsFilter");
        //设置优先级
        registration.setOrder(1);
        return registration;
    }

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
        registration.setOrder(2);
        return registration;
    }

}
