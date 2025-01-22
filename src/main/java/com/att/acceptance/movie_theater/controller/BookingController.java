package com.att.acceptance.movie_theater.controller;

import java.util.Set;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.att.acceptance.movie_theater.entity.Booking;
import com.att.acceptance.movie_theater.service.BookingService;
import com.att.acceptance.movie_theater.service.UserService;
import com.att.acceptance.movie_theater.util.SecurityUtils;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;

@RestController
@RequestMapping("/api/bookings")
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
    @PostMapping(path = "/create", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Booking> createBooking(@Valid @RequestBody Booking booking) {
        Long userId = SecurityUtils.getAuthenticatedUserId();
        booking.setUser(userService.getUserById(userId)); // Fetch the authenticated user entity
        Booking savedBooking = bookingService.createBooking(booking);
        return ResponseEntity.ok(savedBooking);
    }
    
    /**
     * Update a booking by ID. (Admin only)
     *
     * @param id The booking ID.
     * @param booking The updated booking details.
     * @return The updated booking.
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping(path = "/update/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Booking> updateBooking(@PathVariable Long id, @Valid @RequestBody Booking booking) {
        Booking updatedBooking = bookingService.updateBooking(id, booking);
        return ResponseEntity.ok(updatedBooking);
    }

    /**
     * Get all bookings for the authenticated user. (Customer only)
     *
     * @return A set of bookings for the user.
     */
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    @GetMapping(path = "/get-user-booking", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Set<Booking>> getBookingsForUser() {
        Long userId = SecurityUtils.getAuthenticatedUserId();
        Set<Booking> bookings = bookingService.getBookingsByUser(userId);
        return ResponseEntity.ok(bookings);
    }
    
    /**
     * Get all bookings. (Admin only)
     *
     * @return List of all bookings.
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(path = "/get-all-bookings", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Set<Booking>> getAllBookings() {
        Set<Booking> bookings = bookingService.getAllBookings();
        return ResponseEntity.ok(bookings);
    }

    /**
     * Cancel a booking by ID. (Customer only)
     *
     * @param id The booking ID.
     */
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    @DeleteMapping(path = "/delete/{id}")
    public ResponseEntity<Void> cancelBooking(@PathVariable @Min(1) Long id) {
        bookingService.cancelBooking(id);
        return ResponseEntity.noContent().build();
    }
}