package com.example.foody.builder;

import com.example.foody.model.Booking;
import com.example.foody.model.SittingTime;
import com.example.foody.model.WeekDayInfo;

import java.time.LocalTime;
import java.util.List;

/**
 * Interface for building {@link SittingTime} objects.
 */
public interface SittingTimeBuilder {
    SittingTimeBuilder id(long id);
    SittingTimeBuilder start(LocalTime start);
    SittingTimeBuilder end(LocalTime end);
    SittingTimeBuilder weekDayInfo(WeekDayInfo weekDayInfo);
    SittingTimeBuilder bookings(List<Booking> bookings);
    SittingTime build();
}