package com.golocal.userservice.service;

import com.golocal.userservice.dto.AuthResponse;
import com.golocal.userservice.dto.LoginRequest;
import com.golocal.userservice.dto.RegisterRequest;
import com.golocal.userservice.entity.User;
import com.golocal.userservice.exception.UserAlreadyExistsException;
import com.golocal.userservice.repository.UserRepository;
import com.golocal.userservice.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service class handling authentication (login) and registration logic.
 */
@Service
@RequiredArgsConstructor // Lombok constructor injection
@Slf4j
public class AuthService {

    private final AuthenticationManager authenticationManager; // For authenticating users
    private final UserRepository userRepository;             // For accessing user data
    private final PasswordEncoder passwordEncoder;           // For encoding passwords
    private final JwtUtil jwtUtil;                         // For generating JWT tokens

    /**
     * Authenticates a user based on provided credentials and generates a JWT token upon success.
     *
     * @param loginRequest DTO containing username and password.
     * @return AuthResponse DTO containing the generated JWT token.
     * @throws org.springframework.security.core.AuthenticationException if authentication fails.
     */
    @Transactional(readOnly = true) // Usually read-only, authentication manager handles state changes
    public AuthResponse login(LoginRequest loginRequest) {
        log.info("Attempting login for user: {}", loginRequest.getUsername());

        // Attempt authentication using Spring Security's AuthenticationManager
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        // If authentication is successful, set the authentication object in the SecurityContext
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Generate JWT token for the authenticated user
        String jwt = jwtUtil.generateToken(authentication);
        log.info("Login successful for user: {}. Token generated.", loginRequest.getUsername());

        // Return the token in the response DTO
        return new AuthResponse(jwt);
    }

    /**
     * Registers a new user in the system.
     * Checks for existing username/email and encodes the password.
     *
     * @param registerRequest DTO containing username, email, and password.
     * @return The newly created User entity (without password exposed ideally, or a confirmation DTO).
     * @throws UserAlreadyExistsException if username or email is already taken.
     */
    @Transactional // This operation modifies the database
    public User register(RegisterRequest registerRequest) {
        log.info("Attempting registration for username: {}", registerRequest.getUsername());

        // Check if username already exists
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            log.warn("Registration failed: Username '{}' already exists.", registerRequest.getUsername());
            throw new UserAlreadyExistsException("Username is already taken: " + registerRequest.getUsername());
        }

        // Check if email already exists
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
             log.warn("Registration failed: Email '{}' already exists.", registerRequest.getEmail());
            throw new UserAlreadyExistsException("Email is already registered: " + registerRequest.getEmail());
        }

        // Create a new User entity
        User newUser = new User();
        newUser.setUsername(registerRequest.getUsername());
        newUser.setEmail(registerRequest.getEmail());
        // Encode the password before saving
        newUser.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        // Set default roles (can be customized if needed)
        newUser.setRoles("ROLE_USER"); // Default role

        // Save the new user to the database
        User savedUser = userRepository.save(newUser);
        log.info("Registration successful for user: {}. User ID: {}", savedUser.getUsername(), savedUser.getId());

        // Avoid returning the entity with the encoded password directly in a real API response.
        // Return a confirmation message, user ID, or a dedicated UserResponseDTO.
        // For simplicity in this example, we return the saved User object.
        return savedUser;
    }
}