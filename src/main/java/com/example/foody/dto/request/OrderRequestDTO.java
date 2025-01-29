package com.example.foody.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Data Transfer Object for order requests.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequestDTO {

    @NotBlank(message = "tableCode cannot be blank")
    @Size(min = 1, max = 10, message = "tableCode cannot be less than 1 character or more than 10 characters long")
    private String tableCode;

    @NotEmpty(message = "orderDishes cannot be empty")
    private List<OrderDishRequestDTO> orderDishes = new ArrayList<>();

    @NotNull(message = "restaurantId cannot be null")
    @Positive(message = "restaurantId cannot be a negative number or zero")
    private Long restaurantId;
}