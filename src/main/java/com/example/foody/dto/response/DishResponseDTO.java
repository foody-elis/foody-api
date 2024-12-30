package com.example.foody.dto.response;

import com.example.foody.utils.serializer.RoundedDoubleSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
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
    private String photoUrl;
    private long restaurantId;
    @JsonSerialize(using = RoundedDoubleSerializer.class)
    private double averageRating;
}