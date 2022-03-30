package com.small.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * @author wesson
 * Create at 2022/3/29 20:21 周二
 */
@Slf4j
@Component
@Aspect
public class Myaspect {

    @Pointcut("execution(public * com.small.controller.*.*(..))")
    public void myPointcut() {
        //
    }

    @Around("myPointcut()")
    public Object doAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        log.info("执行切面...");
        Object proceed = proceedingJoinPoint.proceed();
        log.info("切面结束...");

        return proceed;
    }
}
