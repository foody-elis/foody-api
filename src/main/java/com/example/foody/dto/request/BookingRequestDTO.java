package com.example.foody.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingRequestDTO {
    @NotNull(message = "date cannot be null")
    @FutureOrPresent(message = "date cannot be in the past")
    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate date;

    @NotNull(message = "seats cannot be null")
    @Min(value = 1, message = "seats cannot be less than 1")
    private Integer seats;

    @NotNull(message = "sittingTimeId cannot be null")
    @Positive(message = "sittingTimeId cannot be a negative number")
    private Long sittingTimeId;

    @NotNull(message = "restaurantId cannot be null")
    @Positive(message = "restaurantId cannot be a negative number")
    private Long restaurantId;
}
