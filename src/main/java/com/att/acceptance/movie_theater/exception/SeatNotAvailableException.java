package com.att.acceptance.movie_theater.exception;

public class SeatNotAvailableException extends RuntimeException {

	/**
	 * 
	 */
	public SeatNotAvailableException() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public SeatNotAvailableException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 */
	public SeatNotAvailableException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 */
	public SeatNotAvailableException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public SeatNotAvailableException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}
	
	

}
