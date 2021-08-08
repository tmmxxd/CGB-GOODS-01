package com.cy.pj.common.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 缓存Aspect
 */
@Aspect
@Component
public class SysCacheAspect {

    /**
     * HashMap 线程不安全
     * HashTable 线程安全,但性能很差(商场购物,只允许一个人进去)
     * ConcurrentHashMap 线程安全,JDK7进行了分段加锁(类似于商场有五层,每层一个用户)
     * ConcurrentHashMap 线程安全,JDK8进行了单元格加锁+CAS算法(类似锁了商场的试衣间)
     */
    private Map<String, Object> cacheMap = new ConcurrentHashMap<>();

    @Pointcut("@annotation(com.cy.pj.common.annotation.RequiredCache)")
    public void doCache() {
    }

    @Pointcut("@annotation(com.cy.pj.common.annotation.ClearCache)")
    public void clearCache() {
    }

    @Around("doCache()")
    public Object around(ProceedingJoinPoint jp) throws Throwable {
        System.out.println("Get data from cache");
        Object cacheData = cacheMap.get("dept.findObjects");
        if (cacheData != null) {
            return cacheData;
        }
        Object result = jp.proceed();
        cacheMap.put("dept.findObjects", result);
        System.out.println("Put data to cache");
        return result;
    }

    /**
     * 当切入点对应的方法执行ok以后会执行如下方法,进行cache清除操作
     */
    @AfterReturning("clearCache()")
    public void clear() {
        System.out.println("====clear cache===");
        cacheMap.clear();
    }
}
