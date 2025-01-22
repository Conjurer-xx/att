package com.att.acceptance.movie_theater.service;

import com.att.acceptance.movie_theater.entity.*;
import com.att.acceptance.movie_theater.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final ShowtimeRepository showtimeRepository;
    private final SeatRepository seatRepository;
    private final UserRepository userRepository;

    public BookingService(BookingRepository bookingRepository, ShowtimeRepository showtimeRepository, SeatRepository seatRepository, UserRepository userRepository) {
        this.bookingRepository = bookingRepository;
        this.showtimeRepository = showtimeRepository;
        this.seatRepository = seatRepository;
        this.userRepository = userRepository;
    }

    /**
     * Create a new booking.
     *
     * @param booking The booking to create.
     * @return The created booking.
     */
    @Transactional
    public Booking createBooking(Booking booking) {
        // Validate user
        userRepository.findById(booking.getUser().getId()).orElseThrow(() ->
                new IllegalArgumentException("User with ID " + booking.getUser().getId() + " does not exist."));

        // Validate showtime
        showtimeRepository.findById(booking.getShowtime().getId()).orElseThrow(() ->
                new IllegalArgumentException("Showtime with ID " + booking.getShowtime().getId() + " does not exist."));

        // Validate seat
        seatRepository.findById(booking.getSeat().getId()).orElseThrow(() ->
                new IllegalArgumentException("Seat with ID " + booking.getSeat().getId() + " does not exist."));

        if (bookingRepository.existsBySeatIdAndShowtimeId(booking.getSeat().getId(), booking.getShowtime().getId())) {
            throw new IllegalArgumentException("The seat is already booked for the selected showtime.");
        }

        return bookingRepository.save(booking);
    }

    /**
     * Fetch all bookings for a specific user.
     *
     * @param userId The user ID.
     * @return A set of bookings.
     */
    @Transactional(readOnly = true)
    public Set<Booking> getBookingsByUser(Long userId) {
        userRepository.findById(userId).orElseThrow(() ->
                new IllegalArgumentException("User with ID " + userId + " does not exist."));
        return Set.copyOf(bookingRepository.findByUserId(userId));
    }

    /**
     * Cancel a booking by its ID.
     *
     * @param bookingId The booking ID.
     */
    @Transactional
    public void cancelBooking(Long bookingId) {
        bookingRepository.findById(bookingId).orElseThrow(() ->
                new IllegalArgumentException("Booking with ID " + bookingId + " does not exist."));
        bookingRepository.deleteById(bookingId);
    }
    
    /**
     * Fetch all bookings.
     * @return A set of bookings.
     */
    @Transactional(readOnly = true)
    public Set<Booking> getAllBookings() {
        return new HashSet<>(bookingRepository.findAll());
    }
    
	/***
	 * Cancel a booking by its ID for a specific user.
	 *
	 * @param bookingId The booking ID.
	 * @param userId    The user ID.
	 */
    @Transactional
    public void cancelBookingForUser(Long bookingId, Long userId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found."));
        
        if (!booking.getUser().getId().equals(userId)) {
            throw new SecurityException("You are not authorized to cancel this booking.");
        }
        
        bookingRepository.deleteById(bookingId);
    }


}