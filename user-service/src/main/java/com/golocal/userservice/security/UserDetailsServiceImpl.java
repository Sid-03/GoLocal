package com.golocal.userservice.security;

import com.golocal.userservice.entity.User;
import com.golocal.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Custom implementation of Spring Security's UserDetailsService.
 * Loads user-specific data (including credentials and authorities) from the database.
 */
@Service
@RequiredArgsConstructor // Lombok constructor injection
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository; // Inject the repository

    /**
     * Locates the user based on the username. In the actual implementation, the search
     * may possibly be case sensitive depending on how the implementor hous the names.
     *
     * @param username the username identifying the user whose data is required.
     * @return a fully populated user record (never <code>null</code>)
     * @throws UsernameNotFoundException if the user could not be found or the user has no
     *                                   GrantedAuthority
     */
    @Override
    @Transactional(readOnly = true) // Use read-only transaction for loading data
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("Attempting to load user by username: {}", username);

        // Find the user in the database by username
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.warn("User not found with username: {}", username);
                    // Throw exception expected by Spring Security
                    return new UsernameNotFoundException("User not found with username: " + username);
                });

        log.debug("User found: {}. Roles: {}", user.getUsername(), user.getRoles());

        // The User entity already implements UserDetails, so we can return it directly.
        // Spring Security will use the getPassword() and getAuthorities() methods from the User entity.
        return user;
    }
}