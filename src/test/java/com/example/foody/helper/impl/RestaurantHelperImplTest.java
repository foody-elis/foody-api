package com.example.foody.helper.impl;

import com.example.foody.TestDataUtil;
import com.example.foody.dto.response.DetailedRestaurantResponseDTO;
import com.example.foody.mapper.RestaurantMapper;
import com.example.foody.model.Dish;
import com.example.foody.model.Restaurant;
import com.example.foody.model.Review;
import com.example.foody.model.SittingTime;
import com.example.foody.repository.DishRepository;
import com.example.foody.repository.ReviewRepository;
import com.example.foody.repository.SittingTimeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RestaurantHelperImplTest {

    @InjectMocks
    private RestaurantHelperImpl restaurantHelperImpl;

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private SittingTimeRepository sittingTimeRepository;

    @Mock
    private DishRepository dishRepository;

    @Mock
    private RestaurantMapper restaurantMapper;

    @Test
    void buildDetailedRestaurantResponseDTOWhenValidReturnsDetailedRestaurantResponseDTO() {
        // Arrange
        Restaurant restaurant = TestDataUtil.createTestRestaurant();
        double averageRating = 4.5;
        List<SittingTime> sittingTimes = List.of(TestDataUtil.createTestSittingTime());
        List<Dish> dishes = List.of(TestDataUtil.createTestDish());
        List<Review> reviews = List.of(TestDataUtil.createTestReview());
        DetailedRestaurantResponseDTO expectedResponseDTO = TestDataUtil.createTestDetailedRestaurantResponseDTO();

        when(reviewRepository.findAverageRatingByRestaurant_Id(restaurant.getId())).thenReturn(averageRating);
        when(sittingTimeRepository.findAllByRestaurant_IdAndWeekDayAndStartAfterNowOrderByStartLimit(
                restaurant.getId(), LocalDateTime.now().getDayOfWeek().getValue(), DetailedRestaurantResponseDTO.QueryResultLimits.SITTING_TIMES_LIMIT))
                .thenReturn(sittingTimes);
        when(dishRepository.findAllByRestaurant_IdOrderByAverageRatingDescLimit(
                restaurant.getId(), DetailedRestaurantResponseDTO.QueryResultLimits.DISHES_LIMIT))
                .thenReturn(dishes);
        when(reviewRepository.findAllByRestaurant_IdOrderByCreated_AtDescLimit(
                restaurant.getId(), DetailedRestaurantResponseDTO.QueryResultLimits.REVIEWS_LIMIT))
                .thenReturn(reviews);
        when(restaurantMapper.restaurantToDetailedRestaurantResponseDTO(restaurant, averageRating, sittingTimes, dishes, reviews))
                .thenReturn(expectedResponseDTO);

        // Act
        DetailedRestaurantResponseDTO result = restaurantHelperImpl.buildDetailedRestaurantResponseDTO(restaurant);

        // Assert
        assertNotNull(result);
        assertEquals(expectedResponseDTO, result);
        verify(reviewRepository).findAverageRatingByRestaurant_Id(restaurant.getId());
        verify(sittingTimeRepository).findAllByRestaurant_IdAndWeekDayAndStartAfterNowOrderByStartLimit(
                restaurant.getId(), LocalDateTime.now().getDayOfWeek().getValue(), DetailedRestaurantResponseDTO.QueryResultLimits.SITTING_TIMES_LIMIT);
        verify(dishRepository).findAllByRestaurant_IdOrderByAverageRatingDescLimit(
                restaurant.getId(), DetailedRestaurantResponseDTO.QueryResultLimits.DISHES_LIMIT);
        verify(reviewRepository).findAllByRestaurant_IdOrderByCreated_AtDescLimit(
                restaurant.getId(), DetailedRestaurantResponseDTO.QueryResultLimits.REVIEWS_LIMIT);
        verify(restaurantMapper).restaurantToDetailedRestaurantResponseDTO(restaurant, averageRating, sittingTimes, dishes, reviews);
    }

    @Test
    void buildDetailedRestaurantResponseDTOsWhenValidReturnsListOfDetailedRestaurantResponseDTO() {
        // Arrange
        Restaurant restaurant = TestDataUtil.createTestRestaurant();
        double averageRating = 4.5;
        List<SittingTime> sittingTimes = List.of(TestDataUtil.createTestSittingTime());
        List<Dish> dishes = List.of(TestDataUtil.createTestDish());
        List<Review> reviews = List.of(TestDataUtil.createTestReview());
        DetailedRestaurantResponseDTO expectedResponseDTO = TestDataUtil.createTestDetailedRestaurantResponseDTO();

        when(reviewRepository.findAverageRatingByRestaurant_Id(restaurant.getId())).thenReturn(averageRating);
        when(sittingTimeRepository.findAllByRestaurant_IdAndWeekDayAndStartAfterNowOrderByStartLimit(
                restaurant.getId(), LocalDateTime.now().getDayOfWeek().getValue(), DetailedRestaurantResponseDTO.QueryResultLimits.SITTING_TIMES_LIMIT))
                .thenReturn(sittingTimes);
        when(dishRepository.findAllByRestaurant_IdOrderByAverageRatingDescLimit(
                restaurant.getId(), DetailedRestaurantResponseDTO.QueryResultLimits.DISHES_LIMIT))
                .thenReturn(dishes);
        when(reviewRepository.findAllByRestaurant_IdOrderByCreated_AtDescLimit(
                restaurant.getId(), DetailedRestaurantResponseDTO.QueryResultLimits.REVIEWS_LIMIT))
                .thenReturn(reviews);
        when(restaurantMapper.restaurantToDetailedRestaurantResponseDTO(restaurant, averageRating, sittingTimes, dishes, reviews))
                .thenReturn(expectedResponseDTO);

        // Act
        List<DetailedRestaurantResponseDTO> result = restaurantHelperImpl.buildDetailedRestaurantResponseDTOs(List.of(restaurant));

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(expectedResponseDTO, result.get(0));
    }
}