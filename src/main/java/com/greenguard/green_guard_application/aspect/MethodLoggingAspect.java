package com.greenguard.green_guard_application.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


@Component
@Aspect
public class MethodLoggingAspect {
    private static final Logger log = LoggerFactory.getLogger(MethodLoggingAspect.class);

    @Before("@annotation(com.greenguard.green_guard_application.aspect.annotation.EnableMethodCallLog)")
    void logMethodCall(JoinPoint joinPoint) {
        log.info("Event: method call, Method: {}, Arguments: {}, Called by: {}, Called on: {}",
                joinPoint.getSignature().toShortString(),
                joinPoint.getArgs(),
                joinPoint.getThis(),
                joinPoint.getTarget());
    }

    @AfterReturning(value = "@annotation(com.greenguard.green_guard_application.aspect.annotation.EnableMethodReturnValueLog",
            returning = "returnedObject")
    void logReturnValue(JoinPoint joinPoint, Object returnedObject) {
        log.info("Event: method returned value, Returned value instance: {}, Returned value string representation: {}",
                returnedObject.getClass().toString(),
                returnedObject.toString());
    }

    @AfterThrowing(value = "@annotation(com.greenguard.green_guard_application.aspect.annotation.EnableExceptionThrowingLog", throwing = "exception")
    void logExceptionThrown(JoinPoint joinPoint, Exception exception) {
        log.warn("Event: exception thrown, Thrown by method: {}, Exception cause: {}, Exception message: {}",
                joinPoint.getSignature().toShortString(),
                exception.getCause(),
                exception.getMessage());
    }
}
