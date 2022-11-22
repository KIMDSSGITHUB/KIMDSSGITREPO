package com.dss.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
public class ControllerAdvisor extends ResponseEntityExceptionHandler {
    private static final String TIME_STAMP = "timestamp";
    private static final String MESSAGE = "message";

    @ExceptionHandler({ReviewNotFoundException.class})
    public ResponseEntity<Object> handleNotFoundException(Exception ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put(TIME_STAMP, LocalDateTime.now());
        body.put(MESSAGE, ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }
}
