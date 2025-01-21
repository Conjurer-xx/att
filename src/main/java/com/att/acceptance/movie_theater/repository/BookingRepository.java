package com.att.acceptance.movie_theater.repository;

import com.att.acceptance.movie_theater.entity.Booking;
import com.att.acceptance.movie_theater.entity.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    /**
     * Find all bookings for a specific user.
     * @param userId User ID
     * @param pageable Pagination details
     * @return Paginated list of bookings
     */
    @Query("SELECT b FROM Booking b WHERE b.user.id = :userId")
    Page<Booking> findByUserId(Long userId, Pageable pageable);

    /**
     * Cancel a booking by updating its status to 'CANCELLED'.
     * @param bookingId Booking ID
     */
    @Modifying
    @Transactional
    @Query("UPDATE Booking b SET b.status = 'CANCELLED' WHERE b.id = :bookingId")
    void cancelBooking(Long bookingId);

    /**
     * Confirm a booking by updating its status to 'CONFIRMED'.
     * @param bookingId Booking ID
     */
    @Modifying
    @Transactional
    @Query("UPDATE Booking b SET b.status = 'CONFIRMED' WHERE b.id = :bookingId")
    void confirmBooking(Long bookingId);

    /**
     * Calculate the total price of a booking.
     * @param bookingId Booking ID
     * @return Total price
     */
    @Query("SELECT b.price FROM Booking b WHERE b.id = :bookingId")
    Double findTotalPriceByBookingId(Long bookingId);
    
	/**
	 * Find all bookings for a specific user.
	 * 
	 * @param user     User
	 * @param pageable Pagination details
	 * @return Paginated list of bookings
	 */
    Page<Booking> findByUser(User user, Pageable pageable);
    
    /**
     * Check if a seat is already booked for a specific showtime.
     * 
     * @param seatId Seat ID
     * @param showtimeId Showtime ID
     * @return True if the seat is booked, false otherwise
     */    
    boolean existsBySeatIdAndShowtimeId(Long seatId, Long showtimeId);
}
