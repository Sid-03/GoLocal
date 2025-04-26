package com.golocal.userservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configures Cross-Origin Resource Sharing (CORS) for the User Service.
 * Necessary if requests originate directly from the frontend to this service,
 * although typically requests go through the API Gateway which handles CORS globally.
 * Having it here provides flexibility or allows direct access during development.
 */
@Configuration
public class CorsConfig {

    // Define allowed origins - should align with the gateway's allowed origins
    // In production, list specific frontend domain(s).
    private final String[] allowedOrigins = {"http://localhost:3000", "http://localhost:9000"}; // Frontend dev and Gateway ports

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(@NonNull CorsRegistry registry) {
                registry.addMapping("/api/**") // Apply CORS to /api/** paths within this service
                        .allowedOrigins(allowedOrigins) // Origins allowed to connect
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD") // Allowed HTTP methods
                        .allowedHeaders("*") // Allow all headers (consider restricting in production)
                        .allowCredentials(true) // Allow cookies/auth headers
                        .maxAge(3600); // Cache preflight response for 1 hour
            }
        };
    }
}