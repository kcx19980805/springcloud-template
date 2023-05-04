package com.kcx.common.log;

import com.alibaba.fastjson.JSON;
import com.kcx.common.entity.database1.SysOperLogEntity;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * 操作日志AOP基类，子类继承实现方法  @Aspect @Component注入
 */
public abstract class SysLogBaseAspect {
    //统计请求的处理时间
    Long startTime = null;
    /**
     * 配置织入点 @sysLog注解
     */
    @Pointcut("@annotation(com.kcx.common.log.SysLog)")
    public void logPointCut() {

    }

    /**
     * 异常切入点记录异常日志 扫描所有controller包下操作
     */
/*    @Pointcut("execution(* com.kcx.api.user.sys.controller..*.*(..))")
    public void exceptionLogPointCut() {
    }*/


    @Before(value = "logPointCut()")
    public void before(JoinPoint joinPoint) {
        startTime = System.currentTimeMillis();
    }


    /**
     * 处理完请求后执行
     *
     * @param joinPoint 切点
     */
    @AfterReturning(pointcut = "logPointCut()", returning = "jsonResult")
    public void doAfterReturning(JoinPoint joinPoint, Object jsonResult) {
        saveSysLog((ProceedingJoinPoint) joinPoint, System.currentTimeMillis() - startTime, null, jsonResult);
    }

    /**
     * 拦截异常操作
     *
     * @param joinPoint 切点
     * @param e         异常
     */
    @AfterThrowing(value = "logPointCut()", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Exception e) {
        saveSysLog((ProceedingJoinPoint) joinPoint, System.currentTimeMillis() - startTime, e, e);
    }


    /**
     * 保存操作日志
     * @param joinPoint
     * @param time       操作时长
     * @param e          异常
     * @param jsonResult 返回参数
     */
    public abstract void saveSysLog(ProceedingJoinPoint joinPoint, long time, final Exception e, Object jsonResult);


    /**
     * 获取请求的参数，放到log中
     *
     * @param operLog 操作日志
     * @throws Exception 异常
     */
    public void setRequestValue(JoinPoint joinPoint, SysOperLogEntity operLog) {

        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes) attributes).getRequest();
        //请求方法
        String method = request.getMethod().toLowerCase();
        if ("put".equals(method) || "post".equals(method)) {
            String params = argsArrayToString(joinPoint.getArgs());
            operLog.setOperParam(StringUtils.substring(params, 0, 20000));
        } else {
            operLog.setOperParam(StringUtils.substring(request.getQueryString(), 0, 20000));
        }
    }

    /**
     * 是否存在注解，如果存在就获取
     */
    public SysLog getAnnotationLog(JoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();

        if (method != null) {
            return method.getAnnotation(SysLog.class);
        }
        return null;
    }

    /**
     * 参数拼装
     */
    public String argsArrayToString(Object[] paramsArray) {
        String params = "";
        if (paramsArray != null && paramsArray.length > 0) {
            for (int i = 0; i < paramsArray.length; i++) {
                if (!isFilterObject(paramsArray[i])) {
                    Object jsonObj = JSON.toJSON(paramsArray[i]);
                    params += jsonObj.toString() + " ";
                }
            }
        }
        return params.trim();
    }

    /**
     * 判断是否需要过滤的对象。
     *
     * @param o 对象信息。
     * @return 如果是需要过滤的对象，则返回true；否则返回false。
     */
    public boolean isFilterObject(final Object o) {
        return o instanceof MultipartFile || o instanceof HttpServletRequest || o instanceof HttpServletResponse;
    }
}
