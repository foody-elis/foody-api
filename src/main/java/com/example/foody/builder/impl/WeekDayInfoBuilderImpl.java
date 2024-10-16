package com.example.foody.builder.impl;

import com.example.foody.builder.WeekDayInfoBuilder;
import com.example.foody.model.Restaurant;
import com.example.foody.model.SittingTime;
import com.example.foody.model.WeekDayInfo;
import com.example.foody.utils.SittingTimeStep;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class WeekDayInfoBuilderImpl implements WeekDayInfoBuilder {
    private long id;
    private int weekDay;
    private LocalTime startLaunch;
    private LocalTime endLaunch;
    private LocalTime startDinner;
    private LocalTime endDinner;
    private SittingTimeStep sittingTimeStep;
    private Restaurant restaurant;
    private List<SittingTime> sittingTimes = new ArrayList<>();


    @Override
    public WeekDayInfoBuilder id(long id) {
        this.id = id;
        return this;
    }

    @Override
    public WeekDayInfoBuilder weekDay(int weekDay) {
        this.weekDay = weekDay;
        return this;
    }

    @Override
    public WeekDayInfoBuilder startLaunch(LocalTime startLaunch) {
        this.startLaunch = startLaunch;
        return this;
    }

    @Override
    public WeekDayInfoBuilder endLaunch(LocalTime endLaunch) {
        this.endLaunch = endLaunch;
        return this;
    }

    @Override
    public WeekDayInfoBuilder startDinner(LocalTime startDinner) {
        this.startDinner = startDinner;
        return this;
    }

    @Override
    public WeekDayInfoBuilder endDinner(LocalTime endDinner) {
        this.endDinner = endDinner;
        return this;
    }

    @Override
    public WeekDayInfoBuilder sittingTimeStep(SittingTimeStep sittingTimeStep) {
        this.sittingTimeStep = sittingTimeStep;
        return this;
    }

    @Override
    public WeekDayInfoBuilder restaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
        return this;
    }

    @Override
    public WeekDayInfoBuilder sittingTimes(List<SittingTime> sittingTimes) {
        this.sittingTimes = sittingTimes;
        return this;
    }

    @Override
    public WeekDayInfo build() {
        return new WeekDayInfo(
                id,
                weekDay,
                startLaunch,
                endLaunch,
                startDinner,
                endDinner,
                sittingTimeStep,
                restaurant,
                sittingTimes
        );
    }
}