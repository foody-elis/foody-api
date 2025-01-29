package com.example.foody.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for order dish requests.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDishRequestDTO {

    @NotNull(message = "dishId cannot be null")
    @Positive(message = "dishId cannot be a negative number or zero")
    private Long dishId;

    @NotNull(message = "quantity cannot be null")
    @Min(value = 1, message = "quantity cannot be less than 1")
    private Integer quantity;
}