package com.att.acceptance.movie_theater;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * Main application class for the Movie Ticket Booking System.
 * This application provides a RESTful API for managing movies, showtimes,
 * users, and ticket bookings.
 *
 * Features include:
 * - Movie Management: Add, update, delete, and fetch movie details.
 * - Showtime Management: Manage showtimes with constraints on overlaps.
 * - User Management: Register, authenticate users, and role-based access.
 * - Ticket Booking: Book tickets and track booking details.
 * 
 * */
@SpringBootApplication
public class MovieTheaterApplication {

	public static void main(String[] args) {
		SpringApplication.run(MovieTheaterApplication.class, args);
	}

}
