package com.att.acceptance.movie_theater.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.att.acceptance.movie_theater.entity.Showtime;
import com.att.acceptance.movie_theater.service.ShowtimeService;

import jakarta.validation.Valid;

/**
 * Controller for managing showtimes.
 * Customers can view showtimes, while admins have full control.
 */
@RestController
@RequestMapping("/api/showtimes")
@Validated
public class ShowtimeController {

    private final ShowtimeService showtimeService;


    public ShowtimeController(ShowtimeService showtimeService) {
        this.showtimeService = showtimeService;
    }

    /**
     * Fetch all showtimes with pagination.
     * Accessible to all users.
     * @param pageable Pagination details
     * @return Paginated list of showtimes
     */
    @GetMapping
    public ResponseEntity<Page<Showtime>> getAllShowtimes(Pageable pageable) {
        return ResponseEntity.ok(showtimeService.getAllShowtimes(pageable));
    }

    /**
     * Fetch all showtimes for a specific movie with pagination.
     * 
     * @param movieId Movie ID
     * @param pageable Pagination details
     * @return Paginated list of showtimes
     */
    @PreAuthorize("hasRole('CUSTOMER_ROLE') or hasRole('ADMIN_ROLE')")
    @GetMapping("/movie/{movieId}")
    public ResponseEntity<Page<Showtime>> getShowtimesByMovie(
            @PathVariable Long movieId, Pageable pageable) {
        return ResponseEntity.ok(showtimeService.getShowtimesByMovie(movieId, pageable));
    }
    
    /**
     * Add a new showtime (Admin only).
     * @param showtime Showtime details
     * @return Created showtime
     */
    @PreAuthorize("hasRole('ADMIN_ROLE')")
    @PostMapping
    public ResponseEntity<Showtime> createShowtime(@Valid @RequestBody Showtime showtime) {
        return ResponseEntity.status(HttpStatus.CREATED).body(showtimeService.createShowtime(showtime));
    }

    /**
     * Update an existing showtime (Admin only).
     * @param id Showtime ID
     * @param showtimeDetails Updated showtime details
     * @return Updated showtime
     */
    @PreAuthorize("hasRole('ADMIN_ROLE')")
    @PutMapping("/{id}")
    public ResponseEntity<Showtime> updateShowtime(
            @PathVariable Long id,
            @Valid @RequestBody Showtime showtimeDetails) {
        return ResponseEntity.ok(showtimeService.updateShowtime(id, showtimeDetails));
    }

    /**
     * Delete a showtime (Admin only).
     * @param id Showtime ID
     */
    @PreAuthorize("hasRole('ADMIN_ROLE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShowtime(@PathVariable Long id) {
        showtimeService.deleteShowtime(id);
        return ResponseEntity.noContent().build();
    }
}