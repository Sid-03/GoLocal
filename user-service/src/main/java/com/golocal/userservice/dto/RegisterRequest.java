package com.golocal.userservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) representing the request body for user registration.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    /**
     * The desired username for the new user.
     * Must not be blank and should meet length requirements.
     */
    @NotBlank(message = "Username cannot be blank")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    private String username;

    /**
     * The email address for the new user.
     * Must not be blank and must be a valid email format.
     */
    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Email should be valid")
    @Size(max = 100, message = "Email cannot exceed 100 characters")
    private String email;

    /**
     * The desired password for the new user.
     * Must not be blank and should meet minimum length requirements.
     */
    @NotBlank(message = "Password cannot be blank")
    @Size(min = 8, max = 100, message = "Password must be between 8 and 100 characters")
    // Consider adding @Pattern for complexity requirements in production
    private String password;

    // You could add other fields here like firstName, lastName, etc.
    // You could also add a field for requested roles if needed,
    // but typically roles are assigned default or by admins later.
    // private String roles; // e.g., "ROLE_USER" (less common in registration DTO)
}