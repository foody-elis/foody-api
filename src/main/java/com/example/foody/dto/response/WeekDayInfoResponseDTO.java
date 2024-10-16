package com.example.foody.dto.response;

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
    private LocalTime startLaunch;
    private LocalTime endLaunch;
    private LocalTime startDinner;
    private LocalTime endDinner;
    private String sittingTimeStep;
    private long restaurantId;
}
