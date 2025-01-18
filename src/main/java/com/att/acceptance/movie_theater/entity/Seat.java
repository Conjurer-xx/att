package com.att.acceptance.movie_theater.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "seats")
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Seat number is required.")
    private String seatNumber;

    @OneToOne(mappedBy = "seat", cascade = CascadeType.ALL)
    private SeatAvailability seatAvailability; 

    @Column(nullable = false)
    private BigDecimal price;

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
	 * @return the price
	 */
	public BigDecimal getPrice() {
		return price;
	}

	/**
	 * @param price the price to set
	 */
	public void setPrice(BigDecimal price) {
		this.price = price;
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
		return "Seat [id=" + id + ", seatNumber=" + seatNumber + ", seatAvailability=" + seatAvailability + ", price="
				+ price + "]";
	}



}