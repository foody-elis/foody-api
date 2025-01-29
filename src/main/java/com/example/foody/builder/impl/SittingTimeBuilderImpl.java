package com.example.foody.builder.impl;

import com.example.foody.builder.SittingTimeBuilder;
import com.example.foody.model.Booking;
import com.example.foody.model.SittingTime;
import com.example.foody.model.WeekDayInfo;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the {@link SittingTimeBuilder} interface.
 */
@Component
public class SittingTimeBuilderImpl implements SittingTimeBuilder {

    private long id;
    private LocalTime start;
    private LocalTime end;
    private WeekDayInfo weekDayInfo;
    private List<Booking> bookings = new ArrayList<>();

    @Override
    public SittingTimeBuilder id(long id) {
        this.id = id;
        return this;
    }

    @Override
    public SittingTimeBuilder start(LocalTime start) {
        this.start = start;
        return this;
    }

    @Override
    public SittingTimeBuilder end(LocalTime end) {
        this.end = end;
        return this;
    }

    @Override
    public SittingTimeBuilder weekDayInfo(WeekDayInfo weekDayInfo) {
        this.weekDayInfo = weekDayInfo;
        return this;
    }

    @Override
    public SittingTimeBuilder bookings(List<Booking> bookings) {
        this.bookings = bookings;
        return this;
    }

    @Override
    public SittingTime build() {
        return new SittingTime(id, start, end, weekDayInfo, bookings);
    }
}