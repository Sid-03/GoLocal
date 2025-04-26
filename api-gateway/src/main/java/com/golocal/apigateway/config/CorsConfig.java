package com.golocal.apigateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter; // Reactive version for Gateway
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.util.pattern.PathPatternParser;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Configures Cross-Origin Resource Sharing (CORS) for the API Gateway.
 * This allows the frontend application (running on a different origin)
 * to make requests to the gateway.
 */
@Configuration
public class CorsConfig {

    // Define the origin(s) allowed to access the gateway.
    // Use specific origins in production instead of "*".
    // Should match the origin of your frontend app.
    private final List<String> allowedOrigins = Collections.singletonList("http://localhost:3000");

    @Bean
    public CorsWebFilter corsWebFilter() {
        CorsConfiguration corsConfig = new CorsConfiguration();

        // Origins allowed to make requests
        corsConfig.setAllowedOrigins(allowedOrigins);

        // HTTP methods allowed (GET, POST, etc.)
        corsConfig.setAllowedMethods(Arrays.asList(
                HttpMethod.GET.name(),
                HttpMethod.POST.name(),
                HttpMethod.PUT.name(),
                HttpMethod.DELETE.name(),
                HttpMethod.OPTIONS.name(), // Important for preflight requests
                HttpMethod.HEAD.name()
        ));

        // Headers allowed in the request
        // Using "*" is convenient for development but consider listing specific headers
        // like "Authorization", "Content-Type", "X-Requested-With" in production.
        corsConfig.addAllowedHeader("*");

        // Whether the browser should include credentials (like cookies or auth headers)
        // in CORS requests. Set to true if using token-based auth via headers or cookies.
        corsConfig.setAllowCredentials(true);

        // How long the results of a preflight request (OPTIONS) can be cached by the browser.
        corsConfig.setMaxAge(3600L); // 1 hour

        // Apply this CORS configuration to specific paths
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource(new PathPatternParser());
        // Apply CORS rules to all API endpoints routed by the gateway
        source.registerCorsConfiguration("/api/**", corsConfig);

        return new CorsWebFilter(source);
    }
}