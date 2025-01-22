package com.att.acceptance.movie_theater.entity;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import io.swagger.v3.oas.annotations.media.Schema;

@Entity
@Table(name = "movies")
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier of the Movie.", example = "1")
    private Long id;

    @NotBlank(message = "Title is required.")
    @Size(min = 3, max = 255, message = "Title must be between 3 and 255 characters")
    @Schema(description = "Title of the Movie.", example = "The Shawshank Redemption")
    private String title;

    @NotBlank(message = "Genre is required.")
    @Size(min = 3, max = 50, message = "Genre must be between 3 and 50 characters")
    private String genre;

    @NotNull(message = "Duration is required.")
    @Min(value = 1, message = "Duration must be at least 1 minute.")
    @Schema(description = "Duration of the Movie in minutes.", example = "142")
    private Integer duration; 

    @NotBlank(message = "Rating is required.")
    @Schema(description = "Rating of the Movie.", example = "R")
    private String rating; 

    @NotNull(message = "Release year is required.")
    @Min(value = 1900, message = "Release year must be after 1900.")  
    @Schema(description = "Release year of the Movie.", example = "1994")
    private Integer releaseYear; 

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL)
    @Schema(description = "List of Showtimes for the Movie.")
    private Set<Showtime> showtimes = new HashSet<>(); 

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public Integer getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(Integer releaseYear) {
        this.releaseYear = releaseYear;
    }

    public Set<Showtime> getShowtimes() {
        return showtimes;
    }

    public void setShowtimes(Set<Showtime> showtimes) {
        this.showtimes = showtimes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Movie movie = (Movie) o;
        return Objects.equals(id, movie.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", title=\'" + title + '\'' +
                ", genre=\'" + genre + '\'' +
                ", duration=" + duration +
                ", rating=\'" + rating + '\'' +
                ", releaseYear=" + releaseYear +
                '}';
    }
}