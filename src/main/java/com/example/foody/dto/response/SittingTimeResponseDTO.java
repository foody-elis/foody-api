package com.example.foody.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SittingTimeResponseDTO {
    private long id;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime start;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime end;

    private long weekDayInfoId;
}