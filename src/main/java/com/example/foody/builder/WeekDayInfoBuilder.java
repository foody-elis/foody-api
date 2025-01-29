package com.example.foody.builder;

import com.example.foody.model.Restaurant;
import com.example.foody.model.SittingTime;
import com.example.foody.model.WeekDayInfo;
import com.example.foody.utils.enums.SittingTimeStep;

import java.time.LocalTime;
import java.util.List;

/**
 * Interface for building {@link WeekDayInfo} objects.
 */
public interface WeekDayInfoBuilder {
    WeekDayInfoBuilder id(long id);
    WeekDayInfoBuilder weekDay(int weekDay);
    WeekDayInfoBuilder startLaunch(LocalTime startLaunch);
    WeekDayInfoBuilder endLaunch(LocalTime endLaunch);
    WeekDayInfoBuilder startDinner(LocalTime startDinner);
    WeekDayInfoBuilder endDinner(LocalTime endDinner);
    WeekDayInfoBuilder sittingTimeStep(SittingTimeStep sittingTimeStep);
    WeekDayInfoBuilder restaurant(Restaurant restaurant);
    WeekDayInfoBuilder sittingTimes(List<SittingTime> sittingTimes);
    WeekDayInfo build();
}