package com.greenguard.green_guard_application.restController.exception;

import com.greenguard.green_guard_application.aspect.annotation.EnableMethodCallLog;
import com.greenguard.green_guard_application.service.exception.SensorNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(exception = SensorNotFoundException.class)
    @EnableMethodCallLog
    protected ResponseEntity<String> notFoundExceptionHandler(Exception exception) {
        return new ResponseEntity<>(exception.getMessage(),
                                    HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(exception = Exception.class)
    @EnableMethodCallLog
    protected ResponseEntity<Map<String, String>> defaultExceptionHandler(WebRequest request, Exception exception) {
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("server message", "Iternal Server Error");
        responseBody.put("exception message", exception.getMessage());
        responseBody.put("request context path", request.getContextPath());

        return new ResponseEntity<>(responseBody,
                                    HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
