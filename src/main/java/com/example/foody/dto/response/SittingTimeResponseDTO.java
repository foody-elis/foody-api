package com.example.foody.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SittingTimeResponseDTO {
    private long id;
    private int weekDay;
    private LocalTime startTime;
    private LocalTime endTime;
    private long restaurantId;
}
