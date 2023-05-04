package com.kcx.api.user.config.sysLog;


import com.kcx.api.user.config.jwt.JWTConfig;
import com.kcx.api.user.sys.service.SysOperLogService;
import com.alibaba.fastjson.JSON;
import com.kcx.common.entity.database1.SysOperLogEntity;
import com.kcx.common.log.SysLog;
import com.kcx.common.log.SysLogBaseAspect;
import com.kcx.common.utils.ip.IpUtils;
import com.kcx.common.utils.token.JWTUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

/**
 * AOP自动保存操作日志到数据库
 */
@Aspect
@Component
public class SysLogAspect extends SysLogBaseAspect {
    @Resource
    private SysOperLogService sysOperLogService;
    @Resource
    private JWTConfig jwtConfig;


    /**
     * @param joinPoint
     * @param time       操作时长
     * @param e          异常
     * @param jsonResult 返回参数
     */
    public void saveSysLog(ProceedingJoinPoint joinPoint, long time, final Exception e, Object jsonResult) {
        // 获得注解
        SysLog syslog = getAnnotationLog(joinPoint);
        if (syslog == null) {
            return;
        }
        //得到请求对象
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes) attributes).getRequest();
        SysOperLogEntity sysLog = new SysOperLogEntity();
        sysLog.setTitle(syslog.title());
        //设置IP地址
        sysLog.setOperIp(IpUtils.getIpAddress(request));
        //操作人员账号
        String token = request.getHeader(jwtConfig.getTokenName());
        sysLog.setOperUserId(Long.valueOf(JWTUtils.getPayloadValue(jwtConfig,token)));
        sysLog.setOperUserType("0");
        if (e != null) {
            sysLog.setStatus(1);
            sysLog.setErrorMsg(e.getMessage());
        }
        //设置请求参数
        //请求方法
        String method = request.getMethod().toLowerCase();
        if ("put".equals(method) || "post".equals(method)) {
            String params = argsArrayToString(joinPoint.getArgs());
            sysLog.setOperParam(StringUtils.substring(params, 0, 20000));
        } else {
            sysLog.setOperParam(StringUtils.substring(request.getQueryString(), 0, 20000));
        }
        // 返回参数
        sysLog.setJsonResult(JSON.toJSONString(jsonResult));
        sysLog.setOperTime(time);
        sysLog.setCreateTime(LocalDateTime.now());
        //保存系统日志
        sysOperLogService.save(sysLog);
    }

}
