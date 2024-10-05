package com.example.foody.exceptions.sitting_time;

public class InvalidWeekDayException extends RuntimeException {
    public InvalidWeekDayException(int weekDay) {
        super(String.format("Invalid week day %d: must be between 1 and 7", weekDay));
    }
}
