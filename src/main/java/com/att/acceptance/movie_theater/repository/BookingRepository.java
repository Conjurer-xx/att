package com.att.acceptance.movie_theater.repository;

import com.att.acceptance.movie_theater.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
}