package com.example.foody.mapper;

import com.example.foody.dto.request.DishRequestDTO;
import com.example.foody.dto.request.DishUpdateRequestDTO;
import com.example.foody.dto.response.DishResponseDTO;
import com.example.foody.model.Dish;

/**
 * Mapper interface for converting between Dish entities and DTOs.
 */
public interface DishMapper {

    /**
     * Converts a Dish entity to a DishResponseDTO.
     *
     * @param dish the Dish entity to convert
     * @param averageRating the average rating of the dish
     * @return the converted DishResponseDTO
     */
    DishResponseDTO dishToDishResponseDTO(Dish dish, double averageRating);

    /**
     * Converts a DishRequestDTO to a Dish entity.
     *
     * @param dishRequestDTO the DishRequestDTO to convert
     * @return the converted Dish entity
     */
    Dish dishRequestDTOToDish(DishRequestDTO dishRequestDTO);

    /**
     * Updates a Dish entity from a DishUpdateRequestDTO.
     *
     * @param dish the Dish entity to update
     * @param dishUpdateRequestDTO the DishUpdateRequestDTO with updated information
     */
    void updateDishFromDishUpdateRequestDTO(Dish dish, DishUpdateRequestDTO dishUpdateRequestDTO);
}