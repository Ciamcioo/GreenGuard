package com.greenguard.green_guard_application.aspect.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@EnableMethodCallLog
@EnableMethodReturnValueLog
public @interface EnableMethodLog {

}
