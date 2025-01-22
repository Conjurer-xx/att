package com.att.acceptance.movie_theater.controller;

import com.att.acceptance.movie_theater.entity.Movie;
import com.att.acceptance.movie_theater.entity.Theater;
import com.att.acceptance.movie_theater.entity.Showtime;
import com.att.acceptance.movie_theater.service.MovieService;
import com.att.acceptance.movie_theater.service.TheaterService;
import com.att.acceptance.movie_theater.service.ShowtimeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;


/**
 * Controller for admin-level operations.
 * These operations require admin access and provide additional functionality.
 * Admins can add new movies, theaters, and showtimes.
 *
 **/
@RestController
@RequestMapping("/admin")
@Validated
public class AdminController {

    private final MovieService movieService;
    private final TheaterService theaterService;
    private final ShowtimeService showtimeService;

    public AdminController(MovieService movieService, TheaterService theaterService, ShowtimeService showtimeService) {
        this.movieService = movieService;
        this.theaterService = theaterService;
        this.showtimeService = showtimeService;
    }

    /**
     * Add a new movie. (Admin only)
     *
     * @param movie The movie to add.
     * @return The added movie.
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/movies")
    public ResponseEntity<Movie> addMovie(@Valid @RequestBody Movie movie) {
        Movie savedMovie = movieService.addMovie(movie);
        return ResponseEntity.ok(savedMovie);
    }

    /**
     * Add a new theater. (Admin only)
     *
     * @param theater The theater to add.
     * @return The added theater.
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/theaters")
    public ResponseEntity<Theater> addTheater(@Valid @RequestBody Theater theater) {
        Theater savedTheater = theaterService.addTheater(theater);
        return ResponseEntity.ok(savedTheater);
    }

    /**
     * Add a new showtime. (Admin only)
     *
     * @param showtime The showtime to add.
     * @return The added showtime.
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/showtimes")
    public ResponseEntity<Showtime> addShowtime(@Valid @RequestBody Showtime showtime) {
        Showtime savedShowtime = showtimeService.addShowtime(showtime);
        return ResponseEntity.ok(savedShowtime);
    }

    /**
     * Get all movies. (Accessible by everyone, paginated)
     *
     * @param pageable Pagination details.
     * @return A page of all movies.
     */
    @GetMapping("/movies")
    public ResponseEntity<Page<Movie>> getAllMovies(Pageable pageable) {
        Page<Movie> movies = movieService.getAllMovies(pageable);
        return ResponseEntity.ok(movies);
    }

    /**
     * Delete a movie by ID. (Admin only)
     *
     * @param movieId The ID of the movie to delete.
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/movies/{movieId}")
    public ResponseEntity<Void> deleteMovie(@PathVariable @Min(1) Long movieId) {
        movieService.deleteMovie(movieId);
        return ResponseEntity.noContent().build();
    }

    // Additional admin-level operations can be added here for further flexibility.
}