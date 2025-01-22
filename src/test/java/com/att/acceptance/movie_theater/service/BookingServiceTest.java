package com.att.acceptance.movie_theater.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.att.acceptance.movie_theater.entity.Booking;
import com.att.acceptance.movie_theater.entity.User;
import com.att.acceptance.movie_theater.repository.BookingRepository;

@ExtendWith(MockitoExtension.class)
public class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @InjectMocks
    private BookingService bookingService;

    private Booking booking;

    @BeforeEach
    void setUp() {
        // Initialize a sample booking entity for testing
        User user = new User();
        user.setId(1L);

        booking = new Booking();
        booking.setId(1L);
        booking.setUser(user);
        booking.setPrice(10.0f);
    }

    /**
     * Test for retrieving all bookings.
     */
    @Test
    void testGetAllBookings() {
        // Mock repository response
        when(bookingRepository.findAll()).thenReturn(List.of(booking));

        // Call the service method
        Set<Booking> bookings = bookingService.getAllBookings();

        // Assertions
        assertNotNull(bookings);
        assertEquals(1, bookings.size());
        verify(bookingRepository, times(1)).findAll();
    }

    /**
     * Test for retrieving bookings by user ID.
     */
    @Test
    void testGetBookingsByUser() {
        // Mock repository response
        when(bookingRepository.findByUserId(1L)).thenReturn(Set.of(booking));

        // Call the service method
        Set<Booking> bookings = bookingService.getBookingsByUser(1L);

        // Assertions
        assertNotNull(bookings);
        assertEquals(1, bookings.size());
        verify(bookingRepository, times(1)).findByUserId(1L);
    }

    /**
     * Test for creating a new booking.
     */
    @Test
    void testCreateBooking() {
        // Mock repository response
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        // Call the service method
        Booking savedBooking = bookingService.createBooking(booking);

        // Assertions
        assertNotNull(savedBooking);
        assertEquals(1L, savedBooking.getId());
        verify(bookingRepository, times(1)).save(booking);
    }

    /**
     * Test for canceling a booking.
     */
    @Test
    void testCancelBooking() {
        // Call the service method
        bookingService.cancelBooking(1L);

        // Verify repository interaction
        verify(bookingRepository, times(1)).deleteById(1L);
    }

    /**
     * Test for canceling a booking for a specific user.
     */
    @Test
    void testCancelBookingForUser() {
        // Mock repository response
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));

        // Call the service method
        bookingService.cancelBookingForUser(1L, 1L);

        // Verify repository interaction
        verify(bookingRepository, times(1)).deleteById(1L);
    }

    /**
     * Test for canceling a booking for a user that doesn't own the booking.
     */
    @Test
    void testCancelBookingForUserUnauthorized() {
        // Mock repository response
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));

        // Call the service method with an unauthorized user
        assertThrows(SecurityException.class, () -> bookingService.cancelBookingForUser(1L, 2L));

        // Verify repository interaction
        verify(bookingRepository, never()).deleteById(anyLong());
    }
}