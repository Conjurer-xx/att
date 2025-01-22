package com.att.acceptance.movie_theater.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.att.acceptance.movie_theater.entity.Showtime;
import com.att.acceptance.movie_theater.repository.ShowtimeRepository;
import com.att.acceptance.movie_theater.repository.TheaterRepository;

/**
 * Service for managing showtimes.
 *
 * This service is responsible for adding, fetching, updating, and deleting
 * showtimes.
 * 
 */
@Service
public class ShowtimeService {

    private final ShowtimeRepository showtimeRepository;
    private final TheaterRepository theaterRepository;

    public ShowtimeService(ShowtimeRepository showtimeRepository, TheaterRepository theaterRepository) {
        this.showtimeRepository = showtimeRepository;
        this.theaterRepository = theaterRepository;
    }

    /**
     * Add a new showtime.
     *
     * @param showtime The showtime to add.
     * @return The added showtime.
     */
    @Transactional
    public Showtime addShowtime(Showtime showtime) {
        validateShowtime(showtime);

        Long theaterId = showtime.getTheater().getId();
        theaterRepository.findById(theaterId).orElseThrow(() ->
                new IllegalArgumentException("Theater with ID " + theaterId + " does not exist."));

        return showtimeRepository.save(showtime);
    }

    /**
     * Validate a new showtime.
     *
     * @param showtime The showtime to validate.
     */
    private void validateShowtime(Showtime showtime) {
        if (showtimeRepository.existsOverlappingShowtime(
                showtime.getTheater().getId(),
                showtime.getStartTime(),
                showtime.getEndTime())) {
            throw new IllegalArgumentException("Showtime overlaps with an existing showtime in the same theater.");
        }

        if (showtimeRepository.existsOverlappingShowtimeForMovie(
                showtime.getTheater().getId(),
                showtime.getMovie().getId(),
                showtime.getStartTime(),
                showtime.getEndTime())) {
            throw new IllegalArgumentException("Showtime overlaps with an existing showtime for the same movie in the theater.");
        }
    }

    /**
     * Fetch all showtimes for a specific movie.
     *
     * @param movieId The movie ID.
     * @return A set of showtimes for the movie.
     */
    @Transactional(readOnly = true)
    public Set<Showtime> getShowtimesByMovie(Long movieId) {
        return Set.copyOf(showtimeRepository.findByMovieId(movieId));
    }

    /**
     * Fetch all showtimes for a specific theater.
     *
     * @param theaterId The theater ID.
     * @return A set of showtimes for the theater.
     */
    @Transactional(readOnly = true)
    public Set<Showtime> getShowtimesByTheater(Long theaterId) {
        theaterRepository.findById(theaterId).orElseThrow(() ->
                new IllegalArgumentException("Theater with ID " + theaterId + " does not exist."));
        return Set.copyOf(showtimeRepository.findByTheaterId(theaterId));
    }

    /**
     * Delete a showtime by its ID.
     *
     * @param showtimeId The ID of the showtime to delete.
     */
    @Transactional
    public void deleteShowtime(Long showtimeId) {
        showtimeRepository.findById(showtimeId).orElseThrow(() ->
                new IllegalArgumentException("Showtime with ID " + showtimeId + " does not exist."));
        showtimeRepository.deleteById(showtimeId);
    }

    /**
     * Update an existing showtime.
     *
     * @param showtimeId The ID of the showtime to update.
     * @param updatedShowtime The updated showtime details.
     * @return The updated showtime.
     */
    @Transactional
    public Showtime updateShowtime(Long showtimeId, Showtime updatedShowtime) {
        Showtime existingShowtime = showtimeRepository.findById(showtimeId).orElseThrow(() ->
                new IllegalArgumentException("Showtime with ID " + showtimeId + " does not exist."));

        validateShowtime(updatedShowtime);

        existingShowtime.setStartTime(updatedShowtime.getStartTime());
        existingShowtime.setEndTime(updatedShowtime.getEndTime());
        existingShowtime.setMovie(updatedShowtime.getMovie());
        existingShowtime.setTheater(updatedShowtime.getTheater());

        return showtimeRepository.save(existingShowtime);
    }
    

    /**
     * remove showtime by id
     * 
     * @param id The ID of the showtime to remove.
     */
    @Transactional
    public void removeShowtime(Long id) {
        Showtime showtime = showtimeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Showtime not found with ID: " + id));
        showtimeRepository.deleteById(showtime.getId());
    }
    
    /**
     * find showtime by id
     * 
     * @param id The ID of the showtime to find.
     * @return The showtime
     */
    @Transactional(readOnly = true)
    public Showtime findShowtimeById(Long id) {
        return showtimeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Showtime not found with ID: " + id));
    }
    
	/**
	 * find all showtimes
	 * 
	 * @return A list of showtimes.
	 */
    public List<Showtime> findAllShowtimes() {
        return new ArrayList<>(showtimeRepository.findAll());
    }


}