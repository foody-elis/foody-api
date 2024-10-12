package com.example.foody.mapper;

import com.example.foody.dto.request.DishRequestDTO;
import com.example.foody.dto.response.DishResponseDTO;
import com.example.foody.model.Dish;

import java.util.List;

public interface DishMapper {
    DishResponseDTO dishToDishResponseDTO(Dish dish);
    Dish dishRequestDTOToDish(DishRequestDTO dishRequestDTO);
    List<DishResponseDTO> dishesToDishResponseDTOs(List<Dish> dishes);
}
