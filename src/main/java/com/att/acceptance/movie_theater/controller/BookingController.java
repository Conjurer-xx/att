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
@RequestMapping("/bookings")
@Validated
public class BookingController {

    private final BookingService bookingService;
    private final UserService userService;

    public BookingController(BookingService bookingService, UserService userService) {
        this.bookingService = bookingService;
        this.userService = userService;
    }

    /**
     * Create a new booking. (Customer only)
     *
     * @param booking The booking to create.
     * @return The created booking.
     */
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    @PostMapping
    public ResponseEntity<Booking> createBooking(@Valid @RequestBody Booking booking) {
        Long userId = SecurityUtils.getAuthenticatedUserId();
        booking.setUser(userService.getUserById(userId)); // Fetch the authenticated user entity
        Booking savedBooking = bookingService.createBooking(booking);
        return ResponseEntity.ok(savedBooking);
    }

    /**
     * Get all bookings for the authenticated user. (Customer only)
     *
     * @return A set of bookings for the user.
     */
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    @GetMapping
    public ResponseEntity<Set<Booking>> getBookingsForUser() {
        Long userId = SecurityUtils.getAuthenticatedUserId();
        Set<Booking> bookings = bookingService.getBookingsByUser(userId);
        return ResponseEntity.ok(bookings);
    }

    /**
     * Cancel a booking by ID. (Customer only)
     *
     * @param id The booking ID.
     */
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelBooking(@PathVariable @Min(1) Long id) {
        bookingService.cancelBooking(id);
        return ResponseEntity.noContent().build();
    }
}