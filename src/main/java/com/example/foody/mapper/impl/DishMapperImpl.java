package com.example.foody.mapper.impl;

import com.example.foody.builder.DishBuilder;
import com.example.foody.dto.request.DishRequestDTO;
import com.example.foody.dto.request.DishUpdateRequestDTO;
import com.example.foody.dto.response.DishResponseDTO;
import com.example.foody.mapper.DishMapper;
import com.example.foody.model.Dish;
import com.example.foody.model.Restaurant;
import org.springframework.stereotype.Component;

@Component
public class DishMapperImpl implements DishMapper {
    private final DishBuilder dishBuilder;

    public DishMapperImpl(DishBuilder dishBuilder) {
        this.dishBuilder = dishBuilder;
    }

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

    @Override
    public void updateDishFromDishUpdateRequestDTO(Dish dish, DishUpdateRequestDTO dishUpdateRequestDTO) {
        if (dish == null || dishUpdateRequestDTO == null) {
            return;
        }

        dish.setName(dishUpdateRequestDTO.getName());
        dish.setDescription(dishUpdateRequestDTO.getDescription());
        dish.setPrice(dishUpdateRequestDTO.getPrice());
    }

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