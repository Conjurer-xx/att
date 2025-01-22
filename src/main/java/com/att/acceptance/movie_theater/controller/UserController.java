package com.att.acceptance.movie_theater.controller;

import com.att.acceptance.movie_theater.entity.User;
import com.att.acceptance.movie_theater.entity.RoleEnum;
import com.att.acceptance.movie_theater.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;

@RestController
@RequestMapping(value = "/api/users", produces = MediaType.APPLICATION_JSON_VALUE)
@Validated
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Register a new user.
     * Accessible to everyone.
     *
     * @param user The user to register.
     * @return The registered user.
     */
    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@Valid @RequestBody User user) {
        User registeredUser = userService.registerUser(user, RoleEnum.ROLE_CUSTOMER);
        return ResponseEntity.ok(registeredUser);
    }

    /**
     * Get all users with pagination. (Admin only)
     *
     * @param pageable Pagination details.
     * @return A page of users.
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(path = "/get-all-users")
    public ResponseEntity<Page<User>> getAllUsers(Pageable pageable) {
        Page<User> users = userService.getAllUsers(pageable);
        return ResponseEntity.ok(users);
    }

    /**
     * Get a user by ID. (Admin only)
     *
     * @param id The user ID.
     * @return The user.
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(path = "/get-single-user/{id}")
    public ResponseEntity<User> getUserById(@PathVariable @Min(1) Long id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    /**
     * Delete a user by ID. (Admin only)
     *
     * @param id The user ID.
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping(path = "/delete/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable @Min(1) Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
    
    /**
     * Update a user by ID. (Admin only)
     *
     * @param id The user ID.
     * @param user The updated user details.
     * @return The updated user.
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping(path = "/update/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> updateUser(@PathVariable Long id, @Valid @RequestBody User user) {
        User updatedUser = userService.updateUser(id, user);
        return ResponseEntity.ok(updatedUser);
    }
    
    /**
     * Assign a role to a user.
     *
     * @param userId the user ID
     * @param role the role to assign
     */
    @PostMapping("/{userId}/roles/{role}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> assignRoleToUser(@PathVariable Long userId, @PathVariable RoleEnum role) {
        userService.assignRoleToUser(userId, role);
        return ResponseEntity.ok().build();
    }
}