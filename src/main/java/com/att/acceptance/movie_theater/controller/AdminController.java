package com.att.acceptance.movie_theater.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.att.acceptance.movie_theater.entity.Movie;
import com.att.acceptance.movie_theater.entity.Showtime;
import com.att.acceptance.movie_theater.entity.Theater;
import com.att.acceptance.movie_theater.service.MovieService;
import com.att.acceptance.movie_theater.service.ShowtimeService;
import com.att.acceptance.movie_theater.service.TheaterService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;

/**
 * Controller for admin-level operations. These operations require admin access
 * and provide additional functionality. Admins can add new movies, theaters,
 * and showtimes.
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
	@Operation(summary = "Add a new movie", description = "Allows an admin to add a new movie.")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Movie added successfully", content = @Content(schema = @Schema(implementation = Movie.class))),
			@ApiResponse(responseCode = "400", description = "Invalid input") })
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
	@Operation(summary = "Add a new theater", description = "Allows an admin to add a new theater.")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Theater added successfully", content = @Content(schema = @Schema(implementation = Theater.class))),
			@ApiResponse(responseCode = "400", description = "Invalid input") })
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
	@Operation(summary = "Add a new showtime", description = "Allows an admin to add a new showtime.")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Showtime added successfully", content = @Content(schema = @Schema(implementation = Showtime.class))),
			@ApiResponse(responseCode = "400", description = "Invalid input") })
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
	@Operation(summary = "Get all movies", description = "Retrieve all movies with pagination.")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Movies retrieved successfully", content = @Content(schema = @Schema(implementation = Page.class))),
			@ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = Page.class))) })
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
	@Operation(summary = "Delete a movie by ID", description = "Allows an admin to delete a movie by ID.")
	@ApiResponses({ @ApiResponse(responseCode = "204", description = "Movie deleted successfully"),
			@ApiResponse(responseCode = "400", description = "Invalid input"),
			@ApiResponse(responseCode = "401", description = "Unauthorized") })
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@DeleteMapping("/movies/{movieId}")
	public ResponseEntity<Void> deleteMovie(@PathVariable @Min(1) Long movieId) {
		movieService.deleteMovie(movieId);
		return ResponseEntity.noContent().build();
	}

	// Additional admin-level operations can be added here for further flexibility.
}