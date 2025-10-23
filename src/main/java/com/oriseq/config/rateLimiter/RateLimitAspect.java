package com.oriseq.config.rateLimiter;

import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;

@Aspect
@Component
public class RateLimitAspect {

    @Autowired
    private RedisRateLimiter redisRateLimiter;

    @Pointcut("@annotation(com.oriseq.config.rateLimiter.RateLimit)")
    public void rateLimitPointcut() {
    }

    @Around("rateLimitPointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        // 获取请求 IP
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String ipAddress = request.getRemoteAddr();

        // 获取目标方法
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        // 方法所在的类名
        // 获取方法所属的类
        Class<?> clazz = method.getDeclaringClass();
        String className = clazz.getName();
        String methodName = method.getName();

        String suffix = ":" + className + "_" + methodName + ":" + ipAddress;

        // 获取所有 RateLimit 注解
        RateLimit[] rateLimits = method.getAnnotationsByType(RateLimit.class);

//        int index = 1;
        for (RateLimit limit : rateLimits) {
            if (!redisRateLimiter.acquireLimit(suffix, limit.limit(), limit.duration())) {
                throw new RuntimeException("访问频率过高");
            }
//            index++;
        }

        return joinPoint.proceed();
    }
}