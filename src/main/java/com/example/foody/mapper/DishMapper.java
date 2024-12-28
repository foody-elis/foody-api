package com.example.foody.mapper;

import com.example.foody.dto.request.DishRequestDTO;
import com.example.foody.dto.request.DishUpdateRequestDTO;
import com.example.foody.dto.response.DetailedDishResponseDTO;
import com.example.foody.dto.response.DishResponseDTO;
import com.example.foody.model.Dish;

import java.util.List;

public interface DishMapper {
    DishResponseDTO dishToDishResponseDTO(Dish dish);
    DetailedDishResponseDTO dishToDetailedDishResponseDTO(Dish dish, double averageRating);
    Dish dishRequestDTOToDish(DishRequestDTO dishRequestDTO);
    void updateDishFromDishUpdateRequestDTO(Dish dish, DishUpdateRequestDTO dishUpdateRequestDTO);
    List<DishResponseDTO> dishesToDishResponseDTOs(List<Dish> dishes);
}