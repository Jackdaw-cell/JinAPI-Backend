package com.jackdawapi.jackdawapiinterface.aop;

import com.jackdawapi.jackdawapiinterface.annotation.SourceCheck;
import com.jackdawapi.jackdawapiinterface.util.ReentrantLock.LockByKey;
import com.jackdawapi.jackdawapiinterface.util.ReentrantLock.LockByKeyImpl;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;

/**
 * 权限校验 AOP
 *
 */
@Aspect
@Component
public class SourceCheckInterceptor {

    private static final LockByKey<String> lockByKey = new LockByKeyImpl<>();
    /**
     * 执行拦截
     *
     * @param joinPoint
     * @param sourceCheck
     * @return
     */
    @Around("@annotation(sourceCheck)")
    public Object doInterceptor(ProceedingJoinPoint joinPoint, SourceCheck sourceCheck) throws Throwable {
        Object header = joinPoint.getArgs()[1];
        LinkedHashMap<String,String> linkedHashMap = (LinkedHashMap<String,String>) header;
        String source = linkedHashMap.get("source"); // 通过权限校验，放行，执行原方法
        Object proceed;
        if (source.equals("jackdawApi")) {
            proceed = joinPoint.proceed();
        }else {
            return "你必须通过平台访问服务";
        }
        return proceed;
    }
}

