package com.example.foody.builder.impl;

import com.example.foody.builder.SittingTimeBuilder;
import com.example.foody.model.Restaurant;
import com.example.foody.model.SittingTime;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

@Component
public class SittingTimeBuilderImpl implements SittingTimeBuilder {
    private long id;
    private int weekDay;
    private LocalTime startTime;
    private LocalTime endTime;
    private Restaurant restaurant;

    @Override
    public SittingTimeBuilder id(long id) {
        this.id = id;
        return this;
    }

    @Override
    public SittingTimeBuilder weekDay(int weekDay) {
        this.weekDay = weekDay;
        return this;
    }

    @Override
    public SittingTimeBuilder startTime(LocalTime startTime) {
        this.startTime = startTime;
        return this;
    }

    @Override
    public SittingTimeBuilder endTime(LocalTime endTime) {
        this.endTime = endTime;
        return this;
    }

    @Override
    public SittingTimeBuilder restaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
        return this;
    }

    @Override
    public SittingTime build() {
        return new SittingTime(id, weekDay, startTime, endTime, restaurant);
    }
}
