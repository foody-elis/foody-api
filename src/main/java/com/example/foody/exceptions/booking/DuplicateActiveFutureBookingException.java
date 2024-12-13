package com.example.foody.exceptions.booking;

import java.time.LocalDate;

public class DuplicateActiveFutureBookingException extends RuntimeException {
    public DuplicateActiveFutureBookingException(long restaurantId, LocalDate date, long sittingTimeId) {
        super(String.format("Duplicate active future booking for restaurant with id = %s on %s at sitting time with id = %s.", restaurantId, date, sittingTimeId));
    }
}
