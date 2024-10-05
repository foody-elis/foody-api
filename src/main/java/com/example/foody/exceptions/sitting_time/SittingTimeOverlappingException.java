package com.example.foody.exceptions.sitting_time;

import java.time.LocalTime;

public class SittingTimeOverlappingException extends RuntimeException {
    public SittingTimeOverlappingException() {
        super("The sitting time overlaps with another sitting time");
    }

    public SittingTimeOverlappingException(LocalTime startTime, LocalTime endTime) {
        super(String.format("The sitting time from %s to %s overlaps with another sitting time", startTime, endTime));
    }
}
