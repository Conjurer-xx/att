package com.att.acceptance.movie_theater.controller;

import com.att.acceptance.movie_theater.entity.Booking;
import com.att.acceptance.movie_theater.entity.Seat;
import com.att.acceptance.movie_theater.security.SecurityUtils;
import com.att.acceptance.movie_theater.service.BookingService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import jakarta.validation.Valid;
import java.util.List;

/**
 * Controller for managing bookings.
 * Customers can manage their own bookings, and admins can view all bookings.
 */
@RestController
@RequestMapping("/api/bookings")
@Validated
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    /**
     * Fetch all bookings (Admin only).
     * @param pageable Pagination details
     * @return Paginated list of all bookings
     */
    @PreAuthorize("hasRole('ADMIN_ROLE')")
    @GetMapping
    public ResponseEntity<Page<Booking>> getAllBookings(Pageable pageable) {
        return ResponseEntity.ok(bookingService.getAllBookings(pageable));
    }

    /**
     * Fetch bookings for the authenticated customer.
     * @param pageable Pagination details
     * @return Paginated list of bookings for the customer
     */
    @PreAuthorize("hasRole('CUSTOMER_ROLE')")
    @GetMapping("/my")
    public ResponseEntity<Page<Booking>> getCustomerBookings(Pageable pageable) {
        Long userId = SecurityUtils.getAuthenticatedUserId();
        return ResponseEntity.ok(bookingService.getCustomerBookings(userId, pageable));
    }

    /**
     * Fetch a booking by its ID (Admins or the booking owner).
     * @param id Booking ID
     * @return Booking details
     */
    @PreAuthorize("hasRole('ADMIN_ROLE') or @bookingSecurity.isOwner(#id)")
    @GetMapping("/{id}")
    public ResponseEntity<Booking> getBookingById(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.getBookingById(id));
    }

    /**
     * Create a new booking (Customers only).
     * @param booking Booking details
     * @return Created booking
     */
    @PreAuthorize("hasRole('CUSTOMER_ROLE')")
    @PostMapping
    public ResponseEntity<Booking> createBooking(@Valid @RequestBody Booking booking) {
        Long userId = SecurityUtils.getAuthenticatedUserId(); // Fetch the authenticated user's ID
        return ResponseEntity.status(HttpStatus.CREATED).body(bookingService.createBooking(booking, userId));
    }

    /**
     * Delete a booking (Admins or the booking owner).
     * @param id Booking ID
     */
    @PreAuthorize("hasRole('ADMIN_ROLE') or @bookingSecurity.isOwner(#id)")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBooking(@PathVariable Long id) {
        bookingService.deleteBooking(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Find available seats for a specific showtime.
     * Accessible to all users.
     * @param showtimeId Showtime ID
     * @param row Seat row (optional)
     * @param minPrice Minimum price (optional)
     * @param maxPrice Maximum price (optional)
     * @param seatType Seat type (optional)
     * @return List of available seats
     */
    @GetMapping("/available-seats/{showtimeId}")
    public ResponseEntity<List<Seat>> findAvailableSeats(
            @PathVariable Long showtimeId,
            @RequestParam(required = false) String row,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) String seatType) {
        return ResponseEntity.ok(bookingService.findAvailableSeats(showtimeId, row, minPrice, maxPrice, seatType));
    }

	/**
	 * Update a booking (Admins or the booking owner).
	 * 
	 * @param id             Booking ID
	 * @param bookingDetails Updated booking details
	 * @return Updated booking
	 */
    @PreAuthorize("hasRole('ADMIN_ROLE') or @bookingSecurity.isOwner(#id)")
    @PutMapping("/{id}")
    public ResponseEntity<Booking> updateBooking(
            @PathVariable Long id,
            @Valid @RequestBody Booking bookingDetails) {
        Long userId = SecurityUtils.getAuthenticatedUserId(); // Fetch the authenticated user ID
        return ResponseEntity.ok(bookingService.updateBooking(id, bookingDetails, userId));
    }
    
    /**
     * Cancel a booking (Admins or booking owners).
     * 
     * @param id Booking ID
     */
    @PreAuthorize("hasRole('ADMIN_ROLE') or @bookingSecurity.isOwner(#id)")
    @DeleteMapping("/{id}/cancel")
    public ResponseEntity<Void> cancelBooking(@PathVariable Long id) {
        bookingService.cancelBooking(id);
        return ResponseEntity.noContent().build();
    }
	
    /**
     * Confirm a booking (Admins or booking owners).
     * 
     * @param id Booking ID
     */
    @PreAuthorize("hasRole('ADMIN_ROLE') or @bookingSecurity.isOwner(#id)")
    @PostMapping("/{id}/confirm")
    public ResponseEntity<Void> confirmBooking(@PathVariable Long id) {
        bookingService.confirmBooking(id);
        return ResponseEntity.noContent().build();
    }
    
    
    /**
     * Get the total price for a booking.
     * 
     * @param id Booking ID
     * @return Total price
     */
    @GetMapping("/{id}/total-price")
    public ResponseEntity<Double> getTotalPrice(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.getTotalPrice(id));
    
    }
 }
