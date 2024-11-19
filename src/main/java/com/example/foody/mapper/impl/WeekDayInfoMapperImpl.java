package com.example.foody.mapper.impl;

import com.example.foody.builder.WeekDayInfoBuilder;
import com.example.foody.dto.request.WeekDayInfoRequestDTO;
import com.example.foody.dto.response.WeekDayInfoResponseDTO;
import com.example.foody.mapper.WeekDayInfoMapper;
import com.example.foody.model.Restaurant;
import com.example.foody.model.WeekDayInfo;
import com.example.foody.utils.enums.SittingTimeStep;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class WeekDayInfoMapperImpl implements WeekDayInfoMapper {
    private final WeekDayInfoBuilder weekDayInfoBuilder;

    public WeekDayInfoMapperImpl(WeekDayInfoBuilder weekDayInfoBuilder) {
        this.weekDayInfoBuilder = weekDayInfoBuilder;
    }

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

    @Override
    public List<WeekDayInfoResponseDTO> weekDayInfosToWeekDayInfoResponseDTOs(List<WeekDayInfo> weekDayInfos) {
        if (weekDayInfos == null) {
            return null;
        }

        List<WeekDayInfoResponseDTO> list = new ArrayList<>(weekDayInfos.size());
        weekDayInfos.forEach(weekDayInfo -> list.add(weekDayInfoToWeekDayInfoResponseDTO(weekDayInfo)));

        return list;
    }

    private long weekDayInfoRestaurantId(WeekDayInfo weekDayInfo) {
        if (weekDayInfo == null) {
            return 0L;
        }
        Restaurant restaurant = weekDayInfo.getRestaurant();
        if (restaurant == null) {
            return 0L;
        }
        return restaurant.getId();
    }
}
