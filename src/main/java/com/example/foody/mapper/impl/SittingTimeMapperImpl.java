package com.example.foody.mapper.impl;

import com.example.foody.builder.SittingTimeBuilder;
import com.example.foody.dto.request.SittingTimeRequestDTO;
import com.example.foody.dto.response.SittingTimeResponseDTO;
import com.example.foody.mapper.SittingTimeMapper;
import com.example.foody.model.Restaurant;
import com.example.foody.model.SittingTime;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
public class SittingTimeMapperImpl implements SittingTimeMapper {
    private final SittingTimeBuilder sittingTimeBuilder;

    public SittingTimeMapperImpl(SittingTimeBuilder sittingTimeBuilder) {
        this.sittingTimeBuilder = sittingTimeBuilder;
    }

    @Override
    public SittingTimeResponseDTO sittingTimeToSittingTimeResponseDTO(SittingTime sittingTime) {
        if ( sittingTime == null ) {
            return null;
        }

        SittingTimeResponseDTO sittingTimeResponseDTO = new SittingTimeResponseDTO();

        sittingTimeResponseDTO.setRestaurantId( sittingTimeRestaurantId( sittingTime ) );
        sittingTimeResponseDTO.setId( sittingTime.getId() );
        sittingTimeResponseDTO.setWeekDay( sittingTime.getWeekDay() );
        if ( sittingTime.getStartTime() != null ) {
            sittingTimeResponseDTO.setStartTime( DateTimeFormatter.ISO_LOCAL_TIME.format( sittingTime.getStartTime() ) );
        }
        if ( sittingTime.getEndTime() != null ) {
            sittingTimeResponseDTO.setEndTime( DateTimeFormatter.ISO_LOCAL_TIME.format( sittingTime.getEndTime() ) );
        }

        return sittingTimeResponseDTO;
    }

    @Override
    public SittingTime sittingTimeRequestDTOToSittingTime(SittingTimeRequestDTO sittingTimeRequestDTO) {
        if ( sittingTimeRequestDTO == null ) {
            return null;
        }

        SittingTime sittingTime = sittingTimeBuilder
                .weekDay( sittingTimeRequestDTO.getWeekDay() )
                .startTime( sittingTimeRequestDTO.getStartTime() )
                .endTime( sittingTimeRequestDTO.getEndTime() )
                .build();

        return sittingTime;
    }

    @Override
    public List<SittingTimeResponseDTO> sittingTimesToSittingTimeResponseDTOs(List<SittingTime> sittingTimes) {
        if ( sittingTimes == null ) {
            return null;
        }

        List<SittingTimeResponseDTO> list = new ArrayList<SittingTimeResponseDTO>( sittingTimes.size() );
        for ( SittingTime sittingTime : sittingTimes ) {
            list.add( sittingTimeToSittingTimeResponseDTO( sittingTime ) );
        }

        return list;
    }

    private long sittingTimeRestaurantId(SittingTime sittingTime) {
        if ( sittingTime == null ) {
            return 0L;
        }
        Restaurant restaurant = sittingTime.getRestaurant();
        if ( restaurant == null ) {
            return 0L;
        }
        long id = restaurant.getId();
        return id;
    }
}
