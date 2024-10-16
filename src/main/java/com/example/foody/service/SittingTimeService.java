package com.example.foody.service;

import com.example.foody.dto.response.SittingTimeResponseDTO;
import com.example.foody.model.SittingTime;
import com.example.foody.model.WeekDayInfo;

import java.util.List;

public interface SittingTimeService {
    List<SittingTime> createForWeekDayInfo(WeekDayInfo weekDayInfo);
    List<SittingTimeResponseDTO> findAll();
    List<SittingTimeResponseDTO> findAllByRestaurantAndWeekDay(long restaurantId, int weekDay);
    List<SittingTimeResponseDTO> findAllByRestaurantAndWeekDayAndStartAfterNow(long restaurantId, int weekDay);
    boolean remove(long id);
}
