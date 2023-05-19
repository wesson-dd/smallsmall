package com.small.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

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
        final StopWatch watch = new StopWatch();
        watch.start();
        Object proceed = proceedingJoinPoint.proceed();
        watch.stop();
        log.info("耗时={}ms", watch.getLastTaskTimeMillis());

        return proceed;
    }
}
