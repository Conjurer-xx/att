package com.att.acceptance.movie_theater.controller;

import com.att.acceptance.movie_theater.entity.User;
import com.att.acceptance.movie_theater.security.JwtTokenProvider;
import com.att.acceptance.movie_theater.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@Validated
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;

    public AuthenticationController(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
    }

    /**
     * Authenticate a user and generate a JWT token.
     *
     * @param loginRequest The login request containing email and password.
     * @return A response containing the JWT token.
     */
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtTokenProvider.generateToken(authentication);

        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        return ResponseEntity.ok(response);
    }

    /**
     * Register a new user (Customer by default).
     *
     * @param user The user to register.
     * @return The registered user.
     */
    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@Valid @RequestBody User user) {
        User registeredUser = userService.registerUser(user, com.att.acceptance.movie_theater.entity.RoleEnum.ROLE_CUSTOMER);
        return ResponseEntity.ok(registeredUser);
    }

    /**
     * DTO for login request.
     */
    public static class LoginRequest {

        @jakarta.validation.constraints.Email
        private String email;

        @jakarta.validation.constraints.NotBlank
        private String password;

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}