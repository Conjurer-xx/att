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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

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
    @Operation(summary = "Add a new movie", description = "Allows an admin to add a new movie to the system.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Movie added successfully", 
                    content = @Content(schema = @Schema(implementation = Movie.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
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
    @Operation(summary = "Get all movies", description = "Retrieve a list of all available movies.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of movies retrieved", 
                    content = @Content(schema = @Schema(implementation = Movie.class)))
    })
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
    @Operation(summary = "Get movie by ID", description = "Retrieve details of a specific movie by its ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Movie found", 
                    content = @Content(schema = @Schema(implementation = Movie.class))),
            @ApiResponse(responseCode = "404", description = "Movie not found")
    })
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
    @Operation(summary = "Update a movie", description = "Allows an admin to update the details of a movie.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Movie updated successfully", 
                    content = @Content(schema = @Schema(implementation = Movie.class))),
            @ApiResponse(responseCode = "404", description = "Movie not found")
    })
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
    @Operation(summary = "Delete a movie", description = "Allows an admin to delete a specific movie by ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Movie successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Movie not found")
    })
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping(path = "/delete/{id}")
    public ResponseEntity<Void> deleteMovie(@PathVariable @Min(1) Long id) {
        movieService.deleteMovie(id);
        return ResponseEntity.noContent().build();
    }
}