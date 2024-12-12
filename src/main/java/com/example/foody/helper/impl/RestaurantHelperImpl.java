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
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Component
public class RestaurantHelperImpl implements RestaurantHelper {
    private final ReviewRepository reviewRepository;
    private final SittingTimeRepository sittingTimeRepository;
    private final DishRepository dishRepository;
    private final RestaurantMapper restaurantMapper;

    public RestaurantHelperImpl(ReviewRepository reviewRepository, SittingTimeRepository sittingTimeRepository, DishRepository dishRepository, RestaurantMapper restaurantMapper) {
        this.reviewRepository = reviewRepository;
        this.sittingTimeRepository = sittingTimeRepository;
        this.dishRepository = dishRepository;
        this.restaurantMapper = restaurantMapper;
    }

    @Override
    public DetailedRestaurantResponseDTO buildDetailedRestaurantResponseDTO(Restaurant restaurant) {
        double averageRating = reviewRepository.findAverageRatingByRestaurant_Id(restaurant.getId());
        List<SittingTime> sittingTimes = sittingTimeRepository
                .findAllByDeletedAtIsNullAndRestaurant_IdAndWeekDayAndStartAfterNowOrderByStartLimit(restaurant.getId(), LocalDateTime.now().getDayOfWeek().getValue(), DetailedRestaurantResponseDTO.QueryResultLimits.SITTING_TIMES_LIMIT);
        List<Dish> dishes = dishRepository
                .findAllByDeletedAtIsNullAndRestaurant_IdOrderByAverageRatingDescLimit(restaurant.getId(), DetailedRestaurantResponseDTO.QueryResultLimits.DISHES_LIMIT);
        List<Review> reviews = reviewRepository
                .findAllByDeletedAtIsNullAndRestaurant_IdOrderByCreated_AtDescLimit(restaurant.getId(), DetailedRestaurantResponseDTO.QueryResultLimits.REVIEWS_LIMIT);

        return restaurantMapper
                .restaurantToDetailedRestaurantResponseDTO(restaurant, averageRating, sittingTimes, dishes, reviews);
    }

    @Override
    public List<DetailedRestaurantResponseDTO> buildDetailedRestaurantResponseDTOs(List<Restaurant> restaurants) {
        return restaurants.stream()
                .map(this::buildDetailedRestaurantResponseDTO)
                .toList();
    }
}