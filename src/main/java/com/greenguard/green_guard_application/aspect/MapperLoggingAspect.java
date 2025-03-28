package com.greenguard.green_guard_application.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@Aspect
public class MapperLoggingAspect {

    private static final Logger log = LoggerFactory.getLogger(MapperLoggingAspect.class);

    @Around("execution(* com.greenguard.green_guard_application.service.mapper.*(..))")
    public Object logConversionObjects(ProceedingJoinPoint pjp) throws Throwable {
        Object[] args = pjp.getArgs();
        log.info("Object to convert: {}", Arrays.stream(args).findFirst().orElse(null));

        Object proceedObject = pjp.proceed();
        log.info("Converted object: {}", proceedObject.toString());

        return proceedObject;
    }
}
