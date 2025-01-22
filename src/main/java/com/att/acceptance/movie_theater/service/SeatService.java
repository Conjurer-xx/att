package com.att.acceptance.movie_theater.service;

import com.att.acceptance.movie_theater.entity.*;
import com.att.acceptance.movie_theater.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

/**
 * Service for managing seats.
 *
 * This service is responsible for adding, fetching, and deleting seats. The
 * number of seats in a theater is limited by the theater's maximum capacity.
 *
 */
@Service
public class SeatService {

    private final SeatRepository seatRepository;
    private final TheaterRepository theaterRepository;

    public SeatService(SeatRepository seatRepository, TheaterRepository theaterRepository) {
        this.seatRepository = seatRepository;
        this.theaterRepository = theaterRepository;
    }

    /**
     * Add a new seat to a theater.
     *
     * @param theaterId The ID of the theater.
     * @param seat The seat to add.
     * @return The added seat.
     */
    @Transactional
    public Seat addSeat(Long theaterId, Seat seat) {
        Theater theater = theaterRepository.findById(theaterId).orElseThrow(() ->
                new IllegalArgumentException("Theater with ID " + theaterId + " does not exist."));

        if (seatRepository.countSeatsByTheaterId(theaterId) >= theater.getMaxSeats()) {
            throw new IllegalArgumentException("Cannot add more seats. Theater has reached its maximum capacity.");
        }

        SeatAvailability seatAvailability = new SeatAvailability();
        seatAvailability.setSeat(seat);
        seatAvailability.setStatus(AvailabilityStatusEnum.AVAILABLE);
        seat.setSeatAvailability(seatAvailability);

        return seatRepository.save(seat);
    }

    /**
     * Fetch all seats for a specific theater.
     *
     * @param theaterId The ID of the theater.
     * @return A set of seats in the theater.
     */
    @Transactional(readOnly = true)
    public Set<Seat> getSeatsByTheater(Long theaterId) {
        theaterRepository.findById(theaterId).orElseThrow(() ->
                new IllegalArgumentException("Theater with ID " + theaterId + " does not exist."));

        return Set.copyOf(seatRepository.findByTheaterId(theaterId));
    }

    /**
     * Delete a seat by its ID.
     *
     * @param seatId The ID of the seat.
     */
    @Transactional
    public void deleteSeat(Long seatId) {
        seatRepository.findById(seatId).orElseThrow(() ->
                new IllegalArgumentException("Seat with ID " + seatId + " does not exist."));
        seatRepository.deleteById(seatId);
    }
    
	/**
	 * Fetch a seat by its ID.
	 *
	 * @param id The ID of the seat.
	 * @return The seat.
	 */
    @Transactional(readOnly = true)
    public Seat getSeatById(Long id) {
        return seatRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Seat not found with ID: " + id));
    }

}