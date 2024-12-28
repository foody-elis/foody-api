package com.example.foody.helper;

import com.example.foody.dto.response.DetailedDishResponseDTO;
import com.example.foody.model.Dish;

import java.util.List;

public interface DishHelper {
    DetailedDishResponseDTO buildDetailedDishResponseDTO(Dish dish);
    List<DetailedDishResponseDTO> buildDetailedDishResponseDTOs(List<Dish> dishes);
}