package com.golocal.userservice.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.stream.Collectors;

/**
 * JPA Entity representing a User in the application.
 * Implements Spring Security's UserDetails interface for integration.
 */
@Data // Lombok: getters, setters, toString, equals, hashCode
@NoArgsConstructor // Lombok: No-args constructor needed by JPA
@AllArgsConstructor // Lombok: All-args constructor
@Entity
@Table(name = "users", // Table name in the database
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "username"), // Ensure username is unique
                @UniqueConstraint(columnNames = "email")      // Ensure email is unique
        })
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-incrementing ID
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false, length = 100) // Store hashed password
    private String password;

    /**
     * Stores user roles as a comma-separated string.
     * Example: "ROLE_USER,ROLE_ADMIN"
     * Alternatively, use @ElementCollection for a Set<String> or a separate Role entity with @ManyToMany.
     * Comma-separated is simpler for this example but less flexible.
     */
    @Column(nullable = false)
    private String roles = "ROLE_USER"; // Default role

    // --- UserDetails Implementation ---

    /**
     * Converts the comma-separated roles string into a collection of GrantedAuthority objects.
     * Required by Spring Security.
     * @return Collection of user's authorities (roles).
     */
    @Override
    @Transient // Mark as transient so JPA doesn't try to map it to a DB column
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (this.roles == null || this.roles.isBlank()) {
            return new HashSet<>(); // Return empty set if no roles defined
        }
        // Split the roles string by comma, trim whitespace, and map to SimpleGrantedAuthority
        return Arrays.stream(this.roles.split(","))
                     .map(String::trim) // Trim whitespace from each role
                     .filter(role -> !role.isEmpty()) // Filter out empty strings
                     .map(SimpleGrantedAuthority::new) // Create authority object
                     .collect(Collectors.toSet()); // Collect into a Set
    }

    // Other UserDetails methods - returning true assuming accounts are always enabled/valid for simplicity.
    // Implement more complex logic (e.g., is_enabled flags) if needed.

    @Override
    public boolean isAccountNonExpired() {
        return true; // Account never expires
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Account is never locked
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Credentials never expire
    }

    @Override
    public boolean isEnabled() {
        return true; // Account is always enabled
    }
}