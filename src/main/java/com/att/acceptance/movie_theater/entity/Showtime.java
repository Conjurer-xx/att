package com.att.acceptance.movie_theater.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.AssertTrue;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Entity representing a Showtime in the movie theater system.
 * Maps to the database table for showtimes and includes relationships
 * with Movie, Theater, and associated Bookings.
 */
@Entity
@Table(
    name = "showtimes",
    uniqueConstraints = @UniqueConstraint(columnNames = {"theater_id", "start_time"})
)
public class Showtime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "movie_id", nullable = false)
    @NotNull(message = "Movie is required")
    private Movie movie;

    @ManyToOne
    @JoinColumn(name = "theater_id", nullable = false)
    @NotNull(message = "Theater is required")
    private Theater theater;

    @Column(name = "start_time", nullable = false)
    @NotNull(message = "Start time is required")
    private LocalDateTime startTime;

    @Column(name = "end_time", nullable = false)
    @NotNull(message = "End time is required")
    private LocalDateTime endTime;

    @OneToMany(mappedBy = "showtime", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Booking> bookings;

    @AssertTrue(message = "Start time must be before end time")
    public boolean isStartTimeBeforeEndTime() {
        return startTime != null && endTime != null && startTime.isBefore(endTime);
    }

    // Getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public Theater getTheater() {
        return theater;
    }

    public void setTheater(Theater theater) {
        this.theater = theater;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public List<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
    }
}