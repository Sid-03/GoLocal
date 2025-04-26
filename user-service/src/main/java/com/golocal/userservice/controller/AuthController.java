package com.golocal.userservice.controller;

import com.golocal.userservice.dto.AuthResponse;
import com.golocal.userservice.dto.LoginRequest;
import com.golocal.userservice.dto.RegisterRequest;
import com.golocal.userservice.entity.User;
import com.golocal.userservice.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST Controller handling authentication requests (login, registration).
 */
@RestController
@RequestMapping("/api/auth") // Base path for authentication endpoints
@RequiredArgsConstructor // Lombok constructor injection
@Slf4j
// No @CrossOrigin here if CORS is handled globally by CorsConfig or Gateway
public class AuthController {

    private final AuthService authService; // Inject the authentication service

    /**
     * Handles user login requests.
     * Authenticates the user and returns a JWT token upon success.
     *
     * @param loginRequest DTO containing login credentials. Must be valid.
     * @return ResponseEntity containing AuthResponse with JWT token (HTTP 200 OK) or error status.
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        log.debug("Received login request for user: {}", loginRequest.getUsername());
        // AuthService handles authentication logic and exceptions
        AuthResponse authResponse = authService.login(loginRequest);
        log.info("Login successful via controller for user: {}", loginRequest.getUsername());
        return ResponseEntity.ok(authResponse);
        // GlobalExceptionHandler will catch AuthenticationException and return 401
    }

    /**
     * Handles new user registration requests.
     * Creates a new user account.
     *
     * @param registerRequest DTO containing registration details. Must be valid.
     * @return ResponseEntity with a success message (HTTP 201 Created) or error status.
     */
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        log.debug("Received registration request for username: {}", registerRequest.getUsername());
        // AuthService handles registration logic, checks, and exceptions
        User registeredUser = authService.register(registerRequest);
        log.info("Registration successful via controller for user: {}", registeredUser.getUsername());

        // Return a success response (HTTP 201 Created)
        // Avoid sending back sensitive info like the password hash
        return ResponseEntity.status(HttpStatus.CREATED)
                             .body("User registered successfully! Username: " + registeredUser.getUsername());
        // GlobalExceptionHandler will catch UserAlreadyExistsException and return 400
    }
}