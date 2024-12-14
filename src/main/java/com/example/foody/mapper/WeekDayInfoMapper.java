package com.example.foody.mapper;

import com.example.foody.dto.request.WeekDayInfoRequestDTO;
import com.example.foody.dto.request.WeekDayInfoUpdateRequestDTO;
import com.example.foody.dto.response.WeekDayInfoResponseDTO;
import com.example.foody.model.WeekDayInfo;

import java.util.List;

public interface WeekDayInfoMapper {
    WeekDayInfoResponseDTO weekDayInfoToWeekDayInfoResponseDTO(WeekDayInfo weekDayInfo);
    WeekDayInfo weekDayInfoRequestDTOToWeekDayInfo(WeekDayInfoRequestDTO weekDayInfoRequestDTO);
    void updateWeekDayInfoFromWeekDayInfoUpdateRequestDTO(WeekDayInfo weekDayInfo, WeekDayInfoUpdateRequestDTO weekDayInfoUpdateRequestDTO);
    List<WeekDayInfoResponseDTO> weekDayInfosToWeekDayInfoResponseDTOs(List<WeekDayInfo> weekDayInfos);
}
