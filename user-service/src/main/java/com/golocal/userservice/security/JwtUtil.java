package com.golocal.userservice.security;


import io.jsonwebtoken.ExpiredJwtException;
// import io.jsonwebtoken.MalformedJwtException;
// import io.jsonwebtoken.UnsupportedJwtException;
// import io.jsonwebtoken.security.SignatureException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Utility class for handling JWT (JSON Web Token) operations like
 * generation and validation (although validation primarily happens in the gateway).
 */
@Component
@Slf4j
public class JwtUtil {

    @Value("${jwt.secret}")
    private String jwtSecretString;

    @Value("${jwt.expiration.ms}")
    private long jwtExpirationMs;

    private SecretKey signingKey;

    // Initialize the signing key once properties are loaded
    @PostConstruct
    public void init() {
        if (jwtSecretString == null || jwtSecretString.length() < 32) {
             log.error("FATAL: JWT Secret is not configured or is too short (must be at least 32 bytes)!");
             // Handle this critical configuration error appropriately
             // throw new IllegalStateException("JWT Secret not configured or too short!");
             this.signingKey = Keys.hmacShaKeyFor("FallbackWeakSecretMustReplace12345".getBytes(StandardCharsets.UTF_8)); // BAD PRACTICE
        } else {
             byte[] keyBytes = jwtSecretString.getBytes(StandardCharsets.UTF_8);
             this.signingKey = Keys.hmacShaKeyFor(keyBytes);
             log.info("JWT Util initialized with configured secret.");
        }
    }

    /**
     * Generates a JWT token for the given authenticated user principal.
     *
     * @param authentication The Authentication object from Spring Security context.
     * @return The generated JWT token as a String.
     */
    public String generateToken(Authentication authentication) {
        // Get the principal (user details) from the Authentication object
        UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationMs);

        // Extract roles/authorities
        List<String> roles = userPrincipal.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        log.debug("Generating JWT for user: {}, expiring at: {}", userPrincipal.getUsername(), expiryDate);

        // Build the JWT token
        return Jwts.builder()
                .setSubject(userPrincipal.getUsername()) // Use username as subject
                .claim("roles", roles) // Add roles as a custom claim
                // Add other claims if needed (e.g., user ID, email)
                // .claim("userId", ((User) userPrincipal).getId()) // Assuming UserDetails is your User entity
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(signingKey) // Sign with the configured secret key and algorithm (HS256 default)
                .compact();
    }

    // --- Methods mainly used by Gateway or for local validation (if needed) ---

    /**
     * Extracts the username (subject) from the JWT token.
     * Requires the secret key for verification.
     *
     * @param token The JWT token string.
     * @return The username (subject) from the token.
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extracts the expiration date from the JWT token.
     *
     * @param token The JWT token string.
     * @return The expiration Date object.
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extracts a specific claim from the token using a claims resolver function.
     *
     * @param <T> The type of the claim value.
     * @param token The JWT token string.
     * @param claimsResolver A function to extract the desired claim from the Claims object.
     * @return The extracted claim value.
     */
    public <T> T extractClaim(String token, java.util.function.Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Parses the JWT token and returns all claims.
     * Handles signature verification.
     *
     * @param token The JWT token string.
     * @return The Claims object containing all data from the token payload.
     */
    private Claims extractAllClaims(String token) {
        // Note: This parsing implicitly verifies the signature and expiration
        return Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Checks if the JWT token has expired.
     *
     * @param token The JWT token string.
     * @return true if the token has expired, false otherwise.
     */
    private Boolean isTokenExpired(String token) {
        try {
            return extractExpiration(token).before(new Date());
        } catch (ExpiredJwtException e) {
            return true; // Explicitly handle expired exception case
        }
    }

    /**
     * Validates the JWT token against the UserDetails.
     * Checks if the username matches and if the token is not expired.
     * (Primarily for completeness, validation usually done by gateway)
     *
     * @param token The JWT token string.
     * @param userDetails The UserDetails object for the user.
     * @return true if the token is valid for the user, false otherwise.
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        try {
            final String username = extractUsername(token);
            // Check username match and expiration
            return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
        } catch (Exception e) {
            log.warn("Token validation failed for user {}: {}", userDetails.getUsername(), e.getMessage());
            return false;
        }
    }

     /**
     * Simplified validation just checking signature and expiration.
     * Used primarily by the Gateway filter.
     *
     * @param token The JWT token string.
     * @return true if the token signature is valid and not expired.
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(signingKey).build().parseClaimsJws(token);
            // If parsing succeeds without exception, signature is valid and not expired.
            return true;
        } catch (Exception e) {
            // Log different exception types if needed for debugging
            log.debug("Simple token validation failed: {}", e.getMessage());
            return false;
        }
    }
}