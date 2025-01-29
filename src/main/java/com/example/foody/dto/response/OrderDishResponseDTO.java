package com.example.foody.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Data Transfer Object for order dish response.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDishResponseDTO {
    private Long dishId;
    private String dishName;
    private BigDecimal price;
    private int quantity;
}