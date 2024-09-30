package com.example.foody.service;

import com.example.foody.dto.request.RestaurantRequestDTO;
import com.example.foody.dto.response.RestaurantResponseDTO;

import java.util.List;

public interface RestaurantService {
    RestaurantResponseDTO save(RestaurantRequestDTO restaurantDTO);
    List<RestaurantResponseDTO> findAll();
    RestaurantResponseDTO findById(long id);
    boolean remove(long id);
}
