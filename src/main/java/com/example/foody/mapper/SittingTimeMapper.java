package com.example.foody.mapper;

import com.example.foody.dto.response.SittingTimeResponseDTO;
import com.example.foody.model.SittingTime;

import java.util.List;

/**
 * Mapper interface for converting between SittingTime entities and DTOs.
 */
public interface SittingTimeMapper {

    /**
     * Converts a SittingTime entity to a SittingTimeResponseDTO.
     *
     * @param sittingTime the SittingTime entity to convert
     * @return the converted SittingTimeResponseDTO
     */
    SittingTimeResponseDTO sittingTimeToSittingTimeResponseDTO(SittingTime sittingTime);

    /**
     * Converts a list of SittingTime entities to a list of SittingTimeResponseDTOs.
     *
     * @param sittingTimes the list of SittingTime entities to convert
     * @return the list of converted SittingTimeResponseDTOs
     */
    List<SittingTimeResponseDTO> sittingTimesToSittingTimeResponseDTOs(List<SittingTime> sittingTimes);
}