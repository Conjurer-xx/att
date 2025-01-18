package com.att.acceptance.movie_theater.service;

import com.att.acceptance.movie_theater.entity.Movie;
import com.att.acceptance.movie_theater.exception.MovieNotFoundException;
import com.att.acceptance.movie_theater.repository.MovieRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class MovieService {

    private final MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public Page<Movie> getAllMovies(Pageable pageable) {
        return movieRepository.findAll(pageable); 
    }

    @Transactional(readOnly = true)
    public Movie getMovieById(Long id) {
        Optional<Movie> movie = movieRepository.findById(id);
        return movie.orElseThrow(() -> new MovieNotFoundException("Movie not found with id: " + id));
    }

    @Transactional
    public Movie createMovie(Movie movie) {
        return movieRepository.save(movie);
    }

    @Transactional
    public Movie updateMovie(Long id, Movie movieDetails) {
        Movie movie = getMovieById(id); 
        movie.setTitle(movieDetails.getTitle());
        movie.setGenre(movieDetails.getGenre());
        movie.setDuration(movieDetails.getDuration());
        movie.setRating(movieDetails.getRating());
        movie.setReleaseYear(movieDetails.getReleaseYear());
        return movieRepository.save(movie);
    }

    @Transactional
    public void deleteMovie(Long id) {
        movieRepository.deleteById(id);
    }
}