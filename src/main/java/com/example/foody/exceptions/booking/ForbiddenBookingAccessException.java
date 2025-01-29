package com.example.foody.exceptions.booking;

/**
 * Exception thrown when access to a booking is denied.
 */
public class ForbiddenBookingAccessException extends RuntimeException {

    /**
     * Constructs a new ForbiddenBookingAccessException with a default error message.
     */
    public ForbiddenBookingAccessException() {
        super("Booking access denied.");
    }
}