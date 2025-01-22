package com.att.acceptance.movie_theater.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.Objects;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Entity representing a Seat in the movie theater system.
 * Each seat has a unique seat number and is linked to its availability.
 */
@Entity
@Table(name = "seats")
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier for the seat.")
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "theater_id", nullable = false)
    @NotBlank(message = "Theater is required")
    @Schema(description = "The theater to which the seat belongs.")
    private Theater theater;


    /**
     * Unique identifier for the seat in the theater.
     * Example: A1, B12.
     */
    @NotBlank(message = "Seat number is required.")
    @Size(max = 10, message = "Seat number must not exceed 10 characters.")
    @Column(nullable = false, unique = true)
    @Schema(description = "Unique identifier for the seat.")
    private String seatNumber;

    /**
     * Represents the availability status of the seat.
     * This is a one-to-one relationship with the SeatAvailability entity.
     */
    @OneToOne(mappedBy = "seat", cascade = CascadeType.ALL, orphanRemoval = true)
    @Schema(description = "The availability status of the seat.")
    private SeatAvailability seatAvailability; 

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    /**
     * @return the seatAvailability
     */
    public SeatAvailability getSeatAvailability() {
        return seatAvailability;
    }

    /**
     * @param seatAvailability the seatAvailability to set
     */
    public void setSeatAvailability(SeatAvailability seatAvailability) {
        this.seatAvailability = seatAvailability;
    }

    /**
	 * @return the theater
	 */
	public Theater getTheater() {
		return theater;
	}

	/**
	 * @param theater the theater to set
	 */
	public void setTheater(Theater theater) {
		this.theater = theater;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Seat seat = (Seat) o;
        return Objects.equals(id, seat.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

	@Override
	public String toString() {
		return "Seat [id=" + id + ", theater=" + theater + ", seatNumber=" + seatNumber + ", seatAvailability="
				+ seatAvailability + "]";
	}

}