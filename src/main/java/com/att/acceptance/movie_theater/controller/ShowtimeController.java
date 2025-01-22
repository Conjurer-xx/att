package com.att.acceptance.movie_theater.controller;

import com.att.acceptance.movie_theater.entity.Showtime;
import com.att.acceptance.movie_theater.service.ShowtimeService;

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
@RequestMapping(value = "/api/showtimes", produces = MediaType.APPLICATION_JSON_VALUE)
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
    @Operation(summary = "Add a new showtime", description = "Allows an admin to add a new showtime to the system.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Showtime added successfully", 
                    content = @Content(schema = @Schema(implementation = Showtime.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(path = "/add", consumes = MediaType.APPLICATION_JSON_VALUE)
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
    @Operation(summary = "Get showtime by Movie ID", description = "Retrieve details of a specific showtime by its ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Showtime found", 
                    content = @Content(schema = @Schema(implementation = Showtime.class))),
            @ApiResponse(responseCode = "404", description = "Showtime not found")
    })
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
    @Operation(summary = "Get showtime by Theater ID", description = "Retrieve details of a specific showtime by its theater ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Showtime found", 
                    content = @Content(schema = @Schema(implementation = Showtime.class))),
            @ApiResponse(responseCode = "404", description = "Showtime not found")
    })
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
    @Operation(summary = "Update a showtime", description = "Allows an admin to update the details of a showtime.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Showtime updated successfully", 
                    content = @Content(schema = @Schema(implementation = Showtime.class))),
            @ApiResponse(responseCode = "404", description = "Showtime not found")
    })
    @PreAuthorize("hasRole('ADMIN_ROLE')")
    @PutMapping(path = "/update/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Showtime> updateShowtime(@PathVariable @Min(1) Long id, @Valid @RequestBody Showtime updatedShowtime) {
        Showtime showtime = showtimeService.updateShowtime(id, updatedShowtime);
        return ResponseEntity.ok(showtime);
    }

    /**
     * Delete a showtime by ID. (Admin only)
     *
     * @param id The showtime ID.
     */
    @Operation(summary = "Delete a showtime", description = "Allows an admin to delete a specific showtime by ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Showtime successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Showtime not found")
    })
    @PreAuthorize("hasRole('ADMIN_ROLE')")
    @DeleteMapping(path = "/delete/{id}")
    public ResponseEntity<Void> deleteShowtime(@PathVariable @Min(1) Long id) {
        showtimeService.deleteShowtime(id);
        return ResponseEntity.noContent().build();
    }
}