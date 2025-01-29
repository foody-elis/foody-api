package com.example.foody.exceptions.booking;

/**
 * Exception thrown when the booking's sitting time is in the past.
 */
public class InvalidBookingSittingTimeException extends RuntimeException {

    /**
     * Constructs a new InvalidBookingSittingTimeException with a default error message.
     */
    public InvalidBookingSittingTimeException() {
        super("The booking's sitting time is in the past.");
    }
}