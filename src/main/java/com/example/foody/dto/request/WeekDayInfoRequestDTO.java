package com.example.foody.dto.request;

import com.example.foody.utils.enums.SittingTimeStep;
import com.example.foody.utils.validators.sequential_times.SequentialTimes;
import com.example.foody.utils.validators.uniform_nullity.UniformNullity;
import com.example.foody.utils.validators.value_of_enum.ValueOfEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@UniformNullity.List({
        @UniformNullity(fields = {"startLaunch", "endLaunch"}, message = "startLaunch and endLaunch must be both null or both not null"),
        @UniformNullity(fields = {"startDinner", "endDinner"}, message = "startDinner and endDinner must be both null or both not null")
})
@SequentialTimes(fields = {"startLaunch", "endLaunch", "startDinner", "endDinner"}, message = "end time must be after start time")
public class WeekDayInfoRequestDTO {
    @NotNull(message = "weekDay cannot be null")
    @Min(value = 1, message = "weekDay cannot be less than 1")
    @Max(value = 7, message = "weekDay cannot be greater than 7")
    private Integer weekDay;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime startLaunch;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime endLaunch;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime startDinner;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime endDinner;

    @ValueOfEnum(enumClass = SittingTimeStep.class, message = "invalid sittingTimeStep value")
    @NotNull(message = "sittingTimeStep cannot be null")
    private String sittingTimeStep;
}