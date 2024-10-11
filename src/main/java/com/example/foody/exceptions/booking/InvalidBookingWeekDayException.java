package com.example.foody.exceptions.booking;

public class InvalidBookingWeekDayException extends RuntimeException {
    public InvalidBookingWeekDayException() {
        super("The booking's week day does not match the sitting time's week day.");
    }
}
