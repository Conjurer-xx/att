package com.att.acceptance.movie_theater.controller;

import com.att.acceptance.movie_theater.entity.RoleEnum;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/roles")
@Validated
public class RoleController {

    /**
     * Get all predefined roles. (Admin only)
     *
     * @return A set of all roles.
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<Set<RoleEnum>> getAllRoles() {
        Set<RoleEnum> roles = Arrays.stream(RoleEnum.values()).collect(Collectors.toSet());
        return ResponseEntity.ok(roles);
    }

    /**
     * Check if a role exists by name. (Admin only)
     *
     * @param roleName The name of the role.
     * @return The role if it exists.
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/{roleName}")
    public ResponseEntity<RoleEnum> getRoleByName(@PathVariable RoleEnum roleName) {
        return ResponseEntity.ok(roleName);
    }
}