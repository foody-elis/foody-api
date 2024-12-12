package com.example.foody.mapper;

import com.example.foody.dto.request.RestaurantRequestDTO;
import com.example.foody.dto.response.DetailedRestaurantResponseDTO;
import com.example.foody.dto.response.RestaurantResponseDTO;
import com.example.foody.model.Dish;
import com.example.foody.model.Restaurant;
import com.example.foody.model.Review;
import com.example.foody.model.SittingTime;

import java.util.List;

public interface RestaurantMapper {
    RestaurantResponseDTO restaurantToRestaurantResponseDTO(Restaurant restaurant);
    DetailedRestaurantResponseDTO restaurantToDetailedRestaurantResponseDTO(Restaurant restaurant, double averageRating, List<SittingTime> sittingTimes, List<Dish> dishes, List<Review> reviews);
    Restaurant restaurantRequestDTOToRestaurant(RestaurantRequestDTO restaurantRequestDTO);
}