package com.rental.agency.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class GeneralInterceptorAspect {

    @Pointcut("execution(* com.rental.agency.controller.*.*(..))")
    private void controllerPointcut() {}

    @Pointcut("within(com.rental.agency.service.*)")
    private void servicePointcut() {}

    @Before("controllerPointcut()")
    public void beforeMethod(JoinPoint joinPoint) {
        log.info("Before method invoked:: {}", joinPoint.getSignature());
    }

    @AfterReturning(value = "execution(* com.rental.agency.controller.*.*(..))",
    returning = "response")
    public void afterMethod(JoinPoint joinPoint, ResponseEntity<?> response) {
        log.info("After method invoked:: {}", response.getBody());
    }

    @AfterThrowing(value = "execution(* com.rental.agency.controller.*.*(..))",
            throwing = "e")
    public void afterClass(JoinPoint joinPoint, Exception e) {
        log.info("After class invoked: {}", e.getMessage());
    }

    @Before("servicePointcut()")
    public void beforeClass(JoinPoint joinPoint) {
        log.info("Before class invoked: {}", joinPoint.getSignature());
    }

    @After("servicePointcut()")
    public void afterMethod(JoinPoint joinPoint) {
        log.info("After method invoked: {}", joinPoint.getSignature());
    }
}
