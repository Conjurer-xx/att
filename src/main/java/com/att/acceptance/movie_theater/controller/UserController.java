package com.att.acceptance.movie_theater.controller;

import com.att.acceptance.movie_theater.entity.User;
import com.att.acceptance.movie_theater.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import jakarta.validation.Valid;

/**
 * Controller for managing users.
 * Only accessible by admins.
 */
@RestController
@RequestMapping("/api/users")
@Validated
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Fetch all users with pagination (Admin only).
     * @param pageable Pagination details
     * @return Paginated list of users
     */
    @PreAuthorize("hasRole('ADMIN_ROLE')")
    @GetMapping
    public ResponseEntity<Page<User>> getAllUsers(Pageable pageable) {
        return ResponseEntity.ok(userService.getAllUsers(pageable));
    }

    /**
     * Fetch a user by their ID (Admin only).
     * @param id User ID
     * @return User details
     */
    @PreAuthorize("hasRole('ADMIN_ROLE')")
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    /**
     * Create a new user (Admin only).
     * @param user User details
     * @return Created user
     */
    @PreAuthorize("hasRole('ADMIN_ROLE')")
    @PostMapping
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(user));
    }

    /**
     * Update an existing user (Admin only).
     * @param id User ID
     * @param userDetails Updated user details
     * @return Updated user
     */
    @PreAuthorize("hasRole('ADMIN_ROLE')")
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody User userDetails) {
        return ResponseEntity.ok(userService.updateUser(id, userDetails));
    }

    /**
     * Delete a user (Admin only).
     * @param id User ID
     */
    @PreAuthorize("hasRole('ADMIN_ROLE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}