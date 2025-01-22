package com.att.acceptance.movie_theater.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.att.acceptance.movie_theater.entity.Booking;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("SELECT b FROM Booking b WHERE b.user.id = :userId")
    Set<Booking> findByUserId(@Param("userId") Long userId);

    @Query("SELECT CASE WHEN COUNT(b) > 0 THEN true ELSE false END FROM Booking b WHERE b.seat.id = :seatId AND b.showtime.id = :showtimeId")
    boolean existsBySeatIdAndShowtimeId(@Param("seatId") Long seatId, @Param("showtimeId") Long showtimeId);
}

