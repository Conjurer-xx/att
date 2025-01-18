package com.att.acceptance.movie_theater.repository;

import com.att.acceptance.movie_theater.entity.Seat;
import com.att.acceptance.movie_theater.entity.Showtime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {

    @Query("SELECT s FROM Seat s WHERE s.showtime = :showtime AND s.seatNumber = :seatNumber")
    Optional<Seat> findByShowtimeAndSeatNumber(Showtime showtime, String seatNumber);

    @Query("SELECT s FROM Seat s WHERE s.showtime = :showtime AND s NOT IN (SELECT b.seat FROM Booking b WHERE b.seat.id = s.id)")
    List<Seat> findAvailableSeatsByShowtime(Showtime showtime);

    @Query("SELECT s FROM Seat s WHERE s.showtime = :showtime AND s.row = :row AND s NOT IN (SELECT b.seat FROM Booking b WHERE b.seat.id = s.id)")
    List<Seat> findAvailableSeatsByShowtimeAndRow(Showtime showtime, String row);

    @Query("SELECT s FROM Seat s WHERE s.showtime = :showtime AND s NOT IN (SELECT b.seat FROM Booking b WHERE b.seat.id = s.id)")
    List<Seat> findAvailableSeatsByShowtimeAndPriceRange(Showtime showtime, Double minPrice, Double maxPrice);

    @Query("SELECT s FROM Seat s WHERE s.showtime = :showtime AND s.seatType = :seatType AND s NOT IN (SELECT b.seat FROM Booking b WHERE b.seat.id = s.id)")
    List<Seat> findAvailableSeatsByShowtimeAndSeatType(Showtime showtime, String seatType);
}