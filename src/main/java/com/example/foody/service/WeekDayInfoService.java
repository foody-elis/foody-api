package com.example.foody.service;

import com.example.foody.dto.request.WeekDayInfoRequestDTO;
import com.example.foody.dto.response.WeekDayInfoResponseDTO;

import java.util.List;

public interface WeekDayInfoService {
    WeekDayInfoResponseDTO save(WeekDayInfoRequestDTO weekDayInfoRequestDTO);
    List<WeekDayInfoResponseDTO> findAll();
    boolean remove(long id);
}
