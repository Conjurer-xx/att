package com.att.acceptance.movie_theater.repository;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.att.acceptance.movie_theater.entity.Seat;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {

    Optional<Seat> findBySeatNumber(String seatNumber);

    @Query("SELECT s FROM Seat s JOIN s.seatAvailability sa WHERE sa.showtime.id = :showtimeId")
    Set<Seat> findBySeatAvailability_ShowtimeId(@Param("showtimeId") Long showtimeId);

    @Query("SELECT s FROM Seat s WHERE s.seatAvailability.showtime.theater.id = :theaterId")
    Set<Seat> findByTheaterId(@Param("theaterId") Long theaterId);

    @Query("SELECT COUNT(s) FROM Seat s WHERE s.seatAvailability.showtime.theater.id = :theaterId")
    long countSeatsByTheaterId(@Param("theaterId") Long theaterId);

    @Query("SELECT CASE WHEN COUNT(s) > 0 THEN true ELSE false END FROM Seat s WHERE s.seatNumber = :seatNumber AND s.seatAvailability.showtime.theater.id = :theaterId")
    boolean existsBySeatNumberAndTheaterId(@Param("seatNumber") String seatNumber, @Param("theaterId") Long theaterId);
}
