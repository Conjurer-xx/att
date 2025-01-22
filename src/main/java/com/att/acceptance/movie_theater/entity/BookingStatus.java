package com.att.acceptance.movie_theater.entity;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Enum representing the status of a booking.
 */
@Schema(description = "The status of a booking.")
public enum BookingStatus {
	@Schema(description = "The booking is awaiting confirmation.")
    PENDING,     // The booking is awaiting confirmation
    @Schema(description = "The booking is confirmed.")
    CONFIRMED,   // The booking is confirmed
    @Schema(description = "The booking has been cancelled.")
    CANCELLED    // The booking has been cancelled
}
