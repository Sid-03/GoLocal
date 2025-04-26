package com.golocal.inquiryservice.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;


import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Global exception handler for the Inquiry Service.
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    // --- Specific Business Logic Exceptions (Add as needed) ---
    /* Example:
    @ExceptionHandler(InquiryNotFoundException.class)
    public ResponseEntity<Object> handleInquiryNotFound(InquiryNotFoundException ex, WebRequest request) {
        log.warn("Resource not found: {}", ex.getMessage());
        return buildErrorResponse(ex, "Not Found", HttpStatus.NOT_FOUND, request);
    }
    */

    /**
     * Handles validation errors from @Valid annotation on DTOs (HTTP 400).
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
     * Handles errors when request parameters (path variables, query params) have the wrong type.
     */
     @ExceptionHandler(MethodArgumentTypeMismatchException.class)
     public ResponseEntity<Object> handleTypeMismatch(MethodArgumentTypeMismatchException ex, WebRequest request) {
         String error = String.format("Parameter '%s' should be of type '%s'", ex.getName(), ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "unknown");
         Map<String, Object> body = createBaseErrorBody(HttpStatus.BAD_REQUEST, "Invalid Parameter Type", request);
         body.put("message", error);
         log.warn("Parameter type mismatch: {}", error);
         return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
     }


    /**
     * Handles Spring Security Access Denied errors (HTTP 403).
     * Might occur if method-level security denies access.
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> handleAccessDenied(AccessDeniedException ex, WebRequest request) {
        log.warn("Access Denied: {}", ex.getMessage());
        return buildErrorResponse(ex, "Forbidden", HttpStatus.FORBIDDEN, request);
    }


    /**
     * Generic handler for other unhandled exceptions (HTTP 500).
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGenericException(Exception ex, WebRequest request) {
        log.error("An unexpected error occurred processing request [{}]: {}",
                 request.getDescription(false), ex.getMessage(), ex);
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
             body.put("path", "unknown");
         }
        return body;
    }
}