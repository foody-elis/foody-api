package com.example.foody.helper.impl;

import com.example.foody.dto.response.DetailedRestaurantResponseDTO;
import com.example.foody.helper.RestaurantHelper;
import com.example.foody.mapper.RestaurantMapper;
import com.example.foody.model.Dish;
import com.example.foody.model.Restaurant;
import com.example.foody.model.Review;
import com.example.foody.model.SittingTime;
import com.example.foody.repository.DishRepository;
import com.example.foody.repository.ReviewRepository;
import com.example.foody.repository.SittingTimeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Implementation of the {@link RestaurantHelper} interface.
 * <p>
 * Provides methods to build {@link DetailedRestaurantResponseDTO} objects from {@link Restaurant} objects.
 */
@Component
@AllArgsConstructor
public class RestaurantHelperImpl implements RestaurantHelper {
    private final ReviewRepository reviewRepository;
    private final SittingTimeRepository sittingTimeRepository;
    private final DishRepository dishRepository;
    private final RestaurantMapper restaurantMapper;

    /**
     * {@inheritDoc}
     * <p>
     * This method calculates the average rating for the restaurant, retrieves the sitting times, dishes, and reviews,
     * and maps them to a {@link DetailedRestaurantResponseDTO}.
     *
     * @param restaurant the Restaurant object to convert
     * @return the constructed {@link DetailedRestaurantResponseDTO}
     */
    @Override
    public DetailedRestaurantResponseDTO buildDetailedRestaurantResponseDTO(Restaurant restaurant) {
        double averageRating = reviewRepository.findAverageRatingByRestaurant_Id(restaurant.getId());
        List<SittingTime> sittingTimes = sittingTimeRepository
                .findAllByRestaurant_IdAndWeekDayAndStartAfterNowOrderByStartLimit(
                        restaurant.getId(),
                        LocalDateTime.now().getDayOfWeek().getValue(),
                        DetailedRestaurantResponseDTO.QueryResultLimits.SITTING_TIMES_LIMIT
                );
        List<Dish> dishes = dishRepository
                .findAllByRestaurant_IdOrderByAverageRatingDescLimit(
                        restaurant.getId(),
                        DetailedRestaurantResponseDTO.QueryResultLimits.DISHES_LIMIT
                );
        List<Review> reviews = reviewRepository
                .findAllByRestaurant_IdOrderByCreated_AtDescLimit(
                        restaurant.getId(),
                        DetailedRestaurantResponseDTO.QueryResultLimits.REVIEWS_LIMIT
                );

        return restaurantMapper
                .restaurantToDetailedRestaurantResponseDTO(restaurant, averageRating, sittingTimes, dishes, reviews);
    }

    /**
     * {@inheritDoc}
     * <p>
     * This method converts a list of {@link Restaurant} objects to a list of {@link DetailedRestaurantResponseDTO} objects.
     *
     * @param restaurants the list of Restaurant objects to convert
     * @return the list of constructed {@link DetailedRestaurantResponseDTO} objects
     */
    @Override
    public List<DetailedRestaurantResponseDTO> buildDetailedRestaurantResponseDTOs(List<Restaurant> restaurants) {
        return restaurants.stream()
                .map(this::buildDetailedRestaurantResponseDTO)
                .toList();
    }
}