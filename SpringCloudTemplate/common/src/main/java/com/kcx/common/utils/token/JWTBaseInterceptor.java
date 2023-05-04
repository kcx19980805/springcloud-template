package com.kcx.common.utils.token;

import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.kcx.common.constant.HttpStatus;
import com.kcx.common.exception.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * JWT拦截器基类，子类继承然后@Component注入
 */
@Slf4j
public  class JWTBaseInterceptor  implements HandlerInterceptor {

    @Autowired
    JWTBaseConfig jwtConfig;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        log.info("JWT拦截器，请求uri===={}",request.getRequestURI());
        String token = request.getHeader(jwtConfig.getTokenName());
        if(StringUtils.isBlank(token)){
            throw new CustomException("请在请求头添加访问令牌:"+jwtConfig.getTokenName(), HttpStatus.FORBIDDEN);
        }
        try {
            DecodedJWT verify = JWTUtils.verifyToken(jwtConfig,token);//验证令牌
            return true;
        }catch (SignatureVerificationException e){
            e.printStackTrace();
            throw new CustomException("token签名异常", HttpStatus.FORBIDDEN);
        }catch (TokenExpiredException e){
            throw new CustomException("token已过期", HttpStatus.FORBIDDEN);
        }catch (AlgorithmMismatchException e){
            e.printStackTrace();
            throw new CustomException("token算法不一致", HttpStatus.FORBIDDEN);
        }catch (Exception e){
            e.printStackTrace();
            throw new CustomException("token无效，"+e.getMessage(), HttpStatus.FORBIDDEN);
        }
    }
}
