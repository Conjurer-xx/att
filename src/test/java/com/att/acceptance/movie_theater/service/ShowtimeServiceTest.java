package com.att.acceptance.movie_theater.service;

import com.att.acceptance.movie_theater.entity.Showtime;
import com.att.acceptance.movie_theater.repository.ShowtimeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ShowtimeServiceTest {

    @Mock
    private ShowtimeRepository showtimeRepository;

    @InjectMocks
    private ShowtimeService showtimeService;

    private Showtime showtime;

    @BeforeEach
    void setUp() {
        // Initialize a sample showtime entity for testing
        showtime = new Showtime();
        showtime.setId(1L);
        // Replace non-existent setMovieId and setTheaterId with actual entity properties or comments
        // showtime.setMovieId(1L); // Method does not exist
        // showtime.setTheaterId(1L); // Method does not exist
        showtime.setStartTime(LocalDateTime.of(2023, 1, 1, 10, 0));
        showtime.setEndTime(LocalDateTime.of(2023, 1, 1, 12, 0));
    }

    /**
     * Test for adding a new showtime.
     */
    @Test
    void testAddShowtime() {
        // Mock repository response
        when(showtimeRepository.save(any(Showtime.class))).thenReturn(showtime);

        // Call the service method
        Showtime savedShowtime = showtimeService.addShowtime(showtime);

        // Assertions
        assertNotNull(savedShowtime);
        assertEquals(1L, savedShowtime.getId());
        verify(showtimeRepository, times(1)).save(showtime);
    }

    /**
     * Test for retrieving all showtimes.
     */
    @Test
    void testGetAllShowtimes() {
        // Mock repository response
        when(showtimeRepository.findAll()).thenReturn(new ArrayList<>(List.of(showtime)));

        // Call the service method
        List<Showtime> showtimes = showtimeService.findAllShowtimes();

        // Assertions
        assertNotNull(showtimes);
        assertEquals(1, showtimes.size());
        assertEquals(1L, showtimes.get(0).getId());
        verify(showtimeRepository, times(1)).findAll();
    }

    /**
     * Test for retrieving a showtime by ID.
     */
    @Test
    void testGetShowtimeById() {
        // Mock repository response
        when(showtimeRepository.findById(1L)).thenReturn(Optional.of(showtime));

        // Call the service method
        Showtime retrievedShowtime = showtimeService.findShowtimeById(1L);

        // Assertions
        assertNotNull(retrievedShowtime);
        assertEquals(1L, retrievedShowtime.getId());
        verify(showtimeRepository, times(1)).findById(1L);
    }

    /**
     * Test for updating a showtime.
     */
    @Test
    void testUpdateShowtime() {
        // Mock repository response
        when(showtimeRepository.findById(1L)).thenReturn(Optional.of(showtime));
        when(showtimeRepository.save(any(Showtime.class))).thenReturn(showtime);

        // Update showtime details
        showtime.setEndTime(LocalDateTime.of(2023, 1, 1, 14, 0));

        // Call the service method
        Showtime updatedShowtime = showtimeService.updateShowtime(1L, showtime);

        // Assertions
        assertNotNull(updatedShowtime);
        assertEquals(LocalDateTime.of(2023, 1, 1, 14, 0), updatedShowtime.getEndTime());
        verify(showtimeRepository, times(1)).save(showtime);
    }

    /**
     * Test for deleting a showtime.
     */
    @Test
    void testDeleteShowtime() {
        // Mock repository response
        when(showtimeRepository.findById(1L)).thenReturn(Optional.of(showtime));

        // Call the service method
        showtimeService.removeShowtime(1L);

        // Verify repository interaction
        verify(showtimeRepository, times(1)).deleteById(1L);
    }

    /**
     * Test for retrieving a showtime that does not exist.
     */
    @Test
    void testGetNonExistentShowtime() {
        // Mock repository response
        when(showtimeRepository.findById(2L)).thenReturn(Optional.empty());

        // Call the service method and assert exception
        assertThrows(IllegalArgumentException.class, () -> showtimeService.findShowtimeById(2L));
        verify(showtimeRepository, times(1)).findById(2L);
    }
}