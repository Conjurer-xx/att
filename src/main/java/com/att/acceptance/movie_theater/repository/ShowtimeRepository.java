package com.att.acceptance.movie_theater.repository;

import com.att.acceptance.movie_theater.entity.Movie;
import com.att.acceptance.movie_theater.entity.Showtime;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ShowtimeRepository extends JpaRepository<Showtime, Long> {
	
	   @Query("SELECT s FROM Showtime s WHERE s.theater = :theater AND s.startTime < :endTime AND s.endTime > :startTime")
	    List<Showtime> findOverlappingShowtimes(String theater, LocalDateTime startTime, LocalDateTime endTime);

	   @Query("SELECT s FROM Showtime s WHERE s.movie = :movie AND s.theater = :theater " +
	            "AND s.startTime < :endTime AND s.endTime > :startTime")
	    List<Showtime> findByMovieAndTheaterAndStartTimeBeforeAndEndTimeAfter(
	            Movie movie, String theater, LocalDateTime endTime, LocalDateTime startTime); 
	   
	    /**
	     * Find all showtimes for a specific movie.
	     * 
	     * @param movieId Movie ID
	     * @param pageable Pagination details
	     * @return Paginated list of showtimes
	     */
	    Page<Showtime> findByMovieId(Long movieId, Pageable pageable);
}