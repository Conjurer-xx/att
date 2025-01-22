package com.att.acceptance.movie_theater.controller;

import com.att.acceptance.movie_theater.entity.Theater;
import com.att.acceptance.movie_theater.entity.Seat;
import com.att.acceptance.movie_theater.service.TheaterService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import java.util.Set;

@RestController
@RequestMapping("/theaters")
@Validated
public class TheaterController {

    private final TheaterService theaterService;

    public TheaterController(TheaterService theaterService) {
        this.theaterService = theaterService;
    }

    /**
     * Add a new theater. (Admin only)
     *
     * @param theater The theater to add.
     * @return The added theater.
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<Theater> addTheater(@Valid @RequestBody Theater theater) {
        Theater savedTheater = theaterService.addTheater(theater);
        return ResponseEntity.ok(savedTheater);
    }

    /**
     * Get all theaters. (Accessible by all users)
     *
     * @return A set of all theaters.
     */
    @GetMapping
    public ResponseEntity<Set<Theater>> getAllTheaters() {
        Set<Theater> theaters = theaterService.getAllTheaters();
        return ResponseEntity.ok(theaters);
    }

    /**
     * Get a theater by ID. (Accessible by all users)
     *
     * @param id The theater ID.
     * @return The theater.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Theater> getTheaterById(@PathVariable @Min(1) Long id) {
        Theater theater = theaterService.getTheaterById(id);
        return ResponseEntity.ok(theater);
    }

    /**
     * Update an existing theater. (Admin only)
     *
     * @param id The theater ID.
     * @param updatedTheater The updated theater details.
     * @return The updated theater.
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Theater> updateTheater(@PathVariable @Min(1) Long id, @Valid @RequestBody Theater updatedTheater) {
        Theater theater = theaterService.updateTheater(id, updatedTheater);
        return ResponseEntity.ok(theater);
    }

    /**
     * Delete a theater by ID. (Admin only)
     *
     * @param id The theater ID.
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTheater(@PathVariable @Min(1) Long id) {
        theaterService.deleteTheater(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Add a new seat to a theater. (Admin only)
     *
     * @param theaterId The theater ID.
     * @param seat The seat to add.
     * @return The added seat.
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/{theaterId}/seats")
    public ResponseEntity<Seat> addSeatToTheater(@PathVariable @Min(1) Long theaterId, @Valid @RequestBody Seat seat) {
        Seat savedSeat = theaterService.addSeatToTheater(theaterId, seat);
        return ResponseEntity.ok(savedSeat);
    }

    /**
     * Get all seats for a specific theater. (Accessible by all users)
     *
     * @param theaterId The theater ID.
     * @return A set of seats in the theater.
     */
    @GetMapping("/{theaterId}/seats")
    public ResponseEntity<Set<Seat>> getSeatsByTheater(@PathVariable @Min(1) Long theaterId) {
        Set<Seat> seats = theaterService.getSeatsByTheater(theaterId);
        return ResponseEntity.ok(seats);
    }
}