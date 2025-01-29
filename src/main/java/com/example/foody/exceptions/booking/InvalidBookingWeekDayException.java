package com.example.foody.exceptions.booking;

/**
 * Exception thrown when the booking's week day does not match the sitting time's week day.
 */
public class InvalidBookingWeekDayException extends RuntimeException {

    /**
     * Constructs a new InvalidBookingWeekDayException with a default error message.
     */
    public InvalidBookingWeekDayException() {
        super("The booking's week day does not match the sitting time's week day.");
    }
}