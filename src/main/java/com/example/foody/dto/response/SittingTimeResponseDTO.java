package com.example.foody.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SittingTimeResponseDTO {
    private long id;
    private int weekDay;
    private String startTime;
    private String endTime;
    private long restaurantId;
}
