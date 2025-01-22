package com.att.acceptance.movie_theater.entity;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Enumeration representing the availability status of a seat.
 * Defines whether a seat is AVAILABLE or BOOKED.
 */
@Schema(description = "Enumeration representing the availability status of a seat.")
public enum AvailabilityStatusEnum {
	@Schema(description = "The seat is available for booking.")
    AVAILABLE, // The seat is available for booking
    @Schema(description = "The seat has already been booked.")
    BOOKED     // The seat has already been booked
}
