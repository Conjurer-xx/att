package com.att.acceptance.movie_theater.entity;

import jakarta.persistence.*;

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
    private Double price;

    @Column(nullable = false, length = 20)
    private String status = "PENDING";  // Default status: PENDING, CONFIRMED, CANCELLED

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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
