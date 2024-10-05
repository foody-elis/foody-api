package com.example.foody.service;

import com.example.foody.dto.request.SittingTimeRequestDTO;
import com.example.foody.dto.response.SittingTimeResponseDTO;

import java.util.List;

public interface SittingTimeService {
    SittingTimeResponseDTO save(SittingTimeRequestDTO sittingTimeDTO);
    List<SittingTimeResponseDTO> findAll();
    List<SittingTimeResponseDTO> findAllByRestaurant(long restaurantId);
    List<SittingTimeResponseDTO> findAllByRestaurantAndWeekDayAndStartTimeAfterNow(long restaurantId, int weekday);
    boolean remove(long id);
}
