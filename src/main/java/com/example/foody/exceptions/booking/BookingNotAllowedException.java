package com.example.foody.exceptions.booking;

import java.time.LocalDate;

public class BookingNotAllowedException extends RuntimeException {
    public BookingNotAllowedException() {
        super("Booking not allowed.");
    }

    public BookingNotAllowedException(long restaurantId, LocalDate date, long sittingTimeId) {
        super(String.format("Booking not allowed for restaurant with id = %s on %s at sitting time with id = %s.", restaurantId, date, sittingTimeId));
    }
}
