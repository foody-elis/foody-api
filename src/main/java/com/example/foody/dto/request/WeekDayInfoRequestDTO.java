package com.example.foody.dto.request;

import com.example.foody.utils.enums.SittingTimeStep;
import com.example.foody.utils.validators.after_time.AfterTime;
import com.example.foody.utils.validators.uniform_nullity.UniformNullity;
import com.example.foody.utils.validators.value_of_enum.ValueOfEnum;
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
@UniformNullity(fields = {"startLaunch", "endLaunch"}, message = "startLaunch and endLaunch must be both null or both not null")
@UniformNullity(fields = {"startDinner", "endDinner"}, message = "startDinner and endDinner must be both null or both not null")
public class WeekDayInfoRequestDTO {
    @NotNull(message = "weekDay cannot be null")
    @Min(value = 1, message = "weekDay cannot be less than 1")
    @Max(value = 7, message = "weekDay cannot be greater than 7")
    private Integer weekDay;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime startLaunch;

    @JsonFormat(pattern = "HH:mm")
    @AfterTime(target = "startLaunch", message = "endLaunch must be after startLaunch")
    private LocalTime endLaunch;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime startDinner;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime endDinner;

    @ValueOfEnum(enumClass = SittingTimeStep.class, message = "invalid sittingTimeStep value")
    @NotNull(message = "sittingTimeStep cannot be null")
    private String sittingTimeStep;

    // Logical properties for validation
    // todo add custom annotations for these validations

//    @AssertTrue(message = "endLaunch must be after startLaunch")
//    @JsonIgnore
//    public boolean isEndLaunchAfterStartLaunch() {
//        return endLaunch == null || startLaunch == null || endLaunch.isAfter(startLaunch);
//    }
//
//    @AssertTrue(message = "endDinner must be after startDinner")
//    @JsonIgnore
//    public boolean isEndDinnerAfterStartDinner() {
//        return endDinner == null || startDinner == null || endDinner.isAfter(startDinner);
//    }
//
//    @AssertTrue(message = "startDinner must be after endLaunch")
//    @JsonIgnore
//    public boolean isStartDinnerAfterEndLaunch() {
//        return startDinner == null || endLaunch == null || startDinner.isAfter(endLaunch);
//    }
}