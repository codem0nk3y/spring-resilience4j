package com.example.springresiliencedemo.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<Map<String, Object>> handleClientErrorException(HttpClientErrorException ex) {
        log.error("Client error occurred: {}", ex.getMessage());
        String message = extractMessage(ex.getMessage());
        if (ex.getStatusCode() == HttpStatus.NOT_FOUND) {
            return createErrorResponse(HttpStatus.NOT_FOUND, "Resource not found", message);
        }
        return createErrorResponse(HttpStatus.valueOf(ex.getStatusCode().value()), "Client error", message);
    }

    @ExceptionHandler(HttpServerErrorException.class)
    public ResponseEntity<Map<String, Object>> handleServerErrorException(HttpServerErrorException ex) {
        log.error("Server error occurred: {}", ex.getMessage());
        String message = extractMessage(ex.getMessage());
        return createErrorResponse(HttpStatus.valueOf(ex.getStatusCode().value()), "Server error", message);
    }

    @ExceptionHandler(ResourceAccessException.class)
    public ResponseEntity<Map<String, Object>> handleResourceAccessException(ResourceAccessException ex) {
        log.error("Resource access error: {}", ex.getMessage());
        return createErrorResponse(HttpStatus.SERVICE_UNAVAILABLE, "Service unavailable", ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        log.error("Unexpected error occurred: {}", ex.getMessage(), ex);
        return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error", ex.getMessage());
    }

    private ResponseEntity<Map<String, Object>> createErrorResponse(HttpStatus status, String error, String message) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", error);
        body.put("message", message);
        return new ResponseEntity<>(body, status);
    }

    private String extractMessage(String fullMessage) {
        if (fullMessage == null) {
            return "Unknown error";
        }
        // Remove status code prefix if present (e.g., "404 User not found" -> "User not found")
        return fullMessage.replaceFirst("^\\d+\\s+", "");
    }
} 