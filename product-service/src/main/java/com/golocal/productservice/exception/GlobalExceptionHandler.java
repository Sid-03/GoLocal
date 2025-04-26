package com.golocal.productservice.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Global exception handler for the Product Service.
 * Catches specific exceptions and formats consistent error responses.
 */
@ControllerAdvice // Handles exceptions across the whole service
@Slf4j // Lombok logger
public class GlobalExceptionHandler {

    /**
     * Handles ProductNotFoundException (HTTP 404).
     */
    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<Object> handleProductNotFound(ProductNotFoundException ex, WebRequest request) {
        log.warn("Resource not found: {}", ex.getMessage());
        return buildErrorResponse(ex, "Not Found", HttpStatus.NOT_FOUND, request);
    }

    /**
     * Handles validation errors from @Valid (HTTP 400) - if you add validation to DTOs later.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException ex, WebRequest request) {
        Map<String, Object> body = createBaseErrorBody(HttpStatus.BAD_REQUEST, "Validation Error", request);

        String errors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> String.format("'%s': %s", error.getField(), error.getDefaultMessage()))
                .collect(Collectors.joining("; "));
        body.put("details", errors);
        log.warn("Validation failed: {}", errors);

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    /**
     * Generic handler for other unhandled exceptions (HTTP 500).
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGenericException(Exception ex, WebRequest request) {
        log.error("An unexpected error occurred processing request [{}]: {}",
                 request.getDescription(false), ex.getMessage(), ex); // Log stack trace
        return buildErrorResponse(ex, "Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    // --- Helper Methods ---

    private ResponseEntity<Object> buildErrorResponse(Exception ex, String errorType, HttpStatus status, WebRequest request) {
        Map<String, Object> body = createBaseErrorBody(status, errorType, request);
        body.put("message", ex.getMessage());
        return new ResponseEntity<>(body, status);
    }

    private Map<String, Object> createBaseErrorBody(HttpStatus status, String errorType, WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", errorType);
         try {
             body.put("path", request.getDescription(false).replace("uri=", ""));
         } catch (Exception e) {
             body.put("path", "unknown"); // Fallback if request description fails
         }
        return body;
    }
}