package com.att.acceptance.movie_theater.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Utility class for accessing security-related information about the authenticated user.
 */
public class SecurityUtils {

    /**
     * Get the ID of the authenticated user from the SecurityContext.
     * This assumes the user ID is stored as part of the principal in the security context.
     * @return Authenticated user's ID
     */
    public static Long getAuthenticatedUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("No authenticated user found");
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails) {
            // Assume the username is the user ID or map it to the actual user entity
            String username = ((UserDetails) principal).getUsername();
            // Convert the username to a Long if it's the user ID (or fetch user ID from DB if needed)
            return Long.valueOf(username); // Replace this with a DB lookup if needed
        }

        throw new IllegalStateException("Invalid principal type: " + principal);
    }
    
    /**
     * Check if the authenticated user has the specified role.
     * 
     * @param role The role to check
     * @return True if the user has the role, false otherwise
     */
    public static boolean hasRole(String role) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        return authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_" + role));
    }
    
	/**
	 * Get the username of the authenticated user.
	 * 
	 * @return The username of the authenticated user
	 */
        public static String getAuthenticatedUsername() {
        	
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

			if (authentication == null || !authentication.isAuthenticated()) {
				throw new IllegalStateException("No authenticated user found");
			}

			Object principal = authentication.getPrincipal();
			if (principal instanceof UserDetails) {
				return ((UserDetails) principal).getUsername();
			}

			throw new IllegalStateException("Invalid principal type: " + principal);	
			
        }
}