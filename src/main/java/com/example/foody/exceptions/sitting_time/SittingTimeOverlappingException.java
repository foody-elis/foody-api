package com.example.foody.exceptions.sitting_time;

import java.time.LocalTime;

/**
 * Exception thrown when there is an overlap in sitting times.
 */
public class SittingTimeOverlappingException extends RuntimeException {

    /**
     * Constructs a new SittingTimeOverlappingException with a default message indicating that the sitting time overlaps with another sitting time.
     */
    public SittingTimeOverlappingException() {
        super("The sitting time overlaps with another sitting time.");
    }

    /**
     * Constructs a new SittingTimeOverlappingException with a formatted message indicating the start and end times of the overlapping sitting time.
     *
     * @param startTime the start time of the overlapping sitting time
     * @param endTime   the end time of the overlapping sitting time
     */
    public SittingTimeOverlappingException(LocalTime startTime, LocalTime endTime) {
        super(String.format("The sitting time from %s to %s overlaps with another sitting time.", startTime, endTime));
    }
}