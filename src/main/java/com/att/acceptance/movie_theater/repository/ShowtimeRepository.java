package com.att.acceptance.movie_theater.repository;

import java.time.LocalDateTime;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.att.acceptance.movie_theater.entity.Showtime;

/**
 * Repository for accessing Showtime entities.
 */
@Repository
public interface ShowtimeRepository extends JpaRepository<Showtime, Long> {

	/**
	 * Find all showtimes for a specific movie.
	 * 
	 * @param movieId The movie ID.
	 * @return List of showtimes for the given movie.
	 */
	@Query("SELECT s FROM Showtime s WHERE s.movie.id = :movieId")
	Set<Showtime> findByMovieId(@Param("movieId") Long movieId);

	/**
	 * Find all showtimes for a specific theater.
	 * 
	 * @param theaterId The theater ID.
	 * @return List of showtimes for the given theater.
	 */
	@Query("SELECT s FROM Showtime s WHERE s.theater.id = :theaterId")
	Set<Showtime> findByTheaterId(@Param("theaterId") Long theaterId);

	/**
	 * Find overlapping showtimes in a theater.
	 * 
	 * @param theaterId The theater ID.
	 * @param startTime Start time of the show.
	 * @param endTime   End time of the show.
	 * @return True if there are overlapping showtimes, false otherwise.
	 */
	@Query("SELECT CASE WHEN COUNT(s) > 0 THEN true ELSE false END "
			+ "FROM Showtime s WHERE s.theater.id = :theaterId "
			+ "AND ((s.startTime < :endTime AND s.endTime > :startTime))")
	boolean existsOverlappingShowtime(@Param("theaterId") Long theaterId, @Param("startTime") LocalDateTime startTime,
			@Param("endTime") LocalDateTime endTime);

	/**
	 * Find showtimes starting within a specific date and time range.
	 * 
	 * @param start The start of the range.
	 * @param end   The end of the range.
	 * @return List of showtimes within the given time range.
	 */
	@Query("SELECT s FROM Showtime s WHERE s.startTime BETWEEN :start AND :end")
	Set<Showtime> findByStartTimeBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

	/**
	 * Find overlapping showtimes for a specific movie in a theater.
	 * 
	 * @param theaterId The theater ID.
	 * @param movieId   The movie ID.
	 * @param startTime Start time of the show.
	 * @param endTime   End time of the show.
	 * @return True if there are overlapping showtimes, false otherwise.
	 */
	@Query("SELECT CASE WHEN COUNT(s) > 0 THEN true ELSE false END "
			+ "FROM Showtime s WHERE s.theater.id = :theaterId " + "AND s.movie.id = :movieId "
			+ "AND ((s.startTime < :endTime AND s.endTime > :startTime))")
	boolean existsOverlappingShowtimeForMovie(@Param("theaterId") Long theaterId, @Param("movieId") Long movieId,
			@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

}
