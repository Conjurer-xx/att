package com.att.acceptance.movie_theater.controller;



import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.att.acceptance.movie_theater.entity.Movie;
import com.att.acceptance.movie_theater.entity.Showtime;
import com.att.acceptance.movie_theater.service.MovieService;
import com.att.acceptance.movie_theater.service.ShowtimeService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ROLE_ADMIN')") // Ensure only admins can access these endpoints
public class AdminController {

    private final MovieService movieService;
    private final ShowtimeService showtimeService; 
    



	/**
	 * @param movieService
	 * @param showtimeService
	 */
	public AdminController(MovieService movieService, ShowtimeService showtimeService) {
		super();
		this.movieService = movieService;
		this.showtimeService = showtimeService;
	}

	@GetMapping("/movies")
    public ResponseEntity<Page<Movie>> getAllMovies(Pageable pageable) {
        return ResponseEntity.ok(movieService.getAllMovies(pageable));
    }

    @GetMapping("/movies/{id}")
    public ResponseEntity<Movie> getMovieById(@PathVariable Long id) {
        return ResponseEntity.ok(movieService.getMovieById(id));
    }

    @PostMapping("/movies")
    public ResponseEntity<Movie> createMovie(@RequestBody Movie movie) {
        return ResponseEntity.status(HttpStatus.CREATED).body(movieService.createMovie(movie));
    }

    @PutMapping("/movies/{id}")
    public ResponseEntity<Movie> updateMovie(@PathVariable Long id, @RequestBody Movie movieDetails) {
        return ResponseEntity.ok(movieService.updateMovie(id, movieDetails));
    }

    @DeleteMapping("/movies/{id}")
    public ResponseEntity<Void> deleteMovie(@PathVariable Long id) {
        movieService.deleteMovie(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/showtimes")
    public ResponseEntity<Page<Showtime>> getAllShowtimes(Pageable pageable) {
        return ResponseEntity.ok(showtimeService.getAllShowtimes(pageable));
    }

    @GetMapping("/showtimes/{id}")
    public ResponseEntity<Showtime> getShowtimeById(@PathVariable Long id) {
        return ResponseEntity.ok(showtimeService.getShowtimeById(id));
    }

    @PostMapping("/showtimes")
    public ResponseEntity<Showtime> createShowtime(@Valid @RequestBody Showtime showtime) {
        return ResponseEntity.status(HttpStatus.CREATED).body(showtimeService.createShowtime(showtime));
    }

    @PutMapping("/showtimes/{id}")
    @PreAuthorize("@showtimeService.canUpdateShowtime(#id)") // Custom authorization expression
    public ResponseEntity<Showtime> updateShowtime(@PathVariable Long id, @Valid @RequestBody Showtime showtimeDetails) {
        return ResponseEntity.ok(showtimeService.updateShowtime(id, showtimeDetails));
    }

    @DeleteMapping("/showtimes/{id}")
    @PreAuthorize("@showtimeService.canDeleteShowtime(#id)") // Custom authorization expression
    public ResponseEntity<Void> deleteShowtime(@PathVariable Long id) {
        showtimeService.deleteShowtime(id);
        return ResponseEntity.noContent().build();
    }
}
