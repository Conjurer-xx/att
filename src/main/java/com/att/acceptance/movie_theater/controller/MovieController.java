package com.att.acceptance.movie_theater.controller;

import com.att.acceptance.movie_theater.entity.Movie;
import com.att.acceptance.movie_theater.service.MovieService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;

@RestController
@RequestMapping(value = "/api/movies", produces = MediaType.APPLICATION_JSON_VALUE)
@Validated
public class MovieController {

    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    /**
     * Add a new movie. (Admin only)
     *
     * @param movie The movie to add.
     * @return The added movie.
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(path = "/add", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Movie> addMovie(@Valid @RequestBody Movie movie) {
        Movie savedMovie = movieService.addMovie(movie);
        return ResponseEntity.ok(savedMovie);
    }

    /**
     * Get all movies with pagination.
     * Accessible by both customers and admins.
     *
     * @param pageable Pagination details.
     * @return A page of movies.
     */
    @GetMapping(path = "/get-all-movies")
    public ResponseEntity<Page<Movie>> getAllMovies(Pageable pageable) {
        Page<Movie> movies = movieService.getAllMovies(pageable);
        return ResponseEntity.ok(movies);
    }

    /**
     * Get a movie by ID.
     * Accessible by both customers and admins.
     *
     * @param id The movie ID.
     * @return The movie.
     */
    @GetMapping(path = "/get-single-movie/{id}")
    public ResponseEntity<Movie> getMovieById(@PathVariable @Min(1) Long id) {
        Movie movie = movieService.getMovieById(id);
        return ResponseEntity.ok(movie);
    }

    /**
     * Update an existing movie. (Admin only)
     *
     * @param id The movie ID.
     * @param updatedMovie The updated movie details.
     * @return The updated movie.
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping(path = "/update/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Movie> updateMovie(@PathVariable @Min(1) Long id, @Valid @RequestBody Movie updatedMovie) {
        Movie movie = movieService.updateMovie(id, updatedMovie);
        return ResponseEntity.ok(movie);
    }

    /**
     * Delete a movie by ID. (Admin only)
     *
     * @param id The movie ID.
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping(path = "/delete/{id}")
    public ResponseEntity<Void> deleteMovie(@PathVariable @Min(1) Long id) {
        movieService.deleteMovie(id);
        return ResponseEntity.noContent().build();
    }
}