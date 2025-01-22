package com.att.acceptance.movie_theater.security;

import com.att.acceptance.movie_theater.entity.Booking;
import com.att.acceptance.movie_theater.repository.BookingRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * Security utility to validate booking ownership.
 * Ensures only booking owners or admins can access/manage a booking.
 */
@Component
public class BookingSecurity {

    private final BookingRepository bookingRepository;

    public BookingSecurity(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    /**
     * Check if the authenticated user is the owner of the booking or has admin privileges.
     * @param bookingId ID of the booking to validate
     * @return True if the user is the owner or an admin, false otherwise
     */
    public boolean isOwner(Long bookingId) {
        // Get the authenticated user's username
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // Fetch the booking and validate ownership
        Booking booking = bookingRepository.findById(bookingId).orElse(null);
        if (booking == null) {
            return false; // Booking does not exist
        }

        // Check if the user is the booking owner or has the ROLE_ADMIN
        return booking.getUser().getEmail().equals(username)
                || SecurityContextHolder.getContext().getAuthentication().getAuthorities()
                .stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN_ROLE"));
    }
}
