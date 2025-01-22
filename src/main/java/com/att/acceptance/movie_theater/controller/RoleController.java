package com.att.acceptance.movie_theater.controller;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.att.acceptance.movie_theater.entity.RoleEnum;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping(value = "/api/roles", produces = MediaType.APPLICATION_JSON_VALUE)
@Validated
public class RoleController {

	/**
	 * Get all predefined roles. (Admin only)
	 *
	 * @return A set of all roles.
	 */
	@Operation(summary = "Get all roles", description = "Retrieve all predefined roles.")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Roles retrieved successfully", content = @Content(schema = @Schema(implementation = RoleEnum.class))) })
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping(path = "/get-all-roles")
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
	@Operation(summary = "Get role by name", description = "Check if a role exists by name.")
	@ApiResponses({
            @ApiResponse(responseCode = "200", description = "Role retrieved successfully", content = @Content(schema = @Schema(implementation = RoleEnum.class))),
            @ApiResponse(responseCode = "404", description = "Role not found")
            })
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/{roleName}")
	public ResponseEntity<RoleEnum> getRoleByName(@PathVariable RoleEnum roleName) {
		return ResponseEntity.ok(roleName);
	}
}