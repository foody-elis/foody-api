package com.example.foody.dto.response;

import com.example.foody.utils.serializer.RoundedDoubleSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Data Transfer Object for dish response.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DishResponseDTO {

    private long id;
    private String name;
    private String description;
    private BigDecimal price;
    private String photoUrl;
    private Long restaurantId;

    /**
     * The average rating of the dish.
     * <p>
     * Serialized using RoundedDoubleSerializer to round the value.
     */
    @JsonSerialize(using = RoundedDoubleSerializer.class)
    private double averageRating;
}