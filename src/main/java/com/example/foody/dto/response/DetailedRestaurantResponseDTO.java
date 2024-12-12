package com.example.foody.dto.response;

import com.example.foody.utils.serializer.RoundedDoubleSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetailedRestaurantResponseDTO extends RestaurantResponseDTO {
    @JsonSerialize(using = RoundedDoubleSerializer.class)
    private double averageRating;
    private List<SittingTimeResponseDTO> sittingTimes = new ArrayList<>(); // First 3 order by start time after now
    private List<DishResponseDTO> dishes = new ArrayList<>(); // First 5 order by average rating
    private List<ReviewResponseDTO> reviews = new ArrayList<>(); // First 10 order by date

    public static class QueryResultLimits {
        public static final int SITTING_TIMES_LIMIT = 3;
        public static final int DISHES_LIMIT = 5;
        public static final int REVIEWS_LIMIT = 10;
    }
}