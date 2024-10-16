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
    private LocalTime start;
    private LocalTime end;
    private long weekDayInfoId;
}
