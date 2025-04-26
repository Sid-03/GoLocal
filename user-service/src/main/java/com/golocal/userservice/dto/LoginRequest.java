package com.golocal.userservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) representing the request body for user login.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

    /**
     * The username provided by the user attempting to log in.
     * Cannot be blank.
     */
    @NotBlank(message = "Username cannot be blank")
    private String username;

    /**
     * The password provided by the user attempting to log in.
     * Cannot be blank.
     */
    @NotBlank(message = "Password cannot be blank")
    private String password;
}