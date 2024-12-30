package com.example.foody.mapper;

import com.example.foody.dto.request.DishRequestDTO;
import com.example.foody.dto.request.DishUpdateRequestDTO;
import com.example.foody.dto.response.DishResponseDTO;
import com.example.foody.model.Dish;

public interface DishMapper {
    DishResponseDTO dishToDishResponseDTO(Dish dish, double averageRating);
    Dish dishRequestDTOToDish(DishRequestDTO dishRequestDTO);
    void updateDishFromDishUpdateRequestDTO(Dish dish, DishUpdateRequestDTO dishUpdateRequestDTO);
}