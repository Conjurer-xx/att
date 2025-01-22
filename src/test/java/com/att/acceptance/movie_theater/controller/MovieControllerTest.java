package com.att.acceptance.movie_theater.controller;

import com.att.acceptance.movie_theater.entity.Movie;
import com.att.acceptance.movie_theater.service.MovieService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class MovieControllerTest {

    @Mock
    private MovieService movieService;

    @InjectMocks
    private MovieController movieController;

    private MockMvc mockMvc;

    private Movie movie;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(movieController).build();

        movie = new Movie();
        movie.setId(1L);
        movie.setTitle("Test Movie");
        movie.setGenre("Action");
        movie.setDuration(120);
        movie.setRating("PG-13");
        movie.setReleaseYear(2022);
    }

    /**
     * Test for adding a new movie.
     */
    @Test
    void testAddMovie() throws Exception {
        // Mock service response
        when(movieService.addMovie(any(Movie.class))).thenReturn(movie);

        // Perform POST request
        mockMvc.perform(post("/api/movies")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"Test Movie\",\"genre\":\"Action\",\"duration\":120,\"rating\":\"PG-13\",\"releaseYear\":2022}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Test Movie"))
                .andExpect(jsonPath("$.genre").value("Action"));
    }

    /**
     * Test for retrieving a movie by ID.
     */
    @Test
    void testGetMovieById() throws Exception {
        // Mock service response
        when(movieService.getMovieById(1L)).thenReturn(movie);

        // Perform GET request
        mockMvc.perform(get("/api/movies/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Movie"))
                .andExpect(jsonPath("$.genre").value("Action"));
    }

    /**
     * Test for updating a movie.
     */
    @Test
    void testUpdateMovie() throws Exception {
        // Mock service response
        when(movieService.updateMovie(any(Long.class), any(Movie.class))).thenReturn(movie);

        // Perform PUT request
        mockMvc.perform(put("/api/movies/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"Updated Movie\",\"genre\":\"Drama\",\"duration\":150,\"rating\":\"R\",\"releaseYear\":2023}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Movie"));
    }

    /**
     * Test for deleting a movie.
     */
    @Test
    void testDeleteMovie() throws Exception {
        // Mock service response
        when(movieService.getMovieById(1L)).thenReturn(movie);

        // Perform DELETE request
        mockMvc.perform(delete("/api/movies/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}