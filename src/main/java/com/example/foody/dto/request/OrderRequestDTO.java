package com.example.foody.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequestDTO {
    @NotBlank(message = "tableNumber cannot be blank")
    @Pattern(regexp = "^[0-9]*$", message = "tableNumber should be a non-negative number")
    private String tableNumber;

    @NotEmpty(message = "dishIds cannot be empty")
    private List<@NotNull(message = "dishId cannot be null") @Positive(message = "dishId cannot be a negative number or zero") Long> dishes = new ArrayList<>();

    @NotNull(message = "restaurantId cannot be null")
    @Positive(message = "restaurantId cannot be a negative number or zero")
    private Long restaurantId;
}
