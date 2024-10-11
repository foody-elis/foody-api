package com.example.foody.exceptions.booking;

public class ForbiddenBookingAccessException extends RuntimeException {
    public ForbiddenBookingAccessException() {
        super("Booking access denied.");
    }
}
