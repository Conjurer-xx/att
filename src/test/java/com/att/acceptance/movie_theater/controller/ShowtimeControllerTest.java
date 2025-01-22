package com.att.acceptance.movie_theater.controller;

import com.att.acceptance.movie_theater.entity.Movie;
import com.att.acceptance.movie_theater.entity.Showtime;
import com.att.acceptance.movie_theater.entity.Theater;
import com.att.acceptance.movie_theater.service.ShowtimeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class ShowtimeControllerTest {

    @Mock
    private ShowtimeService showtimeService;

    @InjectMocks
    private ShowtimeController showtimeController;

    private MockMvc mockMvc;

    private Showtime showtime;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(showtimeController).build();

        showtime = new Showtime();
        showtime.setId(1L);
        final Movie movie = new Movie();
        movie.setId(1L);        
        movie.setTitle("Test Movie");
        movie.setGenre("Action");
        movie.setDuration(120);
        movie.setRating("PG-13");
        movie.setReleaseYear(2022);        
		showtime.setMovie(movie); 
        final Theater theater = new Theater();
        theater.setId(1L);
        theater.setName("Test Theater");
        theater.setLocation("Test Location");
        theater.setMaxSeats(200);        
		showtime.setTheater(theater);
        showtime.setStartTime(LocalDateTime.of(2023, 1, 1, 10, 0));
        showtime.setEndTime(LocalDateTime.of(2023, 1, 1, 12, 0));
    }

    /**
     * Test for adding a new showtime.
     */
    @Test
    void testAddShowtime() throws Exception {
        // Mock service response
        when(showtimeService.addShowtime(any(Showtime.class))).thenReturn(showtime);

        // Perform POST request
        mockMvc.perform(post("/api/showtimes")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"movieId\":1,\"theaterId\":1,\"startTime\":\"2023-01-01T10:00:00\",\"endTime\":\"2023-01-01T12:00:00\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.movieId").value(1))
                .andExpect(jsonPath("$.theaterId").value(1));
    }

    /**
     * Test for retrieving a showtime by ID.
     */
    @Test
    void testGetShowtimeById() throws Exception {
        // Mock service response
        when(showtimeService.findShowtimeById(1L)).thenReturn(showtime);

        // Perform GET request
        mockMvc.perform(get("/api/showtimes/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.movieId").value(1))
                .andExpect(jsonPath("$.theaterId").value(1));
    }

    /**
     * Test for updating a showtime.
     */
    @Test
    void testUpdateShowtime() throws Exception {
        // Mock service response
        when(showtimeService.updateShowtime(any(Long.class), any(Showtime.class))).thenReturn(showtime);

        // Perform PUT request
        mockMvc.perform(put("/api/showtimes/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"movieId\":1,\"theaterId\":1,\"startTime\":\"2023-01-01T14:00:00\",\"endTime\":\"2023-01-01T16:00:00\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.movieId").value(1))
                .andExpect(jsonPath("$.theaterId").value(1));
    }

    /**
     * Test for deleting a showtime.
     */
    @Test
    void testDeleteShowtime() throws Exception {
        // Mock service response
        when(showtimeService.findShowtimeById(1L)).thenReturn(showtime);

        // Perform DELETE request
        mockMvc.perform(delete("/api/showtimes/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}