package com.att.acceptance.movie_theater.service;

import com.att.acceptance.movie_theater.entity.Movie;
import com.att.acceptance.movie_theater.repository.MovieRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MovieServiceTest {

    @Mock
    private MovieRepository movieRepository;

    @InjectMocks
    private MovieService movieService;

    private Movie movie;

    @BeforeEach
    void setUp() {
        // Initialize a sample movie entity for testing
        movie = new Movie();
        movie.setId(1L);
        movie.setTitle("Test Movie");
        movie.setGenre("Drama");
        movie.setDuration(120);
        movie.setRating("4.5");
        movie.setReleaseYear(2022);
    }

    /**
     * Test for adding a new movie.
     */
    @Test
    void testAddMovie() {
        // Mock repository response
        when(movieRepository.save(any(Movie.class))).thenReturn(movie);

        // Call the service method
        Movie savedMovie = movieService.addMovie(movie);

        // Assertions
        assertNotNull(savedMovie);
        assertEquals("Test Movie", savedMovie.getTitle());
        verify(movieRepository, times(1)).save(movie);
    }

    /**
     * Test for retrieving all movies.
     */
    @Test
    void testGetAllMovies() {
        // Mock repository response
        when(movieRepository.findAll()).thenReturn(new ArrayList<>(List.of(movie)));

        // Call the service method
        List<Movie> movies = new ArrayList<>(movieService.getAllMovies());

        // Assertions
        assertNotNull(movies);
        assertEquals(1, movies.size());
        assertEquals("Test Movie", movies.get(0).getTitle());
        verify(movieRepository, times(1)).findAll();
    }

    /**
     * Test for retrieving a movie by ID.
     */
    @Test
    void testGetMovieById() {
        // Mock repository response
        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));

        // Call the service method
        Movie retrievedMovie = movieService.getMovieById(1L);

        // Assertions
        assertNotNull(retrievedMovie);
        assertEquals(1L, retrievedMovie.getId());
        verify(movieRepository, times(1)).findById(1L);
    }

    /**
     * Test for updating a movie.
     */
    @Test
    void testUpdateMovie() {
        // Mock repository response
        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));
        when(movieRepository.save(any(Movie.class))).thenReturn(movie);

        // Update movie details
        movie.setTitle("Updated Movie");

        // Call the service method
        Movie updatedMovie = movieService.updateMovie(1L, movie);

        // Assertions
        assertNotNull(updatedMovie);
        assertEquals("Updated Movie", updatedMovie.getTitle());
        verify(movieRepository, times(1)).save(movie);
    }

    /**
     * Test for deleting a movie.
     */
    @Test
    void testDeleteMovie() {
        // Mock repository response
        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));

        // Call the service method
        movieService.deleteMovie(1L);

        // Verify repository interaction
        verify(movieRepository, times(1)).deleteById(1L);
    }

    /**
     * Test for retrieving a movie that does not exist.
     */
    @Test
    void testGetNonExistentMovie() {
        // Mock repository response
        when(movieRepository.findById(2L)).thenReturn(Optional.empty());

        // Call the service method and assert exception
        assertThrows(IllegalArgumentException.class, () -> movieService.getMovieById(2L));
        verify(movieRepository, times(1)).findById(2L);
    }
}