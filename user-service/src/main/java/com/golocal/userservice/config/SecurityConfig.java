package com.golocal.userservice.config;

import com.golocal.userservice.security.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfigurationSource; // Use the CorsConfig bean

/**
 * Configures Spring Security for the User Service.
 * Handles authentication (verifying user credentials) and defines security rules.
 */
@Configuration
@EnableWebSecurity // Enables Spring Security's web security support
@EnableMethodSecurity // Enables method-level security annotations like @PreAuthorize (optional)
@RequiredArgsConstructor // Lombok constructor injection for final fields
public class SecurityConfig {

    private final UserDetailsServiceImpl userDetailsService; // Custom service to load user details
    private final CorsConfigurationSource corsConfigurationSource; // Inject CORS config (from CorsConfig bean)

    /**
     * Defines the password encoder bean (BCrypt).
     * Used for hashing passwords before storing and verifying during login.
     * @return PasswordEncoder instance.
     */
    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Exposes the AuthenticationManager as a Bean.
     * Needed for explicit authentication calls (e.g., in the AuthService).
     * @param configuration AuthenticationConfiguration from Spring Security.
     * @return AuthenticationManager instance.
     * @throws Exception If configuration fails.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    /**
     * Defines the primary AuthenticationProvider (DaoAuthenticationProvider).
     * Uses the custom UserDetailsService and PasswordEncoder to authenticate users.
     * @return AuthenticationProvider instance.
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService); // Set custom user details service
        authProvider.setPasswordEncoder(passwordEncoder()); // Set password encoder
        return authProvider;
    }

    /**
     * Configures the main security filter chain.
     * Defines CORS, CSRF, session management, and request authorization rules.
     * @param http HttpSecurity object to configure.
     * @return SecurityFilterChain instance.
     * @throws Exception If configuration fails.
     */
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            // Apply CORS configuration defined in CorsConfig
            .cors(cors -> cors.configurationSource(corsConfigurationSource))

            // Disable CSRF protection, as we are using stateless JWT authentication
            .csrf(AbstractHttpConfigurer::disable)

            // Configure authorization rules for HTTP requests
            .authorizeHttpRequests((authorize) -> authorize
                    // Permit all requests to authentication endpoints (/api/auth/**)
                    .requestMatchers("/api/auth/**").permitAll()
                    // Permit actuator health checks (if actuator is included)
                    .requestMatchers("/actuator/**").permitAll()
                    // All other requests require authentication (or adjust as needed)
                    // If the gateway handles auth for other services, this might be permitAll()
                    .anyRequest().authenticated()
            )

            // Configure session management to be stateless
            // JWT is used for auth, no server-side session needed
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            // Register the custom AuthenticationProvider
            .authenticationProvider(authenticationProvider());

        // Note: No JWT request filter is added here because this service's primary
        // role is to ISSUE tokens via the /api/auth/login endpoint.
        // The API Gateway is responsible for VALIDATING tokens for requests
        // destined for other services (like Inquiry Service).

        return http.build();
    }
}