package com.att.acceptance.movie_theater.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.att.acceptance.movie_theater.entity.AvailabilityStatusEnum;
import com.att.acceptance.movie_theater.entity.SeatAvailability;
import com.att.acceptance.movie_theater.entity.Showtime;

@Repository
public interface SeatAvailabilityRepository extends JpaRepository<SeatAvailability, Long> {

    @Query("SELECT s FROM SeatAvailability s WHERE s.showtime = :showtime AND s.seatNumber = :seatNumber")
    Optional<SeatAvailability> findByShowtimeAndSeatNumber(Showtime showtime, String seatNumber);

    @Query("SELECT s FROM SeatAvailability s WHERE s.showtime = :showtime")
    List<SeatAvailability> findByShowtime(Showtime showtime);

    @Query("SELECT s FROM SeatAvailability s WHERE s.showtime = :showtime AND s.status = :status")
    List<SeatAvailability> findByShowtimeAndStatus(Showtime showtime, AvailabilityStatusEnum status); 

    @Query("SELECT s FROM SeatAvailability s WHERE s.showtime = :showtime AND s.status = :status")
    Page<SeatAvailability> findByShowtimeAndStatus(Showtime showtime, AvailabilityStatusEnum status, Pageable pageable); 
}
