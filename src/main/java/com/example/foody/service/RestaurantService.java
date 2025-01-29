package com.example.foody.service;

import com.example.foody.dto.request.RestaurantRequestDTO;
import com.example.foody.dto.response.DetailedRestaurantResponseDTO;
import com.example.foody.dto.response.RestaurantResponseDTO;

import java.util.List;

public interface RestaurantService {
    RestaurantResponseDTO save(RestaurantRequestDTO restaurantDTO);

    List<DetailedRestaurantResponseDTO> findAll();

    DetailedRestaurantResponseDTO findById(long id);

    DetailedRestaurantResponseDTO findByRestaurateur(long restaurateurId);

    List<DetailedRestaurantResponseDTO> findAllByCategory(long categoryId);

    DetailedRestaurantResponseDTO approveById(long id);

    DetailedRestaurantResponseDTO update(long id, RestaurantRequestDTO restaurantDTO);

    boolean remove(long id);
}