package com.att.acceptance.movie_theater.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * Entity representing a Booking in the movie theater system.
 * Maps to the database table for bookings and includes relationships
 * with User, Showtime, and Seat entities.
 */
@Entity
@Table(
    name = "bookings",
    uniqueConstraints = @UniqueConstraint(columnNames = {"seat_id", "showtime_id"})
)
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "showtime_id", nullable = false)
    private Showtime showtime;

    @OneToOne
    @JoinColumn(name = "seat_id", unique = true, nullable = false)
    private Seat seat;

    @Column(nullable = false)
    @NotNull(message = "Price is required")
    @Positive(message = "Price must be positive")
    @Digits(integer = 5, fraction = 2, message = "Price can have up to 5 digits before the decimal and 2 after")
    private float price;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private BookingStatus status = BookingStatus.PENDING;

    // Getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Showtime getShowtime() {
        return showtime;
    }

    public void setShowtime(Showtime showtime) {
        this.showtime = showtime;
    }

    public Seat getSeat() {
        return seat;
    }

    public void setSeat(Seat seat) {
        this.seat = seat;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }
}