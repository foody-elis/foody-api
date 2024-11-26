package com.example.foody.service;

import com.example.foody.dto.request.WeekDayInfoRequestDTO;
import com.example.foody.dto.response.WeekDayInfoResponseDTO;

import java.util.List;

public interface WeekDayInfoService {
    WeekDayInfoResponseDTO save(WeekDayInfoRequestDTO weekDayInfoRequestDTO);
    List<WeekDayInfoResponseDTO> findAll();
    List<WeekDayInfoResponseDTO> findAllByRestaurant(long restaurantId);
    boolean remove(long id);
}
