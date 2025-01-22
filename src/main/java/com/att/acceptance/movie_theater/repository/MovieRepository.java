package com.att.acceptance.movie_theater.repository;

import com.att.acceptance.movie_theater.entity.Movie;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

    Optional<Movie> findByTitle(String title);

    @Query("SELECT m FROM Movie m WHERE m.genre = :genre")
    Set<Movie> findByGenre(@Param("genre") String genre);

    @Query("SELECT m FROM Movie m WHERE m.releaseYear BETWEEN :startYear AND :endYear")
    Set<Movie> findByReleaseYearRange(@Param("startYear") Integer startYear, @Param("endYear") Integer endYear);
}
