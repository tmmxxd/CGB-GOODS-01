package com.cy.pj.common.aspect;

import com.cy.pj.common.annotation.RequiredLog;
import com.cy.pj.sys.entity.SysLog;
import com.cy.pj.sys.service.SysLogService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Date;

@Aspect
@Component
@Slf4j
public class SysLogAspect {

    @Pointcut("bean(sysUserServiceImpl)")
    public void logPointCut() {
    }

//    @Around("logPointCut()")
//    public Object around(ProceedingJoinPoint jp)
//            throws Throwable {
//        try {
//            log.info("start:" + System.currentTimeMillis());
//            Object result = jp.proceed();//调用下一个切面方法或目标方法
//            log.info("after:" + System.currentTimeMillis());
//            return result;
//        } catch (Throwable e) {
//            log.error(e.getMessage());
//            throw e;
//        }
//    }

    @Around("logPointCut()")
    public Object around(ProceedingJoinPoint jp) throws Throwable {
        long start = System.currentTimeMillis();
        try {
            log.info("start:" + System.currentTimeMillis());
            Object result = jp.proceed();//调用下一个切面方法或目标方法
            long end = System.currentTimeMillis();
            log.info("after:" + System.currentTimeMillis());
            //记录用户正常行为日志
            saveLog(jp, end - start);
            return result;
        } catch (Throwable e) {
            log.error(e.getMessage());
            throw e;
        }
    }


    @Autowired
    private SysLogService sysLogService;

    /**
     * 将用户行为信息写入到数据库
     */
    private void saveLog(ProceedingJoinPoint jp, long time) throws Exception {
        //1.获取行为日志(借助连接点对象)
        //1.1获取类全名
        Class<?> targetClass = jp.getTarget().getClass();
        String className = targetClass.getName();
        //1.2获取目标方法名
        MethodSignature ms = (MethodSignature) jp.getSignature();
        String method = className + "." + ms.getName();//目标方法
        //1.3获取目标方法实际参数
        String params = Arrays.toString(jp.getArgs());
        //1.4获取操作名称(由此注解RequiredLog指定)
        //1.4.1获取方法对象(Method)--->反射技术
        Method targetMethod = targetClass.getDeclaredMethod(ms.getName(), ms.getParameterTypes());
        //1.4.2获取方法对象上的注解
        RequiredLog requiredLog = targetMethod.getAnnotation(RequiredLog.class);
        //1.4.3获取注解中operation属性
        String operation = "operation";
        if (requiredLog != null) {
            operation = requiredLog.operation();
        }
        //2.封装日志
        SysLog log = new SysLog();
        log.setIp("192.168.175.1");
        log.setUsername("cgb1910");
        log.setOperation(operation);
        log.setMethod(method);//目标方法:类全名+方法名
        log.setParams(params);//目标方法实际参数
        log.setTime(time);
        log.setCreatedTime(new Date());
        //3.写日志
        sysLogService.saveObject(log);
    }
}
