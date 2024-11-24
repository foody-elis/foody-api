package com.example.foody.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WeekDayInfoResponseDTO {
    private long id;

    private int weekDay;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime startLaunch;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime endLaunch;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime startDinner;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime endDinner;

    private String sittingTimeStep;

    private long restaurantId;
}
