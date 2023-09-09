package com.jackdawapi.jackdawapiapidoc.aop;


import com.jackdawapi.jackdawapiapidoc.annotation.JMethod;
import com.jackdawapi.jackdawapiapidoc.annotation.JService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;

/**
 * 锁事务
 *
 */
@Aspect
@Component
@Slf4j
public class JServiceInterceptor {

    /**
     * 执行拦截
     *
     * @param joinPoint
     * @param jService
     * @return
     */
    @Around("@annotation(jService)")
    public Object doInterceptor(ProceedingJoinPoint joinPoint, JService jService) throws Throwable {
        Object header = joinPoint;
        Object proceed;
        // 放行，执行原方法
        proceed = joinPoint.proceed();
        return proceed;
    }
}

