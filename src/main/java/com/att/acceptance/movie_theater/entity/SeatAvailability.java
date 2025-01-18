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

@Entity
@Table(name = "seat_availability")
public class SeatAvailability {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(mappedBy = "seatAvailability", cascade = CascadeType.ALL)
    private Seat seat; 

    @ManyToOne
    @JoinColumn(name = "showtime_id", nullable = false)
    private Showtime showtime;

    @Column(nullable = false)
    private String seatNumber;

    @Enumerated(EnumType.STRING)
    private AvailabilityStatusEnum status; 

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the showtime
	 */
	public Showtime getShowtime() {
		return showtime;
	}

	/**
	 * @param showtime the showtime to set
	 */
	public void setShowtime(Showtime showtime) {
		this.showtime = showtime;
	}

	/**
	 * @return the seatNumber
	 */
	public String getSeatNumber() {
		return seatNumber;
	}

	/**
	 * @param seatNumber the seatNumber to set
	 */
	public void setSeatNumber(String seatNumber) {
		this.seatNumber = seatNumber;
	}

	/**
	 * @return the status
	 */
	public AvailabilityStatusEnum getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(AvailabilityStatusEnum status) {
		this.status = status;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, seatNumber, showtime, status);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SeatAvailability other = (SeatAvailability) obj;
		return Objects.equals(id, other.id) && Objects.equals(seatNumber, other.seatNumber)
				&& Objects.equals(showtime, other.showtime) && status == other.status;
	}

	@Override
	public String toString() {
		return "SeatAvailability [id=" + id + ", showtime=" + showtime + ", seatNumber=" + seatNumber + ", status="
				+ status + "]";
	}

    
    
    
    
}