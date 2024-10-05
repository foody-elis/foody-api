package com.example.foody.builder;

import com.example.foody.model.Restaurant;
import com.example.foody.model.SittingTime;

import java.time.LocalTime;

public interface SittingTimeBuilder {
    SittingTimeBuilder id(long id);
    SittingTimeBuilder weekDay(int weekDay);
    SittingTimeBuilder startTime(LocalTime startTime);
    SittingTimeBuilder endTime(LocalTime endTime);
    SittingTimeBuilder restaurant(Restaurant restaurant);
    SittingTime build();
}
