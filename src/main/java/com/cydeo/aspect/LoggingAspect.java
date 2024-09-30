package com.cydeo.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.*;
import org.keycloak.adapters.springsecurity.account.SimpleKeycloakAccount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    //Logger logger = LoggerFactory.getLogger(LoggingAspect.class); --> used annotation slf4j

    @Pointcut("execution(* com.cydeo.controller.ProjectController.*(..)) || execution(* com.cydeo.controller.TaskController.*(..))")
    public void anyProjectOrTaskController() {}

    @Before(("anyProjectOrTaskController()"))
    public void anyProjectOrTaskControllerBeforeAdvice(JoinPoint jointPoint){
        log.info("Before -> Method: {}, User: {}", jointPoint.getSignature().toShortString(), getUsername());

    }

    private String getUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        SimpleKeycloakAccount userDetails = (SimpleKeycloakAccount) authentication.getDetails();
        return userDetails.getKeycloakSecurityContext().getToken().getPreferredUsername();
    }

    @AfterReturning(pointcut = "anyProjectOrTaskController()", returning = "results")
    public void afterReturningAnyProjectOrTaskControllerAdvice(JoinPoint joinPoint, Object results){
        log.info("After -> Method: {}, User: {}, Results: {}"
                , joinPoint.getSignature().toShortString()
                , getUsername()
                , results.toString());
    }
    @AfterThrowing(pointcut = "anyProjectOrTaskController()", throwing = "exception")
    public void afterThrowingAnyProjectOrTaskControllerAdvice(JoinPoint joinPoint, Exception exception){
        log.info("After -> Method: {}, User: {}, Exception: {}"
                , joinPoint.getSignature().toShortString()
                , getUsername()
                , exception.toString());
    }

}
