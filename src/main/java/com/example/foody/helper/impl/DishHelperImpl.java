package com.example.foody.helper.impl;

import com.example.foody.dto.response.DetailedDishResponseDTO;
import com.example.foody.helper.DishHelper;
import com.example.foody.mapper.DishMapper;
import com.example.foody.model.Dish;
import com.example.foody.repository.ReviewRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DishHelperImpl implements DishHelper {
    private final ReviewRepository reviewRepository;
    private final DishMapper dishMapper;

    public DishHelperImpl(ReviewRepository reviewRepository, DishMapper dishMapper) {
        this.reviewRepository = reviewRepository;
        this.dishMapper = dishMapper;
    }

    @Override
    public DetailedDishResponseDTO buildDetailedDishResponseDTO(Dish dish) {
        double averageRating = reviewRepository.findAverageRatingByDish_Id(dish.getId());
        return dishMapper.dishToDetailedDishResponseDTO(dish, averageRating);
    }

    @Override
    public List<DetailedDishResponseDTO> buildDetailedDishResponseDTOs(List<Dish> dishes) {
        return dishes.stream()
                .map(this::buildDetailedDishResponseDTO)
                .toList();
    }
}