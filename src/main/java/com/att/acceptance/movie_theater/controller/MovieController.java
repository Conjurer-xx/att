package com.att.acceptance.movie_theater.controller;

import com.att.acceptance.movie_theater.entity.Movie;
import com.att.acceptance.movie_theater.service.MovieService;

import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for managing movies.
 * Customers can view movies, while admins have full access.
 */
@RestController
@RequestMapping("/api/movies")
@Validated
public class MovieController {

    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    /**
     * Fetch all movies with pagination.
     * Accessible to all users.
     * @param pageable Pagination details
     * @return Paginated list of movies
     */
    @GetMapping
    public ResponseEntity<Page<Movie>> getAllMovies(Pageable pageable) {
        return ResponseEntity.ok(movieService.getAllMovies(pageable));
    }

    /**
     * Fetch a movie by its ID.
     * Accessible to all users.
     * @param id Movie ID
     * @return Movie details
     */
    @GetMapping("/{id}")
    public ResponseEntity<Movie> getMovieById(@PathVariable Long id) {
        return ResponseEntity.ok(movieService.getMovieById(id));
    }

    /**
     * Add a new movie (Admin only).
     * @param movie Movie details
     * @return Created movie
     */
    @PreAuthorize("hasRole('ADMIN_ROLE')")
    @PostMapping
    public ResponseEntity<Movie> createMovie(@Valid @RequestBody Movie movie) {
        return ResponseEntity.status(HttpStatus.CREATED).body(movieService.createMovie(movie));
    }

    /**
     * Update an existing movie (Admin only).
     * @param id Movie ID
     * @param movieDetails Updated details
     * @return Updated movie
     */
    @PreAuthorize("hasRole('ADMIN_ROLE')")
    @PutMapping("/{id}")
    public ResponseEntity<Movie> updateMovie(@PathVariable Long id, @Valid @RequestBody Movie movieDetails) {
        return ResponseEntity.ok(movieService.updateMovie(id, movieDetails));
    }

    /**
     * Delete a movie (Admin only).
     * @param id Movie ID
     */
    @PreAuthorize("hasRole('ADMIN_ROLE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMovie(@PathVariable Long id) {
        movieService.deleteMovie(id);
        return ResponseEntity.noContent().build();
    }
}