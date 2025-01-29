package com.example.foody.exceptions.booking;

/**
 * Exception thrown when the booking is in an invalid state.
 */
public class InvalidBookingStateException extends RuntimeException {

    /**
     * Constructs a new InvalidBookingStateException with the specified detail message.
     *
     * @param message the detail message
     */
    public InvalidBookingStateException(String message) {
        super(message);
    }
}