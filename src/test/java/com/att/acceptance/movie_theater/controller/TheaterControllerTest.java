package com.att.acceptance.movie_theater.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.att.acceptance.movie_theater.entity.Theater;
import com.att.acceptance.movie_theater.service.TheaterService;

@ExtendWith(MockitoExtension.class)
public class TheaterControllerTest {

    @Mock
    private TheaterService theaterService;

    @InjectMocks
    private TheaterController theaterController;

    private MockMvc mockMvc;

    private Theater theater;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(theaterController).build();

        theater = new Theater();
        theater.setId(1L);
        theater.setName("Test Theater");
        theater.setLocation("Test Location");
        theater.setMaxSeats(200);
    }

    /**
     * Test for adding a new theater.
     */
    @Test
    void testAddTheater() throws Exception {
        // Mock service response
        when(theaterService.addTheater(any(Theater.class))).thenReturn(theater);

        // Perform POST request
        mockMvc.perform(post("/api/theaters")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Test Theater\",\"location\":\"Test Location\",\"maxSeats\":200}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Test Theater"))
                .andExpect(jsonPath("$.location").value("Test Location"));
    }

    /**
     * Test for retrieving a theater by ID.
     */
    @Test
    void testGetTheaterById() throws Exception {
        // Mock service response
        when(theaterService.getTheaterById(1L)).thenReturn(theater);

        // Perform GET request
        mockMvc.perform(get("/api/theaters/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Theater"))
                .andExpect(jsonPath("$.location").value("Test Location"));
    }

    /**
     * Test for retrieving all theaters.
     */
    @Test
    void testGetAllTheaters() throws Exception {
        // Mock service response
        when(theaterService.getAllTheaters()).thenReturn(Set.of(theater));

        // Perform GET request
        mockMvc.perform(get("/api/theaters")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Test Theater"))
                .andExpect(jsonPath("$[0].location").value("Test Location"));
    }

    /**
     * Test for updating a theater.
     */
    @Test
    void testUpdateTheater() throws Exception {
        // Mock service response
        when(theaterService.updateTheater(any(Long.class), any(Theater.class))).thenReturn(theater);

        // Perform PUT request
        mockMvc.perform(put("/api/theaters/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Updated Theater\",\"location\":\"Updated Location\",\"maxSeats\":250}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Theater"));
    }

    /**
     * Test for deleting a theater.
     */
    @Test
    void testDeleteTheater() throws Exception {
        // Mock service response
        when(theaterService.getTheaterById(1L)).thenReturn(theater);

        // Perform DELETE request
        mockMvc.perform(delete("/api/theaters/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}