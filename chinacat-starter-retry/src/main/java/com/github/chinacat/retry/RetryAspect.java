package com.github.chinacat.retry;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * @author s.c.gao
 */
@Aspect
public class RetryAspect {

    private static final Logger log = LoggerFactory.getLogger(RetryAspect.class);

    @Pointcut("@annotation(com.github.chinacat.retry.Retry)")
    public void retryPoint() {
    }

    @Around("retryPoint()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        Retry retry = method.getAnnotation(Retry.class);
        int times = retry.times();
        Throwable throwable = null;
        do {
            try {
                return joinPoint.proceed(joinPoint.getArgs());
            } catch (RetryTriggerException e) {
                throwable = e;
                log.info("Method " + method.getName() + " is retrying, and retry times left " + times, e);
            } catch (Throwable t) {
                throwable = t;
                log.error("Method " + method.getName() + " throws an unexpected exception");
                // 非RetryTriggerException异常，都不进行重试，避免逻辑混乱
                break;
            }
        } while (--times > 0);
        throw throwable;
    }
}