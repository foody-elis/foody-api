package com.example.foody.exceptions.booking;

public class InvalidBookingSittingTimeException extends RuntimeException {
    public InvalidBookingSittingTimeException() {
        super("The booking's sitting time is in the past.");
    }
}