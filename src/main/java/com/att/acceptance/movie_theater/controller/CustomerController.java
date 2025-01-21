package com.att.acceptance.movie_theater.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.att.acceptance.movie_theater.entity.Booking;
import com.att.acceptance.movie_theater.entity.Seat;
import com.att.acceptance.movie_theater.entity.Showtime;
import com.att.acceptance.movie_theater.entity.User;
import com.att.acceptance.movie_theater.service.BookingService;
import com.att.acceptance.movie_theater.service.ShowtimeService;
import com.att.acceptance.movie_theater.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/customers")
@PreAuthorize("hasRole('ROLE_CUSTOMER')") // Ensure only customers can access these endpoints
public class CustomerController {

    private final BookingService bookingService;
    private final UserService userService;
    private final ShowtimeService showtimeService; 
    
    

	/**
	 * @param bookingService
	 * @param userService
	 * @param showtimeService
	 */
	public CustomerController(BookingService bookingService, UserService userService, ShowtimeService showtimeService) {
		super();
		this.bookingService = bookingService;
		this.userService = userService;
		this.showtimeService = showtimeService;
	}


    @GetMapping("/bookings")
    public ResponseEntity<List<Booking>> getMyBookings(Pageable pageable) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName(); 
        User user = userService.findUserByEmail(username); 
        return ResponseEntity.ok(bookingService.getBookingsByUser(user, pageable)); 
    }


    @PostMapping("/bookings")
    public ResponseEntity<Booking> createBooking(@Valid @RequestBody BookingRequest bookingRequest) { 
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName(); 
        User user = userService.findUserByEmail(username); 

        Showtime showtime = showtimeService.getShowtimeById(bookingRequest.showtimeId()); 

	        // Create a Seat object 
	        Seat seat = new Seat();
	        seat.setSeatNumber(bookingRequest.seatNumber()); 
	        // Set seat price (you might need to fetch seat price based on seat number and showtime)

	        Booking booking = new Booking();
	        booking.setUser(user);
	        booking.setShowtime(showtime);
	        booking.setSeat(seat); 

	        return ResponseEntity.status(HttpStatus.CREATED).body(bookingService.createBooking(booking, user.getId(), showtime.getId()));
	    }

    @GetMapping("/showtimes")
    public ResponseEntity<Page<Showtime>> getAllShowtimes(Pageable pageable) {
        return ResponseEntity.ok(showtimeService.getAllShowtimes(pageable)); 
    }

    @GetMapping("/showtimes/{id}") 
    public ResponseEntity<Showtime> getShowtimeById(@PathVariable Long id) {
        return ResponseEntity.ok(showtimeService.getShowtimeById(id)); 
    }

    @GetMapping("/bookings/{id}") 
    @PreAuthorize("@bookingService.canAccessBooking(#id)") // Check if the customer can access this booking
    public ResponseEntity<Booking> getBookingById(@PathVariable Long id) { 
        return ResponseEntity.ok(bookingService.getBookingById(id)); 
    }

    @DeleteMapping("/bookings/{id}") 
    @PreAuthorize("@bookingService.canCancelBooking(#id)") // Check if the customer can cancel this booking
    public ResponseEntity<Void> cancelBooking(@PathVariable Long id) { 
        bookingService.deleteBooking(id); 
        return ResponseEntity.noContent().build(); 
    }
    
    public record BookingRequest(Long showtimeId, String seatNumber) {} 
}
