package com.example.foody.mapper.impl;

import com.example.foody.builder.WeekDayInfoBuilder;
import com.example.foody.dto.request.WeekDayInfoRequestDTO;
import com.example.foody.dto.request.WeekDayInfoUpdateRequestDTO;
import com.example.foody.dto.response.WeekDayInfoResponseDTO;
import com.example.foody.mapper.WeekDayInfoMapper;
import com.example.foody.model.Restaurant;
import com.example.foody.model.WeekDayInfo;
import com.example.foody.utils.enums.SittingTimeStep;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the {@link WeekDayInfoMapper} interface.
 * <p>
 * Provides methods to convert between {@link WeekDayInfo} entities and DTOs.
 */
@Component
@AllArgsConstructor
public class WeekDayInfoMapperImpl implements WeekDayInfoMapper {

    private final WeekDayInfoBuilder weekDayInfoBuilder;

    /**
     * {@inheritDoc}
     * <p>
     * Converts a {@link WeekDayInfo} entity to a {@link WeekDayInfoResponseDTO}.
     *
     * @param weekDayInfo the WeekDayInfo entity to convert
     * @return the converted WeekDayInfoResponseDTO
     */
    @Override
    public WeekDayInfoResponseDTO weekDayInfoToWeekDayInfoResponseDTO(WeekDayInfo weekDayInfo) {
        if (weekDayInfo == null) {
            return null;
        }

        WeekDayInfoResponseDTO weekDayInfoResponseDTO = new WeekDayInfoResponseDTO();

        weekDayInfoResponseDTO.setRestaurantId(weekDayInfoRestaurantId(weekDayInfo));
        weekDayInfoResponseDTO.setId(weekDayInfo.getId());
        weekDayInfoResponseDTO.setWeekDay(weekDayInfo.getWeekDay());
        weekDayInfoResponseDTO.setStartLaunch(weekDayInfo.getStartLaunch());
        weekDayInfoResponseDTO.setEndLaunch(weekDayInfo.getEndLaunch());
        weekDayInfoResponseDTO.setStartDinner(weekDayInfo.getStartDinner());
        weekDayInfoResponseDTO.setEndDinner(weekDayInfo.getEndDinner());
        if (weekDayInfo.getSittingTimeStep() != null) {
            weekDayInfoResponseDTO.setSittingTimeStep(weekDayInfo.getSittingTimeStep().name());
        }

        return weekDayInfoResponseDTO;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Converts a {@link WeekDayInfoRequestDTO} to a {@link WeekDayInfo} entity.
     *
     * @param weekDayInfoRequestDTO the WeekDayInfoRequestDTO to convert
     * @return the converted WeekDayInfo entity
     */
    @Override
    public WeekDayInfo weekDayInfoRequestDTOToWeekDayInfo(WeekDayInfoRequestDTO weekDayInfoRequestDTO) {
        if (weekDayInfoRequestDTO == null) {
            return null;
        }

        WeekDayInfoBuilder builder = weekDayInfoBuilder
                .startLaunch(weekDayInfoRequestDTO.getStartLaunch())
                .endLaunch(weekDayInfoRequestDTO.getEndLaunch())
                .startDinner(weekDayInfoRequestDTO.getStartDinner())
                .endDinner(weekDayInfoRequestDTO.getEndDinner());

        if (weekDayInfoRequestDTO.getWeekDay() != null) {
            builder.weekDay(weekDayInfoRequestDTO.getWeekDay());
        }

        if (weekDayInfoRequestDTO.getSittingTimeStep() != null) {
            builder.sittingTimeStep(Enum.valueOf(SittingTimeStep.class, weekDayInfoRequestDTO.getSittingTimeStep()));
        }

        return builder.build();
    }

    /**
     * {@inheritDoc}
     * <p>
     * Updates a {@link WeekDayInfo} entity from a {@link WeekDayInfoUpdateRequestDTO}.
     *
     * @param weekDayInfo                 the WeekDayInfo entity to update
     * @param weekDayInfoUpdateRequestDTO the WeekDayInfoUpdateRequestDTO with updated information
     */
    @Override
    public void updateWeekDayInfoFromWeekDayInfoUpdateRequestDTO(
            WeekDayInfo weekDayInfo,
            WeekDayInfoUpdateRequestDTO weekDayInfoUpdateRequestDTO
    ) {
        if (weekDayInfo == null || weekDayInfoUpdateRequestDTO == null) {
            return;
        }

        weekDayInfo.setStartLaunch(weekDayInfoUpdateRequestDTO.getStartLaunch());
        weekDayInfo.setEndLaunch(weekDayInfoUpdateRequestDTO.getEndLaunch());
        weekDayInfo.setStartDinner(weekDayInfoUpdateRequestDTO.getStartDinner());
        weekDayInfo.setEndDinner(weekDayInfoUpdateRequestDTO.getEndDinner());
        if (weekDayInfoUpdateRequestDTO.getSittingTimeStep() != null) {
            weekDayInfo.setSittingTimeStep(
                    Enum.valueOf(SittingTimeStep.class, weekDayInfoUpdateRequestDTO.getSittingTimeStep())
            );
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * Converts a list of {@link WeekDayInfo} entities to a list of {@link WeekDayInfoResponseDTO} objects.
     *
     * @param weekDayInfos the list of WeekDayInfo entities to convert
     * @return the list of converted WeekDayInfoResponseDTO objects
     */
    @Override
    public List<WeekDayInfoResponseDTO> weekDayInfosToWeekDayInfoResponseDTOs(List<WeekDayInfo> weekDayInfos) {
        if (weekDayInfos == null) {
            return null;
        }

        List<WeekDayInfoResponseDTO> list = new ArrayList<>(weekDayInfos.size());
        weekDayInfos.forEach(weekDayInfo -> list.add(weekDayInfoToWeekDayInfoResponseDTO(weekDayInfo)));

        return list;
    }

    /**
     * Retrieves the restaurant ID from a {@link WeekDayInfo} entity.
     *
     * @param weekDayInfo the WeekDayInfo entity
     * @return the restaurant ID, or null if not available
     */
    private Long weekDayInfoRestaurantId(WeekDayInfo weekDayInfo) {
        if (weekDayInfo == null) {
            return null;
        }
        Restaurant restaurant = weekDayInfo.getRestaurant();
        if (restaurant == null) {
            return null;
        }
        return restaurant.getId();
    }
}