package com.golocal.apigateway.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.function.Predicate;

/**
 * Global filter for JWT Authentication and Authorization.
 * Validates JWT tokens found in the Authorization header for protected routes.
 * Adds user information (like user ID) as headers for downstream services.
 */
@Component
@Slf4j // Lombok annotation for logging
public class JwtAuthFilter implements GlobalFilter, Ordered {

    @Value("${jwt.secret}")
    private String jwtSecretString; // Inject JWT secret from application properties/env

    private SecretKey signingKey;

    // List of API endpoints that DO NOT require authentication.
    // Requests matching these paths will bypass the JWT validation.
    // Use Ant-style path patterns.
    private final List<String> publicApiEndpoints = List.of(
            "/api/auth/**",       // Registration and Login endpoints
            "/api/products",      // Allow fetching list of products
            "/api/products/*"     // Allow fetching single product by ID (Note: '*' matches one segment)
           // "/api/products/**" // Use this if sub-paths under products are also public
           // Add actuator health endpoints if exposed and needed publicly
           // "/actuator/health"
    );

    // Initialize the signing key after properties are injected
    @PostConstruct
    public void init() {
        if (jwtSecretString == null || jwtSecretString.length() < 32) {
             log.error("FATAL: JWT Secret is not configured or is too short (must be at least 32 bytes)!");
             // Consider throwing an exception or shutting down if the secret is critical and missing/invalid
             // For this example, we'll proceed but log a strong warning.
             // throw new IllegalStateException("JWT Secret not configured or too short!");
            this.signingKey = Keys.hmacShaKeyFor("DefaultWeakSecretKeyNeedsReplacingImmediately".getBytes(StandardCharsets.UTF_8)); // Fallback BAD PRACTICE
        } else {
             byte[] keyBytes = jwtSecretString.getBytes(StandardCharsets.UTF_8);
             this.signingKey = Keys.hmacShaKeyFor(keyBytes);
             log.info("JWT Signing Key initialized successfully.");
        }
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String requestPath = request.getPath().value();
        log.debug("Processing request [{} {}]", request.getMethod(), requestPath);

        // Predicate to check if the current request path matches any public endpoint pattern
        Predicate<String> isPublicApi = path -> publicApiEndpoints.stream()
                .anyMatch(publicPath -> pathMatches(path, publicPath)); // Using helper for basic matching

        // If the path is public, skip authentication/authorization checks
        if (isPublicApi.test(requestPath)) {
            log.debug("Path [{}] is public, skipping JWT validation.", requestPath);
            return chain.filter(exchange);
        }

        log.debug("Path [{}] is secured. Checking Authorization header.", requestPath);
        // Check for Authorization header on secured endpoints
        if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
            log.warn("Missing Authorization header for secured path: {}", requestPath);
            return onError(exchange, "Authorization header is required", HttpStatus.UNAUTHORIZED);
        }

        // Extract the token from the header
        String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.warn("Invalid Authorization header format for path: {}", requestPath);
            return onError(exchange, "Authorization header must be 'Bearer [token]'", HttpStatus.UNAUTHORIZED);
        }

        String token = authHeader.substring(7); // Remove "Bearer " prefix

        // Validate the JWT token
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(this.signingKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            // Extract User ID from the token's subject claim
            String userId = claims.getSubject();
            if (userId == null || userId.isBlank()) {
                log.warn("User ID (subject) missing or empty in JWT for path: {}", requestPath);
                return onError(exchange, "Invalid token: User identifier missing", HttpStatus.UNAUTHORIZED);
            }

            log.debug("JWT validated for User ID: '{}', Path: '{}'", userId, requestPath);

            // Add the validated User ID as a header for downstream services
            ServerHttpRequest modifiedRequest = request.mutate()
                    .header("X-User-Id", userId) // Header name convention
                    // Optionally add other claims like roles if needed by services
                    // .header("X-User-Roles", claims.get("roles", String.class))
                    .build();

            // Proceed with the modified request in the filter chain
            return chain.filter(exchange.mutate().request(modifiedRequest).build());

        } catch (ExpiredJwtException e) {
            log.warn("Expired JWT token for path [{}]: {}", requestPath, e.getMessage());
            return onError(exchange, "Authorization token has expired", HttpStatus.UNAUTHORIZED);
        } catch (UnsupportedJwtException e) {
             log.warn("Unsupported JWT token for path [{}]: {}", requestPath, e.getMessage());
             return onError(exchange, "Unsupported authorization token format", HttpStatus.UNAUTHORIZED);
        } catch (MalformedJwtException e) {
             log.warn("Malformed JWT token for path [{}]: {}", requestPath, e.getMessage());
             return onError(exchange, "Invalid authorization token format", HttpStatus.UNAUTHORIZED);
        } catch (SignatureException e) {
             log.warn("Invalid JWT signature for path [{}]: {}", requestPath, e.getMessage());
             return onError(exchange, "Invalid authorization token signature", HttpStatus.UNAUTHORIZED);
        } catch (IllegalArgumentException e) {
             log.warn("JWT claims string is empty or null for path [{}]: {}", requestPath, e.getMessage());
             return onError(exchange, "Invalid authorization token", HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            // Catch unexpected errors during validation
            log.error("Unexpected error validating JWT for path [{}]: {}", requestPath, e.getMessage(), e);
            return onError(exchange, "Authentication error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Helper method to create an error response.
     * Sets the HTTP status code and completes the response.
     * @param exchange The current server exchange.
     * @param errorMessage The error message to log.
     * @param httpStatus The HTTP status to return.
     * @return A Mono indicating completion.
     */
    private Mono<Void> onError(ServerWebExchange exchange, String errorMessage, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        log.error("Auth Filter Error: {} [Status: {}] for Request: {}", errorMessage, httpStatus, exchange.getRequest().getPath());
        // Optionally write a JSON error body to the response here if needed
        // response.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        // byte[] bytes = ("{\"error\": \"" + errorMessage + "\"}").getBytes(StandardCharsets.UTF_8);
        // DataBuffer buffer = response.bufferFactory().wrap(bytes);
        // return response.writeWith(Mono.just(buffer));
        return response.setComplete(); // Complete the response without a body for simplicity
    }

    /**
     * Specifies the order of this global filter.
     * Lower values have higher priority. We want this filter to run before routing happens.
     * @return The filter order.
     */
    @Override
    public int getOrder() {
        return -100; // Run before standard filters like LoadBalancerClientFilter (order 10100)
    }

    /**
     * Basic path matching helper. Supports '*' as a single segment wildcard and '**' for multiple segments.
     * Note: Spring Cloud Gateway provides more sophisticated Path Predicates (used in config),
     * this is a simplified helper just for the public path check within the filter logic.
     */
    private boolean pathMatches(String requestPath, String pattern) {
         if (pattern.endsWith("/**")) {
             String prefix = pattern.substring(0, pattern.length() - 3);
             return requestPath.startsWith(prefix);
         } else if (pattern.endsWith("/*")) {
             String prefix = pattern.substring(0, pattern.length() - 2);
             // Check if it starts with prefix and has only one more segment
             if (!requestPath.startsWith(prefix)) return false;
             String remaining = requestPath.substring(prefix.length());
             // It matches if remaining is empty (for prefix/) or has no slashes after the first char (if any)
             return remaining.isEmpty() || (remaining.startsWith("/") && remaining.indexOf('/', 1) == -1);
         } else {
             return requestPath.equals(pattern);
         }
    }
}