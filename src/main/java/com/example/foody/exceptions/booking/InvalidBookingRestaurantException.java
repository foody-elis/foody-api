package com.example.foody.exceptions.booking;

public class InvalidBookingRestaurantException extends RuntimeException {
    public InvalidBookingRestaurantException() {
        super("The booking's restaurant does not match the sitting time's restaurant.");
    }
}
