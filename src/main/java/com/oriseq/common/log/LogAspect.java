package com.oriseq.common.log;

import cn.hutool.core.date.DateTime;
import com.oriseq.config.LoginTool;
import com.oriseq.controller.utils.Result;
import com.oriseq.dtm.dto.LoginUser;
import com.oriseq.dtm.entity.Log;
import com.oriseq.service.LogService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.ContentCachingRequestWrapper;

import java.nio.charset.StandardCharsets;

@Aspect
@Component
@Slf4j
public class LogAspect {

    @Autowired
    private LogService logService;
    @Autowired
    private LoginTool loginTool;

    // 定义切点：匹配带有 @EnableLogging 注解的方法
    @Pointcut("@annotation(com.oriseq.common.log.EnableLogging)")
    public void methodPointcut() {
    }

    // 定义切点：匹配带有 @EnableLogging 注解的类
    @Pointcut("@within(com.oriseq.common.log.EnableLogging)")
    public void classPointcut() {
    }

    // 组合切点：匹配方法或类上的 @EnableLogging 注解
    @Pointcut("methodPointcut() || classPointcut()")
    public void logPointcut() {
    }


    @AfterReturning(pointcut = " logPointcut()", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();

        Log log = buildLog(joinPoint, request, response);
        if (result instanceof Result<?>) {
            log.setMessage(((Result<?>) result).getMessage());
            log.setResponseStatus(((Result<?>) result).getCode());
        } else {
            log.setMessage("Method executed successfully");
        }
        log.setException(null);

        logService.saveLog(log);
    }

    @AfterThrowing(pointcut = " logPointcut()", throwing = "exception")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable exception) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();

        Log log = buildLog(joinPoint, request, response);
        log.setMessage("Method execution failed");
        log.setException(exception.getMessage());

        logService.saveLog(log);
    }

    private Log buildLog(JoinPoint joinPoint, HttpServletRequest request, HttpServletResponse response) {
        Log logg = new Log();
        logg.setLogTime(DateTime.now());
        logg.setClassName(joinPoint.getTarget().getClass().getName());
//        log.setMethod(((MethodSignature) joinPoint.getSignature()).getMethod().getName());
        logg.setMethod(joinPoint.getSignature().getName());
        try {
            logg.setUserId(getUserIdFromRequest(request)); // 假设从请求中获取用户ID
        } catch (Exception e) {
            log.warn("存储日志时，获取用户ID失败");
        }
        logg.setIp(request.getRemoteAddr());
        logg.setRequestUrl(request.getRequestURL().toString());
        logg.setRequestMethod(request.getMethod());
        // 获取请求参数
        String requestParams = getRequestParams(request);
        logg.setRequestParams(requestParams);

        return logg;
    }


    private String getRequestParams(HttpServletRequest request) {
        StringBuilder params = new StringBuilder();

        // 获取URL中的查询参数
        String queryString = request.getQueryString();
        if (queryString != null) {
            params.append("Query Params: ").append(queryString).append("; ");
        }

        // 获取POST请求体中的参数
        if (request instanceof ContentCachingRequestWrapper) {
            ContentCachingRequestWrapper wrappedRequest = (ContentCachingRequestWrapper) request;
            byte[] contentAsByteArray = wrappedRequest.getContentAsByteArray();
            if (contentAsByteArray.length > 0) {
                String body = new String(contentAsByteArray, StandardCharsets.UTF_8);
                params.append("Body: ").append(body);
            }
        }

        return params.toString();
    }

    private String getUserIdFromRequest(HttpServletRequest request) {
        // 从请求中获取用户ID的逻辑
        LoginUser userInfo = loginTool.getUserInfo(request);
        return userInfo.getUserId();
    }
}