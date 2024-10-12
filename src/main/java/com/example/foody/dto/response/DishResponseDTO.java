package com.example.foody.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DishResponseDTO {
    private long id;
    private String name;
    private String description;
    private BigDecimal price;
    private String photo;
    private long restaurantId;
}