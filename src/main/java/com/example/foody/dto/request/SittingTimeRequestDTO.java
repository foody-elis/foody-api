package com.example.foody.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SittingTimeRequestDTO {
    @NotNull(message = "weekDay cannot be null")
    @Min(value = 1, message = "weekDay cannot be less than 1")
    @Max(value = 7, message = "weekDay cannot be greater than 7")
    private Integer weekDay;

    @NotNull(message = "startTime cannot be null")
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime startTime;

    @NotNull(message = "endTime cannot be blank")
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime endTime;

    @NotNull(message = "restaurantId cannot be null")
    @Positive(message = "restaurantId cannot be a negative number")
    private Long restaurantId;

    @AssertTrue(message = "endTime must be after startTime")
    public boolean isEndTimeAfterStartTime() {
        return endTime.isAfter(startTime);
    }
}
