package com.example.foody.helper;

import com.example.foody.dto.response.DishResponseDTO;
import com.example.foody.model.Dish;

import java.util.List;

/**
 * Helper interface for building {@link DishResponseDTO} objects.
 */
public interface DishHelper {

    /**
     * Builds a {@link DishResponseDTO} from a given Dish.
     *
     * @param dish the Dish object to convert
     * @return the constructed {@link DishResponseDTO}
     */
    DishResponseDTO buildDishResponseDTO(Dish dish);

    /**
     * Builds a list of {@link DishResponseDTO} objects from a given list of Dishes.
     *
     * @param dishes the list of Dish objects to convert
     * @return the list of constructed {@link DishResponseDTO} objects
     */
    List<DishResponseDTO> buildDishResponseDTOs(List<Dish> dishes);
}