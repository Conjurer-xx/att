package com.att.acceptance.movie_theater.repository;

import com.att.acceptance.movie_theater.entity.Booking;
import com.att.acceptance.movie_theater.entity.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    @Query("SELECT b FROM Booking b WHERE b.user = :user") 
    Page<Booking> findByUser(User user, Pageable pageable); 
}