package com.kcx.job.config.filter;

import com.kcx.common.filter.FilterBaseConfig;
import org.springframework.context.annotation.Configuration;

/**
 * 过滤器配置
 */
@Configuration
public class FilterConfig extends FilterBaseConfig {

    /**
     * shiro过滤器
     */
/*    @Bean
    public FilterRegistrationBean shiroFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new DelegatingFilterProxy("shiroFilter"));
        //该值缺省为false，表示生命周期由SpringApplicationContext管理，设置为true则表示由ServletContainer管理
        registration.addInitParameter("targetFilterLifecycle", "true");
        registration.addUrlPatterns("/*");
        registration.setOrder(3);
        return registration;
    }*/


}
