package com.jackdawapi.jackdawapiapidoc.aop;


import com.jackdawapi.jackdawapiapidoc.annotation.JMethod;
import com.jackdawapi.jackdawapiapidoc.annotation.JParams;
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
public class JParamsInterceptor {

    /**
     * 执行拦截
     *
     * @param joinPoint
     * @param jParams
     * @return
     */
    @Around("@annotation(jParams)")
    public Object doInterceptor(ProceedingJoinPoint joinPoint, JParams jParams) throws Throwable {
        Object header = joinPoint;
        Object proceed;
        proceed = joinPoint.proceed();
        return proceed;
    }
}

