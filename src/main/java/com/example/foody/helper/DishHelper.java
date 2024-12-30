package com.example.foody.helper;

import com.example.foody.dto.response.DishResponseDTO;
import com.example.foody.model.Dish;

import java.util.List;

public interface DishHelper {
    DishResponseDTO buildDishResponseDTO(Dish dish);
    List<DishResponseDTO> buildDishResponseDTOs(List<Dish> dishes);
}