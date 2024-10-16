package com.example.foody.mapper;

import com.example.foody.dto.response.SittingTimeResponseDTO;
import com.example.foody.model.SittingTime;

import java.util.List;

public interface SittingTimeMapper {
    SittingTimeResponseDTO sittingTimeToSittingTimeResponseDTO(SittingTime sittingTime);
    List<SittingTimeResponseDTO> sittingTimesToSittingTimeResponseDTOs(List<SittingTime> sittingTimes);
}
