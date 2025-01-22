package com.att.acceptance.movie_theater.controller;

import com.att.acceptance.movie_theater.entity.Showtime;
import com.att.acceptance.movie_theater.service.ShowtimeService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import java.util.Set;

@RestController
@RequestMapping("/showtimes")
@Validated
public class ShowtimeController {

    private final ShowtimeService showtimeService;

    public ShowtimeController(ShowtimeService showtimeService) {
        this.showtimeService = showtimeService;
    }

    /**
     * Add a new showtime. (Admin only)
     *
     * @param showtime The showtime to add.
     * @return The added showtime.
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<Showtime> addShowtime(@Valid @RequestBody Showtime showtime) {
        Showtime savedShowtime = showtimeService.addShowtime(showtime);
        return ResponseEntity.ok(savedShowtime);
    }

    /**
     * Get all showtimes for a specific movie. (Accessible by all users)
     *
     * @param movieId The movie ID.
     * @return A set of showtimes for the movie.
     */
    @GetMapping("/movie/{movieId}")
    public ResponseEntity<Set<Showtime>> getShowtimesByMovie(@PathVariable @Min(1) Long movieId) {
        Set<Showtime> showtimes = showtimeService.getShowtimesByMovie(movieId);
        return ResponseEntity.ok(showtimes);
    }

    /**
     * Get all showtimes for a specific theater. (Accessible by all users)
     *
     * @param theaterId The theater ID.
     * @return A set of showtimes for the theater.
     */
    @GetMapping("/theater/{theaterId}")
    public ResponseEntity<Set<Showtime>> getShowtimesByTheater(@PathVariable @Min(1) Long theaterId) {
        Set<Showtime> showtimes = showtimeService.getShowtimesByTheater(theaterId);
        return ResponseEntity.ok(showtimes);
    }

    /**
     * Update an existing showtime. (Admin only)
     *
     * @param id The showtime ID.
     * @param updatedShowtime The updated showtime details.
     * @return The updated showtime.
     */
    @PreAuthorize("hasRole('ADMIN_ROLE')")
    @PutMapping("/{id}")
    public ResponseEntity<Showtime> updateShowtime(@PathVariable @Min(1) Long id, @Valid @RequestBody Showtime updatedShowtime) {
        Showtime showtime = showtimeService.updateShowtime(id, updatedShowtime);
        return ResponseEntity.ok(showtime);
    }

    /**
     * Delete a showtime by ID. (Admin only)
     *
     * @param id The showtime ID.
     */
    @PreAuthorize("hasRole('ADMIN_ROLE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShowtime(@PathVariable @Min(1) Long id) {
        showtimeService.deleteShowtime(id);
        return ResponseEntity.noContent().build();
    }
}