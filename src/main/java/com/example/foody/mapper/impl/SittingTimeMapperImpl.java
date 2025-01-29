package com.example.foody.mapper.impl;

import com.example.foody.dto.response.SittingTimeResponseDTO;
import com.example.foody.mapper.SittingTimeMapper;
import com.example.foody.model.SittingTime;
import com.example.foody.model.WeekDayInfo;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the {@link SittingTimeMapper} interface.
 * <p>
 * Provides methods to convert between {@link SittingTime} entities and DTOs.
 */
@Component
public class SittingTimeMapperImpl implements SittingTimeMapper {

    /**
     * {@inheritDoc}
     * <p>
     * Converts a {@link SittingTime} entity to a {@link SittingTimeResponseDTO}.
     *
     * @param sittingTime the SittingTime entity to convert
     * @return the converted SittingTimeResponseDTO
     */
    @Override
    public SittingTimeResponseDTO sittingTimeToSittingTimeResponseDTO(SittingTime sittingTime) {
        if (sittingTime == null) {
            return null;
        }

        SittingTimeResponseDTO sittingTimeResponseDTO = new SittingTimeResponseDTO();

        sittingTimeResponseDTO.setWeekDayInfoId(sittingTimeWeekDayInfoId(sittingTime));
        sittingTimeResponseDTO.setId(sittingTime.getId());
        sittingTimeResponseDTO.setStart(sittingTime.getStart());
        sittingTimeResponseDTO.setEnd(sittingTime.getEnd());

        return sittingTimeResponseDTO;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Converts a list of {@link SittingTime} entities to a list of {@link SittingTimeResponseDTO} objects.
     *
     * @param sittingTimes the list of SittingTime entities to convert
     * @return the list of converted SittingTimeResponseDTO objects
     */
    @Override
    public List<SittingTimeResponseDTO> sittingTimesToSittingTimeResponseDTOs(List<SittingTime> sittingTimes) {
        if (sittingTimes == null) {
            return null;
        }

        List<SittingTimeResponseDTO> list = new ArrayList<>(sittingTimes.size());
        sittingTimes.forEach(sittingTime -> list.add(sittingTimeToSittingTimeResponseDTO(sittingTime)));

        return list;
    }

    /**
     * Retrieves the week day info ID from a {@link SittingTime} entity.
     *
     * @param sittingTime the SittingTime entity
     * @return the week day info ID, or null if not available
     */
    private Long sittingTimeWeekDayInfoId(SittingTime sittingTime) {
        if (sittingTime == null) {
            return null;
        }
        WeekDayInfo weekDayInfo = sittingTime.getWeekDayInfo();
        if (weekDayInfo == null) {
            return null;
        }
        return weekDayInfo.getId();
    }
}