package com.att.acceptance.movie_theater.service;

import com.att.acceptance.movie_theater.entity.User;
import com.att.acceptance.movie_theater.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JwtUserDetailsService {

    private final UserRepository userRepository;
    private final UserDetailsServiceImpl userDetailsServiceImpl;

    public JwtUserDetailsService(UserRepository userRepository, UserDetailsServiceImpl userDetailsServiceImpl) {
        this.userRepository = userRepository;
        this.userDetailsServiceImpl = userDetailsServiceImpl;
    }

    /**
     * Load a user by email for JWT validation.
     *
     * @param email The email of the user.
     * @return The UserDetails object containing user information.
     */
    public UserDetails loadUserByEmail(String email) {
        return userDetailsServiceImpl.loadUserByUsername(email);
    }

    /**
     * Validate a user based on their email.
     *
     * @param email The email of the user.
     * @return The User entity.
     */
    public User validateUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() ->
                new UsernameNotFoundException("User not found with email: " + email));
    }
}