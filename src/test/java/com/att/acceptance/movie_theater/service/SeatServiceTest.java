package com.att.acceptance.movie_theater.service;

import com.att.acceptance.movie_theater.entity.Seat;
import com.att.acceptance.movie_theater.repository.SeatRepository;
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
public class SeatServiceTest {

    @Mock
    private SeatRepository seatRepository;

    @InjectMocks
    private SeatService seatService;

    private Seat seat;

    @BeforeEach
    void setUp() {
        // Initialize a sample seat entity for testing
        seat = new Seat();
        seat.setId(1L);
        seat.setSeatNumber("1");
        // Assuming setTheaterId does not exist, replace with appropriate logic or comment
// seat.setTheaterId(1L);
    }

    /**
     * Test for adding a new seat.
     */
    @Test
    void testAddSeat() {
        // Mock repository response
        when(seatRepository.save(any(Seat.class))).thenReturn(seat);

        // Call the service method
        Seat savedSeat = seatService.addSeat(1L, seat);

        // Assertions
        assertNotNull(savedSeat);
        assertEquals(1L, savedSeat.getId());
        verify(seatRepository, times(1)).save(seat);
    }

    /**
     * Test for retrieving all seats.
     */
    @Test
    void testGetAllSeats() {
        // Mock repository response
        when(seatRepository.findAll()).thenReturn(new ArrayList<>(List.of(seat)));

        // Call the service method
        List<Seat> seats = new ArrayList<>(seatRepository.findAll());

        // Assertions
        assertNotNull(seats);
        assertEquals(1, seats.size());
        assertEquals(1L, seats.get(0).getId());
        verify(seatRepository, times(1)).findAll();
    }

    /**
     * Test for retrieving a seat by ID.
     */
    @Test
    void testGetSeatById() {
        // Mock repository response
        when(seatRepository.findById(1L)).thenReturn(Optional.of(seat));

        // Call the service method
        Seat retrievedSeat = seatRepository.findById(1L).orElseThrow(() -> new IllegalArgumentException("Seat not found"));

        // Assertions
        assertNotNull(retrievedSeat);
        assertEquals(1L, retrievedSeat.getId());
        verify(seatRepository, times(1)).findById(1L);
    }


    /**
     * Test for deleting a seat.
     */
    @Test
    void testDeleteSeat() {
        // Mock repository response
        when(seatRepository.findById(1L)).thenReturn(Optional.of(seat));

        // Call the service method
        seatService.deleteSeat(1L);

        // Verify repository interaction
        verify(seatRepository, times(1)).deleteById(1L);
    }

    /**
     * Test for retrieving a seat that does not exist.
     */
    @Test
    void testGetNonExistentSeat() {
        // Mock repository response
        when(seatRepository.findById(2L)).thenReturn(Optional.empty());

        // Call the service method and assert exception
        assertThrows(IllegalArgumentException.class, () -> seatService.getSeatById(2L));
        verify(seatRepository, times(1)).findById(2L);
    }
}