package com.cydeo.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class PerformanceAspect {

    @Pointcut("@annotation(com.cydeo.annotation.ExecutionTime)")
    public void executionTimePointcut() {}

    @Around("executionTimePointcut()")
    public Object aroundExecutionTimeAdvice(ProceedingJoinPoint proceedingJoinPoint){
        long beforeTime = System.currentTimeMillis();

        Object result = null;
        log.info("Execution starts: ");
        try{
            result = proceedingJoinPoint.proceed();
        }catch (Throwable throwable){
            throwable.printStackTrace();
        }

        long afterTime = System.currentTimeMillis();
        log.info("Execution time: " + (afterTime - beforeTime) + "ms" + "Method: {} " + proceedingJoinPoint.getSignature().toShortString());

        return result;

    }
}
