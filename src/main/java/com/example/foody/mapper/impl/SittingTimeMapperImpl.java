package com.example.foody.mapper.impl;

import com.example.foody.dto.response.SittingTimeResponseDTO;
import com.example.foody.mapper.SittingTimeMapper;
import com.example.foody.model.SittingTime;
import com.example.foody.model.WeekDayInfo;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SittingTimeMapperImpl implements SittingTimeMapper {

    @Override
    public SittingTimeResponseDTO sittingTimeToSittingTimeResponseDTO(SittingTime sittingTime) {
        if ( sittingTime == null ) {
            return null;
        }

        SittingTimeResponseDTO sittingTimeResponseDTO = new SittingTimeResponseDTO();

        sittingTimeResponseDTO.setWeekDayInfoId( sittingTimeWeekDayInfoId( sittingTime ) );
        sittingTimeResponseDTO.setId( sittingTime.getId() );
        sittingTimeResponseDTO.setStart( sittingTime.getStart() );
        sittingTimeResponseDTO.setEnd( sittingTime.getEnd() );

        return sittingTimeResponseDTO;
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

    private long sittingTimeWeekDayInfoId(SittingTime sittingTime) {
        if ( sittingTime == null ) {
            return 0L;
        }
        WeekDayInfo weekDayInfo = sittingTime.getWeekDayInfo();
        if ( weekDayInfo == null ) {
            return 0L;
        }
        long id = weekDayInfo.getId();
        return id;
    }
}
