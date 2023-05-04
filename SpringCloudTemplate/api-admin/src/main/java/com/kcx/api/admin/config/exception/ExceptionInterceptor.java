package com.kcx.api.admin.config.exception;

import com.kcx.common.exception.ExceptionBaseInterceptor;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 异常处理器，避免大量try-catch
 *
 * @RestControllerAdvice捕捉全局存在@RestController注解的类的异常
 * @ControllerAdvice捕捉全局存在@Controller注解的类的异常
 */
@RestControllerAdvice
public class ExceptionInterceptor extends ExceptionBaseInterceptor {

}
