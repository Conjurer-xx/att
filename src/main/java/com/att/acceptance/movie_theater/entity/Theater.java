package com.att.acceptance.movie_theater.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.Set;

/**
 * Entity representing a Theater in the movie theater system.
 * Maps to the database table for theaters and includes relationships with showtimes and seat limits.
 */
@Entity
@Table(name = "theaters")
public class Theater {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Theater name is required.")
    @Size(max = 255, message = "Theater name must not exceed 255 characters.")
    @Column(nullable = false, unique = true)
    private String name;

    @NotBlank(message = "Location is required.")
    @Size(max = 500, message = "Location must not exceed 500 characters.")
    @Column(nullable = false)
    private String location;

    @NotNull(message = "Maximum number of seats is required.")
    @Min(value = 1, message = "Theater must have at least 1 seat.")
    @Column(nullable = false)
    private Integer maxSeats;

    @OneToMany(mappedBy = "theater", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Showtime> showtimes;

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getMaxSeats() {
        return maxSeats;
    }

    public void setMaxSeats(Integer maxSeats) {
        this.maxSeats = maxSeats;
    }

    public Set<Showtime> getShowtimes() {
        return showtimes;
    }

    public void setShowtimes(Set<Showtime> showtimes) {
        this.showtimes = showtimes;
    }
}