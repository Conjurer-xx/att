package com.att.acceptance.movie_theater.controller;

import com.att.acceptance.movie_theater.entity.Booking;
import com.att.acceptance.movie_theater.entity.Seat;
import com.att.acceptance.movie_theater.service.BookingService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping
    public ResponseEntity<Page<Booking>> getAllBookings(Pageable pageable) {
        return ResponseEntity.ok(bookingService.getAllBookings(pageable)); 
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Booking> getBookingById(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.getBookingById(id));
    }

    @PostMapping
    public ResponseEntity<Booking> createBooking(@RequestBody Booking booking, 
                                                @RequestParam Long userId, 
                                                @RequestParam Long showtimeId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bookingService.createBooking(booking, userId, showtimeId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Booking> updateBooking(@PathVariable Long id, @RequestBody Booking bookingDetails) {
        return ResponseEntity.ok(bookingService.updateBooking(id, bookingDetails));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBooking(@PathVariable Long id) {
        bookingService.deleteBooking(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/available-seats/{showtimeId}")
    public ResponseEntity<List<Seat>> findAvailableSeats(
            @PathVariable Long showtimeId,
            @RequestParam(required = false) String row, 
            @RequestParam(required = false) Double minPrice, 
            @RequestParam(required = false) Double maxPrice, 
            @RequestParam(required = false) String seatType) {
        return ResponseEntity.ok(bookingService.findAvailableSeats(showtimeId, row, minPrice, maxPrice, seatType));
    }
}