package com.att.acceptance.movie_theater.entity;

import java.util.Objects;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Entity representing the availability of a seat for a specific showtime.
 * Maps to the "seat_availability" table and links seats to their availability status.
 */
@Entity
@Table(name = "seat_availability")
public class SeatAvailability {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier for the seat availability record.")
    private Long id;

    /**
     * The seat associated with this availability record.
     */
    @OneToOne(mappedBy = "seatAvailability", cascade = CascadeType.ALL)
    @Schema(description = "The seat associated with this availability record.")
    private Seat seat; 

    /**
     * The showtime for which this seat availability is tracked.
     */
    @ManyToOne
    @JoinColumn(name = "showtime_id", nullable = false)
    @Schema(description = "The showtime for which this seat availability is tracked.")
    private Showtime showtime;

    /**
     * The unique seat number for this record.
     */
    @Column(nullable = false)
    @Schema(description = "The unique seat number for this record.")
    private String seatNumber;

    /**
     * The availability status of the seat (e.g., AVAILABLE, BOOKED).
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Schema(description = "The availability status of the seat.")
    private AvailabilityStatusEnum status; 

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Showtime getShowtime() {
        return showtime;
    }

    public void setShowtime(Showtime showtime) {
        this.showtime = showtime;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public AvailabilityStatusEnum getStatus() {
        return status;
    }

    public void setStatus(AvailabilityStatusEnum status) {
        this.status = status;
    }

    /**
	 * @return the seat
	 */
	public Seat getSeat() {
		return seat;
	}

	/**
	 * @param seat the seat to set
	 */
	public void setSeat(Seat seat) {
		this.seat = seat;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SeatAvailability that = (SeatAvailability) o;
        return Objects.equals(id, that.id) &&
               Objects.equals(seatNumber, that.seatNumber) &&
               Objects.equals(showtime, that.showtime) &&
               status == that.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, seatNumber, showtime, status);
    }

    @Override
    public String toString() {
        return "SeatAvailability{" +
               "id=" + id +
               ", showtime=" + showtime +
               ", seatNumber='" + seatNumber + '\'' +
               ", status=" + status +
               '}';
    }
}
