package com.att.acceptance.movie_theater.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.att.acceptance.movie_theater.entity.SeatAvailability;


/**
 * Repository for accessing SeatAvailability entities.
 */
@Repository
public interface SeatAvailabilityRepository extends JpaRepository<SeatAvailability, Long> {
    @Query("SELECT CASE WHEN COUNT(sa) > 0 THEN true ELSE false END FROM SeatAvailability sa WHERE sa.seatNumber = :seatNumber AND sa.showtime.id = :showtimeId")
    boolean existsBySeatNumberAndShowtime_Id(@Param("seatNumber") String seatNumber, @Param("showtimeId") Long showtimeId);
}


