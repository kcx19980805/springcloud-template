package com.kcx.api.user.config.webmvc;

import com.kcx.api.user.config.file.FileConfig;
import com.kcx.api.user.config.jwt.JWTInterceptor;
import com.kcx.common.constant.Constants;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;
import java.util.ArrayList;

/**
 * WebMvc配置
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Resource
    private JWTInterceptor jwtInterceptor;

    @Resource
    private FileConfig fileConfig;

    /**
     * 添加拦截器
     *
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 所有接口都需token验证,排除不需要的
        registry.addInterceptor(jwtInterceptor).addPathPatterns("/**").excludePathPatterns(outUrls());
    }

    /**
     * 添加资源处理
     *
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //配置一个静态文件的路径 否则css和js无法使用，虽然默认的静态资源是放在static下，但是没有配置里面的文件夹
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
        //拦截文件上传下载路径并解析为配置路径
        registry.addResourceHandler(Constants.RESOURCE_UPLOAD + "/**").addResourceLocations("file:" + fileConfig.getUploadPath());
        registry.addResourceHandler(Constants.RESOURCE_DOWNLOAD + "/**").addResourceLocations("file:" + fileConfig.getDownloadPath());
    }

    /**
     * 设置跨域访问
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        //设置允许跨域的路径
        registry.addMapping("/**")
                //设置允许跨域请求的域名
                .allowedOriginPatterns("*")
                //是否允许证书 不再默认开启
                .allowCredentials(true)
                //设置允许的方法
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                //跨域允许时间
                .maxAge(7200);
    }

    /**
     * 所有排除不需要token的urls
     *
     * @return
     */
    public static ArrayList outUrls() {
        ArrayList arrayList = new ArrayList();
        //必要的
        arrayList.add(Constants.RESOURCE_UPLOAD + "/**");
        arrayList.add(Constants.RESOURCE_DOWNLOAD + "/**");
        arrayList.add("/v2/api-docs");
        arrayList.add("/statics/**");
        arrayList.add("/swagger-ui.html");
        arrayList.add("/doc.html");
        arrayList.add("/swagger-resources/**");
        arrayList.add("/webjars/**");
        arrayList.add("/**.html");
        arrayList.add("/**.js");
        arrayList.add("/**.css");
        //下面是接口路径
        arrayList.add("/user/sendSms");
        arrayList.add("/user/register");
        arrayList.add("/user/login");
        arrayList.add("/dict/list");

        return arrayList;
    }
}
