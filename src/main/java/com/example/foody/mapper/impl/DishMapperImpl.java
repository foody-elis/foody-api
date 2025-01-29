package com.example.foody.mapper.impl;

import com.example.foody.builder.DishBuilder;
import com.example.foody.dto.request.DishRequestDTO;
import com.example.foody.dto.request.DishUpdateRequestDTO;
import com.example.foody.dto.response.DishResponseDTO;
import com.example.foody.mapper.DishMapper;
import com.example.foody.model.Dish;
import com.example.foody.model.Restaurant;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Implementation of the {@link DishMapper} interface.
 * <p>
 * Provides methods to convert between {@link Dish} entities and DTOs.
 */
@Component
@AllArgsConstructor
public class DishMapperImpl implements DishMapper {

    private final DishBuilder dishBuilder;

    /**
     * {@inheritDoc}
     * <p>
     * Converts a {@link Dish} entity to a {@link DishResponseDTO}.
     *
     * @param dish          the Dish entity to convert
     * @param averageRating the average rating of the dish
     * @return the converted DishResponseDTO
     */
    @Override
    public DishResponseDTO dishToDishResponseDTO(Dish dish, double averageRating) {
        if (dish == null) {
            return null;
        }

        DishResponseDTO dishResponseDTO = new DishResponseDTO();

        dishResponseDTO.setId(dish.getId());
        dishResponseDTO.setName(dish.getName());
        dishResponseDTO.setDescription(dish.getDescription());
        dishResponseDTO.setPrice(dish.getPrice());
        dishResponseDTO.setPhotoUrl(dish.getPhotoUrl());
        dishResponseDTO.setRestaurantId(dishRestaurantId(dish));
        dishResponseDTO.setAverageRating(averageRating);

        return dishResponseDTO;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Converts a {@link DishRequestDTO} to a {@link Dish} entity.
     *
     * @param dishRequestDTO the DishRequestDTO to convert
     * @return the converted Dish entity
     */
    @Override
    public Dish dishRequestDTOToDish(DishRequestDTO dishRequestDTO) {
        if (dishRequestDTO == null) {
            return null;
        }

        return dishBuilder
                .name(dishRequestDTO.getName())
                .description(dishRequestDTO.getDescription())
                .price(dishRequestDTO.getPrice())
                .build();
    }

    /**
     * {@inheritDoc}
     * <p>
     * Updates a {@link Dish} entity from a {@link DishUpdateRequestDTO}.
     *
     * @param dish                 the Dish entity to update
     * @param dishUpdateRequestDTO the DishUpdateRequestDTO with updated information
     */
    @Override
    public void updateDishFromDishUpdateRequestDTO(Dish dish, DishUpdateRequestDTO dishUpdateRequestDTO) {
        if (dish == null || dishUpdateRequestDTO == null) {
            return;
        }

        dish.setName(dishUpdateRequestDTO.getName());
        dish.setDescription(dishUpdateRequestDTO.getDescription());
        dish.setPrice(dishUpdateRequestDTO.getPrice());
    }

    /**
     * Retrieves the restaurant ID from a {@link Dish} entity.
     *
     * @param dish the Dish entity
     * @return the restaurant ID, or null if not available
     */
    private Long dishRestaurantId(Dish dish) {
        if (dish == null) {
            return null;
        }
        Restaurant restaurant = dish.getRestaurant();
        if (restaurant == null) {
            return null;
        }
        return restaurant.getId();
    }
}