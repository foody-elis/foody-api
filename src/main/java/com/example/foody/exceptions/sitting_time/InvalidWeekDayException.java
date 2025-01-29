package com.example.foody.exceptions.sitting_time;

/**
 * Exception thrown when an invalid week day is provided.
 */
public class InvalidWeekDayException extends RuntimeException {

    /**
     * Constructs a new InvalidWeekDayException with a formatted message indicating the invalid week day.
     *
     * @param weekDay the invalid week day that caused the exception
     */
    public InvalidWeekDayException(int weekDay) {
        super(String.format("Invalid week day %d: must be between 1 and 7.", weekDay));
    }
}