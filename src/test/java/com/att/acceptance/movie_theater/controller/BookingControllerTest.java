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

import com.att.acceptance.movie_theater.entity.Booking;
import com.att.acceptance.movie_theater.entity.Seat;
import com.att.acceptance.movie_theater.entity.Showtime;
import com.att.acceptance.movie_theater.entity.User;
import com.att.acceptance.movie_theater.service.BookingService;

@ExtendWith(MockitoExtension.class)
public class BookingControllerTest {

    @Mock
    private BookingService bookingService;

    @InjectMocks
    private BookingController bookingController;

    private MockMvc mockMvc;

    private Booking booking;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(bookingController).build();

        booking = new Booking();
        booking.setId(1L);
        User user = new User();
        user.setId(1L);
        user.setName("Test User");
        user.setEmail("test@example.com");
        booking.setUser(user); // Assuming User entity is properly initialized
        Showtime showtime = new Showtime();
        showtime.setId(1L);
        showtime.setStartTime(java.time.LocalDateTime.of(2023, 1, 1, 10, 0));
        showtime.setEndTime(java.time.LocalDateTime.of(2023, 1, 1, 12, 0));
        booking.setShowtime(showtime); // Assuming Showtime entity is properly initialized
        Seat seat = new Seat();
        seat.setId(1L);
        seat.setSeatNumber("A1");
        booking.setSeat(seat); // Assuming Seat entity is properly initialized
        booking.setPrice(15.50f);
    }

    /**
     * Test for adding a new booking.
     */
    @Test
    void testAddBooking() throws Exception {
        // Mock service response
        when(bookingService.createBooking(any(Booking.class))).thenReturn(booking);

        // Perform POST request
        mockMvc.perform(post("/api/bookings")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"userId\":1,\"showtimeId\":1,\"seatNumber\":\"A1\",\"price\":15.50}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.seatNumber").value("A1"));
    }

    /**
     * Test for retrieving a booking by ID.
     */
    @Test
    void testGetBookingById() throws Exception {
        // Mock service response
        when(bookingService.getBookingsByUser(1L)).thenReturn(Set.of(booking));

        // Perform GET request
        mockMvc.perform(get("/api/bookings/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.seatNumber").value("A1"));
    }

    /**
     * Test for retrieving all bookings.
     */
    @Test
    void testGetAllBookings() throws Exception {
        // Mock service response
        when(bookingService.getAllBookings()).thenReturn(Set.of(booking));

        // Perform GET request
        mockMvc.perform(get("/api/bookings")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userId").value(1))
                .andExpect(jsonPath("$[0].seatNumber").value("A1"));
    }

    /**
     * Test for updating a booking.
     */
    @Test
    void testUpdateBooking() throws Exception {
        // Mock service response
        when(bookingService.updateBooking(any(Long.class), any(Booking.class))).thenReturn(booking);

        // Perform PUT request
        mockMvc.perform(put("/api/bookings/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"userId\":1,\"showtimeId\":1,\"seatNumber\":\"A2\",\"price\":20.00}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.seatNumber").value("A1"));
    }

    /**
     * Test for deleting a booking.
     */
    @Test
    void testDeleteBooking() throws Exception {
        // Mock service response
        when(bookingService.getBookingsByUser(1L)).thenReturn(Set.of(booking));

        // Perform DELETE request
        mockMvc.perform(delete("/api/bookings/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}