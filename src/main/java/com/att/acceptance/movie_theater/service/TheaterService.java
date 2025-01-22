package com.att.acceptance.movie_theater.service;

import com.att.acceptance.movie_theater.entity.Seat;
import com.att.acceptance.movie_theater.entity.Theater;
import com.att.acceptance.movie_theater.repository.SeatRepository;
import com.att.acceptance.movie_theater.repository.TheaterRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
public class TheaterService {

    private final TheaterRepository theaterRepository;
    private final SeatRepository seatRepository;

    public TheaterService(TheaterRepository theaterRepository, SeatRepository seatRepository) {
        this.theaterRepository = theaterRepository;
        this.seatRepository = seatRepository;
    }

    /**
     * Add a new theater.
     *
     * @param theater The theater to add.
     * @return The added theater.
     */
    @Transactional
    public Theater addTheater(Theater theater) {
        return theaterRepository.save(theater);
    }

    /**
     * Add a new seat to a theater.
     *
     * @param theaterId The ID of the theater.
     * @param seat The seat to add.
     * @return The added seat.
     */
    @Transactional
    public Seat addSeatToTheater(Long theaterId, Seat seat) {
        Theater theater = theaterRepository.findById(theaterId).orElseThrow(() ->
                new IllegalArgumentException("Theater with ID " + theaterId + " does not exist."));

        if (seatRepository.countSeatsByTheaterId(theaterId) >= theater.getMaxSeats()) {
            throw new IllegalArgumentException("Cannot add more seats. Theater has reached its maximum capacity.");
        }

        seat.setTheater(theater);
        return seatRepository.save(seat);
    }

    /**
     * Get all seats for a specific theater.
     *
     * @param theaterId The theater ID.
     * @return A set of seats in the theater.
     */
    @Transactional(readOnly = true)
    public Set<Seat> getSeatsByTheater(Long theaterId) {
        theaterRepository.findById(theaterId).orElseThrow(() ->
                new IllegalArgumentException("Theater with ID " + theaterId + " does not exist."));

        return Set.copyOf(seatRepository.findByTheaterId(theaterId));
    }

    /**
     * Fetch all theaters.
     *
     * @return A set of all theaters.
     */
    @Transactional(readOnly = true)
    public Set<Theater> getAllTheaters() {
        return Set.copyOf(theaterRepository.findAll());
    }

    /**
     * Fetch a theater by its ID.
     *
     * @param theaterId The theater ID.
     * @return The theater.
     */
    @Transactional(readOnly = true)
    public Theater getTheaterById(Long theaterId) {
        return theaterRepository.findById(theaterId).orElseThrow(() ->
                new IllegalArgumentException("Theater with ID " + theaterId + " not found."));
    }

    /**
     * Delete a theater by its ID.
     *
     * @param theaterId The theater ID.
     */
    @Transactional
    public void deleteTheater(Long theaterId) {
        theaterRepository.deleteById(theaterId);
    }

    /**
     * Update a theater.
     *
     * @param theaterId The theater ID.
     * @param updatedTheater The updated theater details.
     * @return The updated theater.
     */
    @Transactional
    public Theater updateTheater(Long theaterId, Theater updatedTheater) {
        Theater existingTheater = theaterRepository.findById(theaterId).orElseThrow(() ->
                new IllegalArgumentException("Theater with ID " + theaterId + " not found."));

        existingTheater.setName(updatedTheater.getName());
        existingTheater.setLocation(updatedTheater.getLocation());
        existingTheater.setMaxSeats(updatedTheater.getMaxSeats());

        return theaterRepository.save(existingTheater);
    }
}