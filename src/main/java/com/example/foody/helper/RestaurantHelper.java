package com.example.foody.helper;

import com.example.foody.dto.response.DetailedRestaurantResponseDTO;
import com.example.foody.model.Restaurant;

import java.util.List;

public interface RestaurantHelper {
    DetailedRestaurantResponseDTO buildDetailedRestaurantResponseDTO(Restaurant restaurant);
    List<DetailedRestaurantResponseDTO> buildDetailedRestaurantResponseDTOs(List<Restaurant> restaurants);
}