package com.kcx.api.admin.config.jwt;

import com.kcx.common.utils.token.JWTBaseInterceptor;
import org.springframework.stereotype.Component;

/**
 * 权限(Token)验证，前后端分离导致跨域，session不能保存用户，使用jwt进行登录
 */
@Component
public class JWTInterceptor extends JWTBaseInterceptor {

}
