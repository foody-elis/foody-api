package com.example.foody.builder;

import com.example.foody.model.SittingTime;
import com.example.foody.model.WeekDayInfo;

import java.time.LocalTime;

public interface SittingTimeBuilder {
    SittingTimeBuilder id(long id);
    SittingTimeBuilder start(LocalTime start);
    SittingTimeBuilder end(LocalTime end);
    SittingTimeBuilder weekDayInfo(WeekDayInfo weekDayInfo);
    SittingTime build();
}
