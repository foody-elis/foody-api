package com.example.foody.mapper.impl;

import com.example.foody.builder.DishBuilder;
import com.example.foody.dto.request.DishRequestDTO;
import com.example.foody.dto.response.DishResponseDTO;
import com.example.foody.mapper.DishMapper;
import com.example.foody.model.Dish;
import com.example.foody.model.Restaurant;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DishMapperImpl implements DishMapper {
    private final DishBuilder dishBuilder;

    public DishMapperImpl(DishBuilder dishBuilder) {
        this.dishBuilder = dishBuilder;
    }

    @Override
    public DishResponseDTO dishToDishResponseDTO(Dish dish) {
        if (dish == null) {
            return null;
        }

        DishResponseDTO dishResponseDTO = new DishResponseDTO();

        dishResponseDTO.setRestaurantId(dishRestaurantId(dish));
        dishResponseDTO.setId(dish.getId());
        dishResponseDTO.setName(dish.getName());
        dishResponseDTO.setDescription(dish.getDescription());
        dishResponseDTO.setPrice(dish.getPrice());
        dishResponseDTO.setPhoto(dish.getPhoto());

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
                .photo(dishRequestDTO.getPhoto())
                .build();
    }

    @Override
    public List<DishResponseDTO> dishesToDishResponseDTOs(List<Dish> dishes) {
        if (dishes == null) {
            return null;
        }

        List<DishResponseDTO> list = new ArrayList<>(dishes.size());
        dishes.forEach(dish -> list.add(dishToDishResponseDTO(dish)));

        return list;
    }

    private long dishRestaurantId(Dish dish) {
        if (dish == null) {
            return 0L;
        }
        Restaurant restaurant = dish.getRestaurant();
        if (restaurant == null) {
            return 0L;
        }
        return restaurant.getId();
    }
}
