package com.oriseq.config.permission;

import com.oriseq.config.PermissionTool;
import com.oriseq.controller.exception.ErrorEnum;
import com.oriseq.controller.exception.NoPermissionException;
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

/**
 *
 */

//@EnableAspectJAutoProxy
@Aspect
@Component
public class PermissionAspect {


    @Autowired
    private PermissionTool permissionTool;

    @Pointcut("@annotation(com.oriseq.config.permission.RequiredPermission)")
    public void requiredPermissionPointcut() {
    }

    @Around("requiredPermissionPointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        // 获取方法上的注解
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        RequiredPermission annotation = method.getAnnotation(RequiredPermission.class);
        String permission = annotation.value();

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        // 获取当前用户拥有的权限
        // 调用 PermissionService 进行权限检查
        boolean b = permissionTool.hasPermission(permission, request);
        // 权限校验
        if (!b) {
            // 没有权限，抛出异常或返回错误信息
            throw new NoPermissionException(ErrorEnum.NO_PERMISSION.getErrorCode(), ErrorEnum.NO_PERMISSION.getErrorMsg());
        }

        // 执行目标方法
        return joinPoint.proceed();
    }

}