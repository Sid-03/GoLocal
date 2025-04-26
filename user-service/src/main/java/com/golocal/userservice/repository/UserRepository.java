package com.golocal.userservice.repository;

import com.golocal.userservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data JPA repository for the User entity.
 * Provides standard CRUD operations and custom query methods.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> { // Entity is User, ID type is Long

    /**
     * Finds a user by their username.
     * Used by UserDetailsServiceImpl for authentication.
     *
     * @param username The username to search for.
     * @return An Optional containing the User if found, otherwise empty.
     */
    Optional<User> findByUsername(String username);

    /**
     * Checks if a user exists with the given username.
     * Used during registration to prevent duplicates.
     *
     * @param username The username to check.
     * @return true if a user with this username exists, false otherwise.
     */
    Boolean existsByUsername(String username);

    /**
     * Checks if a user exists with the given email address.
     * Used during registration to prevent duplicates.
     *
     * @param email The email address to check.
     * @return true if a user with this email exists, false otherwise.
     */
    Boolean existsByEmail(String email);

    // Add other custom queries if needed, e.g., findByEmail, findAllByRoleContaining, etc.
}