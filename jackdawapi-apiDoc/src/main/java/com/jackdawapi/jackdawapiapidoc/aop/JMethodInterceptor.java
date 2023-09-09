package com.jackdawapi.jackdawapiapidoc.aop;


import com.jackdawapi.jackdawapiapidoc.annotation.JMethod;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;

/**
 * 权限校验 AOP
 *
 */
@Aspect
@Component
public class JMethodInterceptor {

    /**
     * 执行拦截
     *
     * @param joinPoint
     * @param jMethod
     * @return
     */
    @Around("@annotation(jMethod)")
    public Object doInterceptor(ProceedingJoinPoint joinPoint, JMethod jMethod) throws Throwable {
        Object header = joinPoint;
        Object proceed;
        proceed = joinPoint.proceed();
        return proceed;
    }
}

