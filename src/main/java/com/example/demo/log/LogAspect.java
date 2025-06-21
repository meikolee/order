package com.example.demo.log;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.lang.reflect.Array;
import java.util.Arrays;

@Aspect
@Component
@Slf4j
public class LogAspect {

    @Pointcut("execution(* com.example..*Service.*(..))")
    public void serviceMethods(){}

    @Around("serviceMethods()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().toShortString();// 获取方法名
        Object[] args = joinPoint.getArgs();

        log.info("【AOP】方法开始执行: {}", methodName);
        log.info("【AOP】方法参数: {}", Arrays.toString(args));

        long start = System.currentTimeMillis();
        Object result;

        try {
            result = joinPoint.proceed();// 执行方法
        } catch (Throwable ex) {
            log.error("【AOP】方法执行异常: {}", ex.getMessage());
            throw ex;
        }

        long duration = System.currentTimeMillis() - start; // 计算方法执行时间
        log.info("【AOP】方法执行完成: {}，耗时: {} ms", methodName, duration);
        log.info("【AOP】返回结果: {}", result);
        return result;
    }

}
