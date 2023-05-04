package com.kcx.common.exception;

import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import com.kcx.common.constant.HttpStatus;
import com.kcx.common.utils.Result;
import feign.FeignException;
import feign.RetryableException;
import org.apache.shiro.authz.AuthorizationException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.lang.reflect.UndeclaredThrowableException;

/**
 * 异常拦截统一处理类
 * 子类直接继承加上@RestControllerAdvice
 */
public class ExceptionBaseInterceptor {

    /**
     * 请求方式不允许
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Result httpRequestMethodNotSupportedExceptionHandler(HttpRequestMethodNotSupportedException e) {
        return handleException(e, HttpStatus.ERROR, "请求方式不允许get/post");
    }

    /**
     * 自定义异常
     */
    @ExceptionHandler(CustomException.class)
    public Result handleRRException(CustomException e) {
        return handleException(e, e.getCode(), e.getMessage());
    }

    /**
     * shiro异常
     */
    @ExceptionHandler(AuthorizationException.class)
    public Result handleAuthorizationException(AuthorizationException e) {
        return handleException(e, HttpStatus.ERROR, "无访问权限");
    }

    /**
     * 验证异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result validExceptionHandler(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getFieldError().getDefaultMessage();
        return handleException(e, HttpStatus.ERROR, message);
    }

    /**
     * 验证异常
     */
    @ExceptionHandler(BindException.class)
    public Result validatedBindException(BindException e) {
        String message = e.getAllErrors().get(0).getDefaultMessage();
        return handleException(e, HttpStatus.ERROR, message);
    }

    /**
     * feign请求超时
     */
    @ExceptionHandler(RetryableException.class)
    public Result retryableExceptionHandler(RetryableException e) {
        return handleException(e, HttpStatus.ERROR, "远程请求超时");
    }

    /**
     * feign请求异常，例如sentinel限流等报错
     */
    @ExceptionHandler(FeignException.class)
    public Result feignExceptionHandler(FeignException e) {
        return handleException(e, HttpStatus.ERROR, "feign请求异常：" + e.getMessage());
    }

    /**
     * 全局处理sentinel限流异常
     * 如果加了@SentinelResource(value = "xxx"),因为没有指定异常处理方法，在这里捕获
     */
    @ExceptionHandler(UndeclaredThrowableException.class)
    public Result undeclaredThrowableExceptionHandler(UndeclaredThrowableException e) {
        if (e.getCause() instanceof FlowException) {
            return handleException(null, HttpStatus.FORBIDDEN, "当前访问人数过多，请稍后访问" + e.getCause().getClass().getCanonicalName());
        }
        return handleException(e, HttpStatus.ERROR, "未知异常:" + e.getMessage());
    }

    /**
     * 系统异常
     */
    @ExceptionHandler(Exception.class)
    public Result handleException(Exception e) {
        return handleException(e, HttpStatus.ERROR, "未知异常:" + e.getMessage());
    }

    /**
     * 统一处理异常
     *
     * @param code
     * @param message
     * @return
     */
    Result handleException(Exception e, int code, String message) {
        if (null != e) {
            e.printStackTrace();
        }
        if (message.length() > 100) {
            message = message.substring(0, 100);
        }
        Result response = new Result();
        response.setCode(code);
        response.setMsg(message);
        return response;
    }
}
