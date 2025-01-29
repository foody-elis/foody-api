package com.example.foody.dto.response;

import com.example.foody.utils.serializer.RoundedDoubleSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Data Transfer Object for detailed restaurant response.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetailedRestaurantResponseDTO extends RestaurantResponseDTO {

    @JsonSerialize(using = RoundedDoubleSerializer.class)
    private double averageRating;

    /**
     * List of sitting times, first SITTING_TIMES_LIMIT ordered by start time after now.
     */
    private List<SittingTimeResponseDTO> sittingTimes = new ArrayList<>();

    /**
     * List of dishes, first DISHES_LIMIT ordered by average rating.
     */
    private List<DishResponseDTO> dishes = new ArrayList<>();

    /**
     * List of reviews, first REVIEWS_LIMIT ordered by date.
     */
    private List<ReviewResponseDTO> reviews = new ArrayList<>();

    /**
     * Query result limits for sitting times, dishes, and reviews.
     */
    public static class QueryResultLimits {
        public static final int SITTING_TIMES_LIMIT = 3;
        public static final int DISHES_LIMIT = 5;
        public static final int REVIEWS_LIMIT = 10;
    }
}