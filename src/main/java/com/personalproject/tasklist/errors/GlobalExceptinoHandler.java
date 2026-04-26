package com.personalproject.tasklist.errors;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptinoHandler {
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionModel> charLimit(Exception exception) {
        ExceptionModel errorModel = new ExceptionModel(HttpStatusCode.valueOf(400), exception.getMessage(), LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorModel);
    }
}
