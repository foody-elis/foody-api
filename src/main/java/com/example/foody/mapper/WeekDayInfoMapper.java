package com.example.foody.mapper;

import com.example.foody.dto.request.WeekDayInfoRequestDTO;
import com.example.foody.dto.request.WeekDayInfoUpdateRequestDTO;
import com.example.foody.dto.response.WeekDayInfoResponseDTO;
import com.example.foody.model.WeekDayInfo;

import java.util.List;

/**
 * Mapper interface for converting between WeekDayInfo entities and DTOs.
 */
public interface WeekDayInfoMapper {

    /**
     * Converts a WeekDayInfo entity to a WeekDayInfoResponseDTO.
     *
     * @param weekDayInfo the WeekDayInfo entity to convert
     * @return the converted WeekDayInfoResponseDTO
     */
    WeekDayInfoResponseDTO weekDayInfoToWeekDayInfoResponseDTO(WeekDayInfo weekDayInfo);

    /**
     * Converts a WeekDayInfoRequestDTO to a WeekDayInfo entity.
     *
     * @param weekDayInfoRequestDTO the WeekDayInfoRequestDTO to convert
     * @return the converted WeekDayInfo entity
     */
    WeekDayInfo weekDayInfoRequestDTOToWeekDayInfo(WeekDayInfoRequestDTO weekDayInfoRequestDTO);

    /**
     * Updates a WeekDayInfo entity from a WeekDayInfoUpdateRequestDTO.
     *
     * @param weekDayInfo the WeekDayInfo entity to update
     * @param weekDayInfoUpdateRequestDTO the WeekDayInfoUpdateRequestDTO with updated information
     */
    void updateWeekDayInfoFromWeekDayInfoUpdateRequestDTO(
            WeekDayInfo weekDayInfo,
            WeekDayInfoUpdateRequestDTO weekDayInfoUpdateRequestDTO
    );

    /**
     * Converts a list of WeekDayInfo entities to a list of WeekDayInfoResponseDTOs.
     *
     * @param weekDayInfos the list of WeekDayInfo entities to convert
     * @return the list of converted WeekDayInfoResponseDTOs
     */
    List<WeekDayInfoResponseDTO> weekDayInfosToWeekDayInfoResponseDTOs(List<WeekDayInfo> weekDayInfos);
}