package com.att.acceptance.movie_theater.controller;

import com.att.acceptance.movie_theater.entity.Theater;
import com.att.acceptance.movie_theater.entity.Seat;
import com.att.acceptance.movie_theater.service.TheaterService;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import java.util.Set;

@RestController
@RequestMapping(value = "/api/theaters", produces = MediaType.APPLICATION_JSON_VALUE)
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
    @Operation(summary = "Add a new theater", description = "Allows an admin to add a new theater to the system.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Theater added successfully", 
                    content = @Content(schema = @Schema(implementation = Theater.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(path = "/add", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Theater> addTheater(@Valid @RequestBody Theater theater) {
        Theater savedTheater = theaterService.addTheater(theater);
        return ResponseEntity.ok(savedTheater);
    }

    /**
     * Get all theaters. (Accessible by all users)
     *
     * @return A set of all theaters.
     */
    @Operation(summary = "Get all theaters", description = "Retrieve a list of all available theaters.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of theaters retrieved", 
                    content = @Content(schema = @Schema(implementation = Theater.class)))
    })
    @GetMapping(path = "/get-all-theaters")
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
    @Operation(summary = "Get theater by ID", description = "Retrieve details of a specific theater by its ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Theater found", 
                    content = @Content(schema = @Schema(implementation = Theater.class))),
            @ApiResponse(responseCode = "404", description = "Theater not found")
    })
    @GetMapping(path = "/get-single-theater/{id}")
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
    @Operation(summary = "Update a theater", description = "Allows an admin to update the details of a theater.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Theater updated successfully", 
                    content = @Content(schema = @Schema(implementation = Theater.class))),
            @ApiResponse(responseCode = "404", description = "Theater not found")
    })
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping(path = "/update/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Theater> updateTheater(@PathVariable @Min(1) Long id, @Valid @RequestBody Theater updatedTheater) {
        Theater theater = theaterService.updateTheater(id, updatedTheater);
        return ResponseEntity.ok(theater);
    }

    /**
     * Delete a theater by ID. (Admin only)
     *
     * @param id The theater ID.
     */
    @Operation(summary = "Delete a theater", description = "Allows an admin to delete a specific theater by ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Theater successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Theater not found")
    })
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping(path = "/delete/{id}")
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
    @Operation(summary = "Add a seat to a theater", description = "Allows an admin to add a new seat to a theater.")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Seat added successfully", content = @Content(schema = @Schema(implementation = Seat.class))),
			@ApiResponse(responseCode = "400", description = "Invalid input") })
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
    @Operation(summary = "Get seats by theater", description = "Retrieve a list of all seats in a specific theater.")
    @ApiResponses({
    	@ApiResponse(responseCode = "200", description = "List of seats retrieved", content = @Content(schema = @Schema(implementation = Seat.class))),
        @ApiResponse(responseCode = "404", description = "Theater not found")
    })        
    @GetMapping("/{theaterId}/seats")
    public ResponseEntity<Set<Seat>> getSeatsByTheater(@PathVariable @Min(1) Long theaterId) {
        Set<Seat> seats = theaterService.getSeatsByTheater(theaterId);
        return ResponseEntity.ok(seats);
    }
}