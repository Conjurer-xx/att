package com.att.acceptance.movie_theater.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.att.acceptance.movie_theater.entity.Theater;
import com.att.acceptance.movie_theater.repository.TheaterRepository;

@ExtendWith(MockitoExtension.class)
public class TheaterServiceTest {

    @Mock
    private TheaterRepository theaterRepository;

    @InjectMocks
    private TheaterService theaterService;

    private Theater theater;

    @BeforeEach
    void setUp() {
        // Initialize a sample theater entity for testing
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
    void testAddTheater() {
        // Mock repository response
        when(theaterRepository.save(any(Theater.class))).thenReturn(theater);

        // Call the service method
        Theater savedTheater = theaterService.addTheater(theater);

        // Assertions
        assertNotNull(savedTheater);
        assertEquals("Test Theater", savedTheater.getName());
        verify(theaterRepository, times(1)).save(theater);
    }

    /**
     * Test for retrieving all theaters.
     */
    @Test
    void testGetAllTheaters() {
        // Mock repository response
        when(theaterRepository.findAll()).thenReturn(new ArrayList<>(List.of(theater)));

        // Call the service method
        List<Theater> theaters = new ArrayList<>(theaterService.getAllTheaters());

        // Assertions
        assertNotNull(theaters);
        assertEquals(1, theaters.size());
        assertEquals("Test Theater", theaters.get(0).getName());
        verify(theaterRepository, times(1)).findAll();
    }

    /**
     * Test for retrieving a theater by ID.
     */
    @Test
    void testGetTheaterById() {
        // Mock repository response
        when(theaterRepository.findById(1L)).thenReturn(Optional.of(theater));

        // Call the service method
        Theater retrievedTheater = theaterService.getTheaterById(1L);

        // Assertions
        assertNotNull(retrievedTheater);
        assertEquals(1L, retrievedTheater.getId());
        verify(theaterRepository, times(1)).findById(1L);
    }

    /**
     * Test for updating a theater.
     */
    @Test
    void testUpdateTheater() {
        // Mock repository response
        when(theaterRepository.findById(1L)).thenReturn(Optional.of(theater));
        when(theaterRepository.save(any(Theater.class))).thenReturn(theater);

        // Update theater details
        theater.setName("Updated Theater");

        // Call the service method
        Theater updatedTheater = theaterService.updateTheater(1L, theater);

        // Assertions
        assertNotNull(updatedTheater);
        assertEquals("Updated Theater", updatedTheater.getName());
        verify(theaterRepository, times(1)).save(theater);
    }

    /**
     * Test for deleting a theater.
     */
    @Test
    void testDeleteTheater() {
        // Mock repository response
        when(theaterRepository.findById(1L)).thenReturn(Optional.of(theater));

        // Call the service method
        theaterService.deleteTheater(1L);

        // Verify repository interaction
        verify(theaterRepository, times(1)).deleteById(1L);
    }

    /**
     * Test for retrieving a theater that does not exist.
     */
    @Test
    void testGetNonExistentTheater() {
        // Mock repository response
        when(theaterRepository.findById(2L)).thenReturn(Optional.empty());

        // Call the service method and assert exception
        assertThrows(IllegalArgumentException.class, () -> theaterService.getTheaterById(2L));
        verify(theaterRepository, times(1)).findById(2L);
    }
}