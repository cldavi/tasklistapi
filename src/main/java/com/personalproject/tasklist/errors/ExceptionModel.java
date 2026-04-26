package com.personalproject.tasklist.errors;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatusCode;

public record ExceptionModel(
    HttpStatusCode statusCode,
    String errorMessage,
    LocalDateTime timestamp
) {
}
