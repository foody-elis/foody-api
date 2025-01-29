package com.example.foody.exceptions.booking;

/**
 * Exception thrown when the booking's restaurant does not match the sitting time's restaurant.
 */
public class InvalidBookingRestaurantException extends RuntimeException {

    /**
     * Constructs a new InvalidBookingRestaurantException with a default error message.
     */
    public InvalidBookingRestaurantException() {
        super("The booking's restaurant does not match the sitting time's restaurant.");
    }
}