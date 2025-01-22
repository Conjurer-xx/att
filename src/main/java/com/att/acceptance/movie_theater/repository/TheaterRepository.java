package com.att.acceptance.movie_theater.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.att.acceptance.movie_theater.entity.Theater;

/**
 * Repository for accessing Theater entities.
 */
@Repository
public interface TheaterRepository extends JpaRepository<Theater, Long> {
    Optional<Theater> findByName(String name);
}