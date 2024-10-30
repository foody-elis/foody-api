package com.example.foody.dto.request;

import com.example.foody.utils.enums.SittingTimeStep;
import com.example.foody.utils.validators.ValueOfEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WeekDayInfoRequestDTO {
    @NotNull(message = "weekDay cannot be null")
    @Min(value = 1, message = "weekDay cannot be less than 1")
    @Max(value = 7, message = "weekDay cannot be greater than 7")
    private Integer weekDay;

    @NotNull(message = "startLaunch cannot be null")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime startLaunch;

    @NotNull(message = "endLaunch cannot be null")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime endLaunch;

    @NotNull(message = "startDinner cannot be null")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime startDinner;

    @NotNull(message = "endDinner cannot be null")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime endDinner;

    @ValueOfEnum(enumClass = SittingTimeStep.class, message = "invalid sittingTimeStep value")
    @NotNull(message = "sittingTimeStep cannot be null")
    private String sittingTimeStep;

    @NotNull(message = "restaurantId cannot be null")
    @Positive(message = "restaurantId cannot be a negative number")
    private Long restaurantId;

    // Logical properties for validation

    @AssertTrue(message = "endLaunch must be after startLaunch")
    @JsonIgnore
    public boolean isEndLaunchAfterStartLaunch() {
        return endLaunch.isAfter(startLaunch);
    }

    @AssertTrue(message = "endDinner must be after startDinner")
    @JsonIgnore
    public boolean isEndDinnerAfterStartDinner() {
        return endDinner.isAfter(startDinner);
    }

    @AssertTrue(message = "startDinner must be after endLaunch")
    @JsonIgnore
    public boolean isStartDinnerAfterEndLaunch() {
        return startDinner.isAfter(endLaunch);
    }
}
