package com.att.acceptance.movie_theater.service;

import com.att.acceptance.movie_theater.entity.*;
import com.att.acceptance.movie_theater.exception.*;
import com.att.acceptance.movie_theater.repository.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class BookingService {

    private static final String BOOKING_NOT_FOUND_WITH_ID = "Booking not found with id: ";
	private final BookingRepository bookingRepository;
    private final ShowtimeRepository showtimeRepository;
    private final UserRepository userRepository;
    private final SeatRepository seatRepository;
    private final SeatAvailabilityRepository seatAvailabilityRepository;

    public BookingService(BookingRepository bookingRepository, 
                          ShowtimeRepository showtimeRepository, 
                          UserRepository userRepository, 
                          SeatRepository seatRepository,
                          SeatAvailabilityRepository seatAvailabilityRepository) {
        this.bookingRepository = bookingRepository;
        this.showtimeRepository = showtimeRepository;
        this.userRepository = userRepository;
        this.seatRepository = seatRepository;
        this.seatAvailabilityRepository = seatAvailabilityRepository;
    }

    @Transactional(readOnly = true)
    public Page<Booking> getAllBookings(Pageable pageable) {
        return bookingRepository.findAll(pageable); 
    }

    @Transactional(readOnly = true)
    public Booking getBookingById(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new BookingNotFoundException(BOOKING_NOT_FOUND_WITH_ID + id));
    }

    @Transactional
    public Booking createBooking(Booking booking, Long userId, Long showtimeId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found."));

        Showtime showtime = showtimeRepository.findById(showtimeId)
                .orElseThrow(() -> new ShowtimeNotFoundException("Showtime not found."));

        booking.setUser(user);
        booking.setShowtime(showtime);

        Seat seat = seatRepository.findByShowtimeAndSeatNumber(showtime, booking.getSeat().getSeatNumber())
                .orElseThrow(() -> new RuntimeException("Seat not found.")); 

        SeatAvailability seatAvailability = seatAvailabilityRepository.findByShowtimeAndSeatNumber(showtime, seat.getSeatNumber())
                .orElseThrow(() -> new SeatNotAvailableException("Seat is not available."));

        if (!seatAvailability.getStatus().equals(AvailabilityStatusEnum.AVAILABLE)) {
            throw new SeatNotAvailableException("Seat is already booked.");
        }

        booking.setSeat(seat); 

        booking = bookingRepository.save(booking); 

        seatAvailability.setStatus(AvailabilityStatusEnum.BOOKED);
        seatAvailabilityRepository.save(seatAvailability); 

        return booking;
    }

    @Transactional
    public Booking updateBooking(Long id, Booking bookingDetails) {
        // Retrieve the booking directly within the transaction
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new BookingNotFoundException(BOOKING_NOT_FOUND_WITH_ID + id)); 

        if (bookingDetails.getSeat() != null && !bookingDetails.getSeat().getSeatNumber().equals(booking.getSeat().getSeatNumber())) { 
            // Check if the new seat is available
            Seat newSeat = seatRepository.findByShowtimeAndSeatNumber(booking.getShowtime(), bookingDetails.getSeat().getSeatNumber())
                    .orElseThrow(() -> new RuntimeException("Seat not found.")); 

            SeatAvailability newSeatAvailability = seatAvailabilityRepository.findByShowtimeAndSeatNumber(booking.getShowtime(), newSeat.getSeatNumber())
                    .orElseThrow(() -> new SeatNotAvailableException("Seat is not available."));

            if (!newSeatAvailability.getStatus().equals(AvailabilityStatusEnum.AVAILABLE)) {
                throw new SeatNotAvailableException("Seat is already booked.");
            }

            // Update the old seat availability to AVAILABLE
            SeatAvailability oldSeatAvailability = booking.getSeat().getSeatAvailability();
            oldSeatAvailability.setStatus(AvailabilityStatusEnum.AVAILABLE);
            seatAvailabilityRepository.save(oldSeatAvailability);

            // Update the new seat availability to BOOKED
            newSeatAvailability.setStatus(AvailabilityStatusEnum.BOOKED);
            seatAvailabilityRepository.save(newSeatAvailability);

            booking.setSeat(newSeat); 
        }

        booking = bookingRepository.save(booking); 

        return booking;
    }

    @Transactional
    public void deleteBooking(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new BookingNotFoundException(BOOKING_NOT_FOUND_WITH_ID + id)); 

        SeatAvailability seatAvailability = booking.getSeat().getSeatAvailability();
        seatAvailability.setStatus(AvailabilityStatusEnum.AVAILABLE);
        seatAvailabilityRepository.save(seatAvailability); 

        bookingRepository.delete(booking); 
    }    
    
    
  
    @Transactional(readOnly = true)
    public List<Seat> findAvailableSeats(Long showtimeId, String row, Double minPrice, Double maxPrice, String seatType) {
        Showtime showtime = showtimeRepository.findById(showtimeId)
                .orElseThrow(() -> new ShowtimeNotFoundException("Showtime not found."));

        List<SeatAvailability> availableSeatAvailabilities = seatAvailabilityRepository.findByShowtimeAndStatus(showtime, AvailabilityStatusEnum.AVAILABLE);

        return availableSeatAvailabilities.stream()
                .<Seat>map(availability -> seatRepository.findByShowtimeAndSeatNumber(showtime, availability.getSeatNumber())
                        .orElseThrow(() -> new RuntimeException("Seat not found")))
                .filter(seat -> {
                    boolean matchesRow = row == null || row.equals(seat.getSeatNumber().substring(0, 1)); 
                    boolean matchesPrice = (minPrice == null || seat.getPrice().compareTo(BigDecimal.valueOf(minPrice)) >= 0) && 
                                               (maxPrice == null || seat.getPrice().compareTo(BigDecimal.valueOf(maxPrice)) <= 0);
                    // Remove the seatType filter
                    return matchesRow && matchesPrice; 
                })
                .toList();
    }
}