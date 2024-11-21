package com.example.foody.service;

import com.example.foody.dto.request.DishRequestDTO;
import com.example.foody.dto.response.DishResponseDTO;

import java.util.List;

public interface DishService {
    DishResponseDTO save(DishRequestDTO dishDTO);
    List<DishResponseDTO> findAll();
    DishResponseDTO findById(long id);
    List<DishResponseDTO> findAllByRestaurant(long restaurantId);
    boolean remove(long id);
}
