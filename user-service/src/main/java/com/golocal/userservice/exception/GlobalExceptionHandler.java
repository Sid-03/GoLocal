package com.golocal.userservice.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Global exception handler for the User Service.
 * Catches specific exceptions and formats consistent error responses.
 */
@ControllerAdvice // Makes this class handle exceptions across the whole application
@Slf4j // Lombok logger
public class GlobalExceptionHandler {

    /**
     * Handles UserAlreadyExistsException (HTTP 400).
     */
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<Object> handleUserAlreadyExists(UserAlreadyExistsException ex, WebRequest request) {
        log.warn("User registration conflict: {}", ex.getMessage());
        return buildErrorResponse(ex, "Registration Conflict", HttpStatus.BAD_REQUEST, request);
    }

    /**
     * Handles BadCredentialsException (HTTP 401) during login.
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Object> handleBadCredentials(BadCredentialsException ex, WebRequest request) {
        log.warn("Authentication failed: Invalid credentials provided.");
        return buildErrorResponse(ex, "Authentication Failed", HttpStatus.UNAUTHORIZED, request);
    }

     /**
     * Handles UsernameNotFoundException (HTTP 404) - might occur if UserDetailsServiceImpl throws it.
     * Note: DaoAuthenticationProvider often wraps this in BadCredentialsException anyway.
     */
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<Object> handleUsernameNotFound(UsernameNotFoundException ex, WebRequest request) {
         log.warn("Attempt to access non-existent user: {}", ex.getMessage());
         // Typically map this to UNAUTHORIZED or NOT_FOUND depending on context
         return buildErrorResponse(ex, "User Not Found", HttpStatus.NOT_FOUND, request);
    }


    /**
     * Handles validation errors from @Valid annotation on DTOs (HTTP 400).
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException ex, WebRequest request) {
        Map<String, Object> body = createBaseErrorBody(HttpStatus.BAD_REQUEST, "Validation Error", request);

        // Collect specific field errors
        String errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> String.format("'%s': %s", error.getField(), error.getDefaultMessage()))
                .collect(Collectors.joining("; "));
        body.put("details", errors); // Provide detailed validation messages
        log.warn("Validation failed: {}", errors);

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    /**
     * Generic handler for any other unhandled exceptions (HTTP 500).
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGenericException(Exception ex, WebRequest request) {
        log.error("An unexpected error occurred: {}", ex.getMessage(), ex); // Log the full stack trace
        return buildErrorResponse(ex, "Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    // --- Helper Methods ---

    private ResponseEntity<Object> buildErrorResponse(Exception ex, String errorType, HttpStatus status, WebRequest request) {
        Map<String, Object> body = createBaseErrorBody(status, errorType, request);
        body.put("message", ex.getMessage()); // Use the exception's message
        return new ResponseEntity<>(body, status);
    }

    private Map<String, Object> createBaseErrorBody(HttpStatus status, String errorType, WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>(); // Maintain insertion order
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", errorType); // Consistent error type description
        // Extract path from WebRequest (safer than direct manipulation)
        body.put("path", request.getDescription(false).replace("uri=", ""));
        return body;
    }
}