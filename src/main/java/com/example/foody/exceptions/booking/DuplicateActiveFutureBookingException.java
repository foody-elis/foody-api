package com.example.foody.exceptions.booking;

import java.time.LocalDate;

/**
 * Exception thrown when there is a duplicate active future booking.
 */
public class DuplicateActiveFutureBookingException extends RuntimeException {

    /**
     * Constructs a new DuplicateActiveFutureBookingException with a detailed error message.
     *
     * @param restaurantId  the ID of the restaurant
     * @param date          the date of the booking
     * @param sittingTimeId the ID of the sitting time
     */
    public DuplicateActiveFutureBookingException(long restaurantId, LocalDate date, long sittingTimeId) {
        super(
                String.format(
                        "Duplicate active future booking for restaurant with id = %s on %s at sitting time with id = %s.",
                        restaurantId,
                        date,
                        sittingTimeId
                )
        );
    }
}