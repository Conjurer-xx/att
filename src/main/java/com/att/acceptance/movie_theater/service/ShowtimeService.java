package com.att.acceptance.movie_theater.service;

import com.att.acceptance.movie_theater.entity.Movie;
import com.att.acceptance.movie_theater.entity.Showtime;
import com.att.acceptance.movie_theater.exception.MovieNotFoundException;
import com.att.acceptance.movie_theater.exception.ShowtimeNotFoundException;
import com.att.acceptance.movie_theater.exception.ShowtimeOverlapException;
import com.att.acceptance.movie_theater.repository.MovieRepository;
import com.att.acceptance.movie_theater.repository.ShowtimeRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ShowtimeService {

    private final ShowtimeRepository showtimeRepository;
    private final MovieRepository movieRepository;
    private final ShowtimeService service; // Inject itself

    public ShowtimeService(ShowtimeRepository showtimeRepository, MovieRepository movieRepository, ShowtimeService showtimeService) {
        this.showtimeRepository = showtimeRepository;
        this.movieRepository = movieRepository;
        this.service = showtimeService;
    }

    public Page<Showtime> getAllShowtimes(Pageable pageable) {
        return showtimeRepository.findAll(pageable); 
    }

    @Transactional(readOnly = true)
    public Showtime getShowtimeById(Long id) {
        Optional<Showtime> showtime = showtimeRepository.findById(id);
        return showtime.orElseThrow(() -> new ShowtimeNotFoundException("Showtime not found with id: " + id));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')") 
    public Showtime createShowtime(Showtime showtime) {
        Movie movie = movieRepository.findById(showtime.getMovie().getId())
                .orElseThrow(() -> new MovieNotFoundException("Movie not found.")); 

        showtime.setMovie(movie); 
        
        // Check for overlapping showtimes for the same movie in the same theater
        List<Showtime> existingShowtimes = showtimeRepository.findByMovieAndTheaterAndStartTimeBeforeAndEndTimeAfter(
                showtime.getMovie(), showtime.getTheater(), showtime.getEndTime(), showtime.getStartTime());

        if (!existingShowtimes.isEmpty()) {
            throw new ShowtimeOverlapException("Overlapping showtime found for this movie and theater.");
        }

        // Check if endTime is after startTime
        if (showtime.getEndTime().isBefore(showtime.getStartTime())) {
            throw new IllegalArgumentException("End time must be after start time.");
        }

        return showtimeRepository.save(showtime);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') and @showtimeService.canUpdateShowtime(#id)") 
    public Showtime updateShowtime(Long id, Showtime showtimeDetails) {
        Showtime showtime = service.getShowtimeById(id); // Call getShowtimeById via injected instance

        // Check for overlapping showtimes after update
        List<Showtime> existingShowtimes = showtimeRepository.findOverlappingShowtimes(
                showtimeDetails.getTheater(), showtimeDetails.getEndTime(), showtimeDetails.getStartTime()
        );

        if (!existingShowtimes.isEmpty() && !existingShowtimes.contains(showtime)) {
            throw new ShowtimeOverlapException("Overlapping showtime found for this theater.");
        }

        showtime.setMovie(showtimeDetails.getMovie());
        showtime.setTheater(showtimeDetails.getTheater());
        showtime.setStartTime(showtimeDetails.getStartTime());
        showtime.setEndTime(showtimeDetails.getEndTime());

        return showtimeRepository.save(showtime);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') and @showtimeService.canDeleteShowtime(#id)") 
    public void deleteShowtime(Long id) {
        service.getShowtimeById(id); // Call getShowtimeById to ensure showtime exists before deletion
        showtimeRepository.deleteById(id);
    }
    
    /**
     * Fetch all showtimes for a specific movie with pagination.
     * 
     * @param movieId Movie ID
     * @param pageable Pagination details
     * @return Paginated list of showtimes for the movie
     */
    public Page<Showtime> getShowtimesByMovie(Long movieId, Pageable pageable) {
        return showtimeRepository.findByMovieId(movieId, pageable);
    }
}