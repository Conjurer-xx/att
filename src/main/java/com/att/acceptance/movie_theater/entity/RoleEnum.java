package com.att.acceptance.movie_theater.entity;

import io.swagger.v3.oas.annotations.media.Schema;
/**
 * Enumeration representing the roles in the movie theater system.
 * These roles define the access permissions for users.
 */
@Schema(description = "Enumeration representing the roles in the movie theater system.")
public enum RoleEnum {
	@Schema(description = "Administrator role with full access")
    ROLE_ADMIN,   // Administrator role with full access
    @Schema(description = "Customer role with limited access")
    ROLE_CUSTOMER // Customer role with limited access
}
