package com.example.Fundoo_Notes.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger log = LoggerFactory.getLogger(LoggingAspect.class);

    @Around("execution(* com.example.Fundoo_Notes.service..*(..)) || execution(* com.example.Fundoo_Notes.controller..*(..))")
    public Object logAndTrackExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        String method = joinPoint.getSignature().toShortString();
        log.info("Entering {}", method);

        try {
            Object result = joinPoint.proceed();
            long end = System.currentTimeMillis();
            log.info("Completed {} in {} ms", method, (end - start));
            return result;
        } catch (Exception ex) {
            log.error("Failed in {} with error: {}", method, ex.getMessage());
            throw ex;
        }
    }
}
