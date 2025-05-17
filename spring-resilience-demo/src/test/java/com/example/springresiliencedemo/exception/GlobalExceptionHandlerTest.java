package com.example.springresiliencedemo.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void handleClientErrorException_WithNotFoundStatus_ShouldReturnNotFoundStatus() {
        // Given
        HttpClientErrorException ex = new HttpClientErrorException(HttpStatus.NOT_FOUND, "User not found");

        // When
        ResponseEntity<Map<String, Object>> response = handler.handleClientErrorException(ex);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody())
                .containsEntry("error", "Resource not found")
                .containsEntry("message", "User not found")
                .containsKey("timestamp");
    }

    @Test
    void handleClientErrorException_WithBadRequestStatus_ShouldReturnBadRequestStatus() {
        // Given
        HttpClientErrorException ex = new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Invalid request");

        // When
        ResponseEntity<Map<String, Object>> response = handler.handleClientErrorException(ex);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody())
                .containsEntry("error", "Client error")
                .containsEntry("message", "Invalid request")
                .containsKey("timestamp");
    }

    @Test
    void handleServerErrorException_ShouldReturnServerErrorStatus() {
        // Given
        HttpServerErrorException ex = new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "Server error");

        // When
        ResponseEntity<Map<String, Object>> response = handler.handleServerErrorException(ex);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody())
                .containsEntry("error", "Server error")
                .containsEntry("message", "Server error")
                .containsKey("timestamp");
    }

    @Test
    void handleResourceAccessException_ShouldReturnServiceUnavailableStatus() {
        // Given
        ResourceAccessException ex = new ResourceAccessException("Service unavailable");

        // When
        ResponseEntity<Map<String, Object>> response = handler.handleResourceAccessException(ex);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.SERVICE_UNAVAILABLE);
        assertThat(response.getBody())
                .containsEntry("error", "Service unavailable")
                .containsEntry("message", "Service unavailable")
                .containsKey("timestamp");
    }

    @Test
    void handleGenericException_ShouldReturnInternalServerErrorStatus() {
        // Given
        Exception ex = new RuntimeException("Unexpected error");

        // When
        ResponseEntity<Map<String, Object>> response = handler.handleGenericException(ex);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody())
                .containsEntry("error", "Internal server error")
                .containsEntry("message", "Unexpected error")
                .containsKey("timestamp");
    }
} 