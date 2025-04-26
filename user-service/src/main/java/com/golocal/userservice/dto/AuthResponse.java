package com.golocal.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) representing the response after successful authentication.
 * Contains the JWT access token.
 */
@Data // Lombok: Generates getters, setters, toString, equals, hashCode
@NoArgsConstructor // Lombok: Generates no-args constructor
@AllArgsConstructor // Lombok: Generates all-args constructor
public class AuthResponse {

    /**
     * The JSON Web Token (JWT) issued upon successful authentication.
     * The client should store this token and send it in the Authorization header
     * for subsequent requests to protected endpoints.
     */
    private String token;

    // Optionally, you could include basic user info here as well,
    // but often the token itself contains necessary claims.
    // private String username;
    // private List<String> roles;
}