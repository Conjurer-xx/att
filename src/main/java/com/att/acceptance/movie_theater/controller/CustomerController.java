package com.att.acceptance.movie_theater.controller;

import com.att.acceptance.movie_theater.entity.Booking;
import com.att.acceptance.movie_theater.service.BookingService;
import com.att.acceptance.movie_theater.service.UserService;
import com.att.acceptance.movie_theater.util.SecurityUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import java.util.Set;

@RestController
@RequestMapping("/customer")
@Validated
public class CustomerController {

    private final BookingService bookingService;
    private final UserService userService;

    public CustomerController(BookingService bookingService, UserService userService) {
        this.bookingService = bookingService;
        this.userService = userService;
    }

    /**
     * Get all bookings for the authenticated customer or all bookings if admin.
     *
     * @return A set of bookings for the customer or all bookings for admin.
     */
    @PreAuthorize("hasAnyRole('ROLE_CUSTOMER', 'ROLE_ADMIN')")
    @GetMapping("/bookings")
    public ResponseEntity<Set<Booking>> getBookings() {
        if (SecurityUtils.hasRole("ROLE_ADMIN")) {
            return ResponseEntity.ok(bookingService.getAllBookings());
        }
        Long userId = SecurityUtils.getAuthenticatedUserId();
        return ResponseEntity.ok(bookingService.getBookingsByUser(userId));
    }

    /**
     * Create a new booking for the authenticated customer.
     *
     * @param booking The booking to create.
     * @return The created booking.
     */
    @PreAuthorize("hasAnyRole('ROLE_CUSTOMER', 'ROLE_ADMIN')")
    @PostMapping("/bookings")
    public ResponseEntity<Booking> createBooking(@Valid @RequestBody Booking booking) {
        if (SecurityUtils.hasRole("ROLE_ADMIN")) {
            Booking savedBooking = bookingService.createBooking(booking);
            return ResponseEntity.ok(savedBooking);
        }
        Long userId = SecurityUtils.getAuthenticatedUserId();
        booking.setUser(userService.getUserById(userId));
        Booking savedBooking = bookingService.createBooking(booking);
        return ResponseEntity.ok(savedBooking);
    }

    /**
     * Cancel a booking by ID for the authenticated customer or admin.
     *
     * @param bookingId The booking ID to cancel.
     */
    @PreAuthorize("hasAnyRole('ROLE_CUSTOMER', 'ROLE_ADMIN')")
    @DeleteMapping("/bookings/{bookingId}")
    public ResponseEntity<Void> cancelBooking(@PathVariable @Min(1) Long bookingId) {
        if (SecurityUtils.hasRole("ROLE_ADMIN")) {
            bookingService.cancelBooking(bookingId);
            return ResponseEntity.noContent().build();
        }
        Long userId = SecurityUtils.getAuthenticatedUserId();
        bookingService.cancelBookingForUser(bookingId, userId);
        return ResponseEntity.noContent().build();
    }
}