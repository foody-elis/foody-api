package com.example.foody.exceptions.booking;

import java.time.LocalDate;

/**
 * Exception thrown when a booking is not allowed.
 */
public class BookingNotAllowedException extends RuntimeException {

    /**
     * Constructs a new BookingNotAllowedException with a default error message.
     */
    public BookingNotAllowedException() {
        super("Booking not allowed.");
    }

    /**
     * Constructs a new BookingNotAllowedException with a detailed error message.
     *
     * @param restaurantId  the ID of the restaurant
     * @param date          the date of the booking
     * @param sittingTimeId the ID of the sitting time
     */
    public BookingNotAllowedException(long restaurantId, LocalDate date, long sittingTimeId) {
        super(String.format("Booking not allowed for restaurant with id = %s on %s at sitting time with id = %s.", restaurantId, date, sittingTimeId));
    }
}