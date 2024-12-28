package com.example.foody.service;

import com.example.foody.dto.request.DishRequestDTO;
import com.example.foody.dto.request.DishUpdateRequestDTO;
import com.example.foody.dto.response.DetailedDishResponseDTO;
import com.example.foody.dto.response.DishResponseDTO;

import java.util.List;

public interface DishService {
    DishResponseDTO save(DishRequestDTO dishDTO);
    List<DetailedDishResponseDTO> findAll();
    DetailedDishResponseDTO findById(long id);
    List<DetailedDishResponseDTO> findAllByRestaurant(long restaurantId);
    DetailedDishResponseDTO update(long id, DishUpdateRequestDTO dishDTO);
    boolean remove(long id);
}