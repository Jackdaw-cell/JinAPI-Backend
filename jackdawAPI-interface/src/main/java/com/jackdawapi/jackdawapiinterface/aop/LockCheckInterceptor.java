package com.jackdawapi.jackdawapiinterface.aop;

import com.jackdawapi.jackdawapiinterface.annotation.LockCheck;
import com.jackdawapi.jackdawapiinterface.util.ReentrantLock.LockByKey;
import com.jackdawapi.jackdawapiinterface.util.ReentrantLock.LockByKeyImpl;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * 锁事务
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
@Aspect
@Component
public class LockCheckInterceptor {

    private static final LockByKey<String> lockByKey = new LockByKeyImpl<>();
    /**
     * 执行拦截
     *
     * @param joinPoint
     * @param lockCheck
     * @return
     */
    @Around("@annotation(lockCheck)")
    public Object doInterceptor(ProceedingJoinPoint joinPoint, LockCheck lockCheck) throws Throwable {
        //获取请求头参数 accessKey，对accessKey加锁
        Object header = joinPoint.getArgs()[1];
        LinkedHashMap<String,String> linkedHashMap = (LinkedHashMap<String,String>) header;
        String accesskey = linkedHashMap.get("accesskey");
        lockByKey.lock(accesskey);
        System.out.println("用户："+accesskey+"占用锁");
        Object proceed;
        try{
            // 放行，执行原方法
            proceed = joinPoint.proceed();
        }catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            lockByKey.unlock(accesskey);
        }
        System.out.println("用户："+accesskey+"释放锁");
        return proceed;
    }
}

