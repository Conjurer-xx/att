package com.att.acceptance.movie_theater.repository;

import com.att.acceptance.movie_theater.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
}