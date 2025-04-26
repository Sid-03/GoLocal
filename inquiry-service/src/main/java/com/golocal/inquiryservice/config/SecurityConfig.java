package com.golocal.inquiryservice.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfigurationSource; // Inject CorsConfig bean

/**
 * Configures Spring Security for the Inquiry Service.
 * Assumes authentication is handled by the API Gateway (which validates JWT).
 * This configuration primarily ensures that security context (like user ID from headers)
 * can be processed and enables method-level security if needed.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity // Enable annotations like @PreAuthorize
@RequiredArgsConstructor
public class SecurityConfig {

    private final CorsConfigurationSource corsConfigurationSource; // Inject CORS config

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Apply CORS configuration
            .cors(cors -> cors.configurationSource(corsConfigurationSource))

            // Disable CSRF - stateless API
            .csrf(AbstractHttpConfigurer::disable)

            // Authorization Rules:
            // Rely on the Gateway's JWT filter to authenticate.
            // Here, we simply state that any request to inquiry endpoints must have passed
            // the gateway's authentication (which results in an authenticated principal/headers).
            .authorizeHttpRequests(authorize -> authorize
                    .requestMatchers("/api/inquiries/**").authenticated() // Requires authentication
                    .requestMatchers("/actuator/**").permitAll() // Allow health checks
                    .anyRequest().denyAll() // Deny any other unexpected requests
            )

            // Use stateless sessions
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // No specific authentication provider or JWT filter needed here,
        // as we trust the authentication performed by the API Gateway.
        // The gateway adds the 'X-User-Id' header for authenticated requests.

        return http.build();
    }
}