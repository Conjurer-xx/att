package com.att.acceptance.movie_theater.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.att.acceptance.movie_theater.entity.AvailabilityStatusEnum;
import com.att.acceptance.movie_theater.entity.Booking;
import com.att.acceptance.movie_theater.entity.Seat;
import com.att.acceptance.movie_theater.entity.SeatAvailability;
import com.att.acceptance.movie_theater.entity.Showtime;
import com.att.acceptance.movie_theater.entity.User;
import com.att.acceptance.movie_theater.exception.BookingNotFoundException;
import com.att.acceptance.movie_theater.exception.SeatNotAvailableException;
import com.att.acceptance.movie_theater.exception.ShowtimeNotFoundException;
import com.att.acceptance.movie_theater.exception.UserNotFoundException;
import com.att.acceptance.movie_theater.repository.BookingRepository;
import com.att.acceptance.movie_theater.repository.SeatAvailabilityRepository;
import com.att.acceptance.movie_theater.repository.SeatRepository;
import com.att.acceptance.movie_theater.repository.ShowtimeRepository;
import com.att.acceptance.movie_theater.repository.UserRepository;
import com.att.acceptance.movie_theater.security.SecurityUtils;

@Service
public class BookingService {

    private static final String BOOKING_NOT_FOUND_WITH_ID = "Booking not found with id: ";
    private final BookingRepository bookingRepository;
    private final ShowtimeRepository showtimeRepository;
    private final SeatRepository seatRepository;
    private final SeatAvailabilityRepository seatAvailabilityRepository;
    private final UserRepository userRepository;
    private final UserService userService; 

    public BookingService(BookingRepository bookingRepository, 
                          ShowtimeRepository showtimeRepository, 
                          SeatRepository seatRepository, 
                          SeatAvailabilityRepository seatAvailabilityRepository, 
                          UserRepository userRepository,
                          UserService userService) { 
        this.bookingRepository = bookingRepository;
        this.showtimeRepository = showtimeRepository;
        this.seatRepository = seatRepository;
        this.seatAvailabilityRepository = seatAvailabilityRepository;
        this.userRepository = userRepository;
        this.userService = userService; 
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
    
    public Booking createBooking(Booking booking, Long userId) {
        // Fetch the User object from the repository
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));
        
        // Set the user on the booking
        booking.setUser(user);

        // Save and return the booking
        return bookingRepository.save(booking);
    }

    @Transactional
    public Booking createBooking(Booking booking, Long userId, Long showtimeId) {
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication.getPrincipal() instanceof UserDetails)) {
            throw new RuntimeException("Authentication failed."); 
        }
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername(); 
        User user = userService.findUserByEmail(username); 

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

    /**
     * Update an existing booking with new details.
     * 
     * @param bookingId ID of the booking to update
     * @param bookingDetails New booking details
     * @param userId ID of the user making the update
     * @return Updated booking
     */
    public Booking updateBooking(Long bookingId, Booking bookingDetails, Long userId) {
        // Fetch the existing booking
        Booking existingBooking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException("Booking not found with ID: " + bookingId));

        // Check if the authenticated user is authorized
        if (!existingBooking.getUser().getId().equals(userId) &&
                !SecurityUtils.hasRole("ADMIN_ROLE")) {
            throw new AccessDeniedException("You are not authorized to update this booking.");
        }

        // Seat availability check
        if (!existingBooking.getSeat().getId().equals(bookingDetails.getSeat().getId()) ||
                !existingBooking.getShowtime().getId().equals(bookingDetails.getShowtime().getId())) {
            if (bookingRepository.existsBySeatIdAndShowtimeId(
                    bookingDetails.getSeat().getId(), bookingDetails.getShowtime().getId())) {
                throw new SeatNotAvailableException(
                        "Seat is already booked for this showtime. Seat ID: " + bookingDetails.getSeat().getId());
            }
        }

        // Update booking details
        existingBooking.setSeat(bookingDetails.getSeat());
        existingBooking.setPrice(bookingDetails.getPrice());
        existingBooking.setShowtime(bookingDetails.getShowtime());

        // Save and return the updated booking
        return bookingRepository.save(existingBooking);
    }

    @Transactional
    public void deleteBooking(Long id) {
    	
        Booking booking = getBookingById(id);

        // Check if the user is authorized to delete this booking
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName(); 
        User user = userService.findUserByEmail(username); 
        if (!user.getId().equals(booking.getUser().getId())) {
            throw new AccessDeniedException("You are not authorized to delete this booking.");
        }
        
        SeatAvailability seatAvailability = booking.getSeat().getSeatAvailability();
        seatAvailability.setStatus(AvailabilityStatusEnum.AVAILABLE);
        seatAvailabilityRepository.save(seatAvailability); 

        bookingRepository.delete(booking); 
    }    
    
    // Custom authorization methods
    public boolean canAccessBooking(Long id) {
        // Check if the current user is authorized to access this booking
        Booking booking = getBookingById(id); 
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName(); 
        User user = userService.findUserByEmail(username); 
        return booking.getUser().getId().equals(user.getId()); 
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
    

    public List<Booking> getBookingsByUser(User user, Pageable pageable) {
        return bookingRepository.findByUser(user, pageable).getContent(); 
    }
    
    public void cancelBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException("Booking not found with ID: " + bookingId));

        booking.setStatus("CANCELLED"); // Update status
        bookingRepository.save(booking);
    }

    public void confirmBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException("Booking not found with ID: " + bookingId));

        booking.setStatus("CONFIRMED"); // Update status
        bookingRepository.save(booking);
    }

    public Double getTotalPrice(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException("Booking not found with ID: " + bookingId));

        return booking.getPrice();
    }
    
    /**
     * Fetch all bookings for a specific user.
     * 
     * @param userId User ID
     * @param pageable Pagination details
     * @return Paginated list of bookings for the user
     */
    public Page<Booking> getCustomerBookings(Long userId, Pageable pageable) {
        return bookingRepository.findByUserId(userId, pageable);
    }
    
}