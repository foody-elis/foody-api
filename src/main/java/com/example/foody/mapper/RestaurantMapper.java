package com.example.foody.mapper;

import com.example.foody.dto.request.RestaurantRequestDTO;
import com.example.foody.dto.response.RestaurantResponseDTO;
import com.example.foody.model.Restaurant;

import java.util.List;

public interface RestaurantMapper {
    RestaurantResponseDTO restaurantToRestaurantResponseDTO(Restaurant restaurant);
    Restaurant restaurantRequestDTOToRestaurant(RestaurantRequestDTO restaurantRequestDTO);
    List<RestaurantResponseDTO> restaurantsToRestaurantResponseDTOs(List<Restaurant> restaurants);
}
