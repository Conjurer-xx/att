package com.att.acceptance.movie_theater.service;

import com.att.acceptance.movie_theater.entity.Movie;
import com.att.acceptance.movie_theater.repository.MovieRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

/**
 * Service for managing movies.
 *
 * This service is responsible for adding, fetching, updating, and deleting
 * movies.
 */
@Service
public class MovieService {

    private final MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    /**
     * Add a new movie.
     *
     * @param movie The movie to add.
     * @return The added movie.
     */
    @Transactional
    public Movie addMovie(Movie movie) {
        return movieRepository.save(movie);
    }

    /**
     * Fetch all movies with pagination.
     *
     * @param pageable Pagination details.
     * @return A page of movies.
     */
    @Transactional(readOnly = true)
    public Page<Movie> getAllMovies(Pageable pageable) {
        return movieRepository.findAll(pageable);
    }

    /**
     * Fetch all movies without pagination.
     *
     * @return A set of all movies.
     */
    @Transactional(readOnly = true)
    public Set<Movie> getAllMovies() {
        return Set.copyOf(movieRepository.findAll());
    }

    /**
     * Fetch a movie by its ID.
     *
     * @param movieId The movie ID.
     * @return The movie.
     */
    @Transactional(readOnly = true)
    public Movie getMovieById(Long movieId) {
        return movieRepository.findById(movieId).orElseThrow(() ->
                new IllegalArgumentException("Movie with ID " + movieId + " not found."));
    }

    /**
     * Update an existing movie.
     *
     * @param movieId The ID of the movie to update.
     * @param updatedMovie The updated movie details.
     * @return The updated movie.
     */
    @Transactional
    public Movie updateMovie(Long movieId, Movie updatedMovie) {
        Movie existingMovie = movieRepository.findById(movieId).orElseThrow(() ->
                new IllegalArgumentException("Movie with ID " + movieId + " not found."));

        existingMovie.setTitle(updatedMovie.getTitle());
        existingMovie.setGenre(updatedMovie.getGenre());
        existingMovie.setDuration(updatedMovie.getDuration());
        existingMovie.setRating(updatedMovie.getRating());
        existingMovie.setReleaseYear(updatedMovie.getReleaseYear());

        return movieRepository.save(existingMovie);
    }

    /**
     * Delete a movie by its ID.
     *
     * @param movieId The ID of the movie to delete.
     */
    @Transactional
    public void deleteMovie(Long movieId) {
        movieRepository.findById(movieId).orElseThrow(() ->
                new IllegalArgumentException("Movie with ID " + movieId + " does not exist."));
        movieRepository.deleteById(movieId);
    }
}