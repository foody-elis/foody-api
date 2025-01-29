package com.example.foody.helper.impl;

import com.example.foody.dto.response.DishResponseDTO;
import com.example.foody.helper.DishHelper;
import com.example.foody.mapper.DishMapper;
import com.example.foody.model.Dish;
import com.example.foody.repository.ReviewRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Implementation of the {@link DishHelper} interface.
 * <p>
 * Provides methods to build {@link DishResponseDTO} objects from {@link Dish} objects.
 */
@Component
@AllArgsConstructor
public class DishHelperImpl implements DishHelper {

    private final ReviewRepository reviewRepository;
    private final DishMapper dishMapper;

    /**
     * {@inheritDoc}
     * <p>
     * Calculates the average rating for the dish and maps it to a {@link DishResponseDTO}.
     *
     * @param dish the Dish object to convert
     * @return the constructed {@link DishResponseDTO}
     */
    @Override
    public DishResponseDTO buildDishResponseDTO(Dish dish) {
        double averageRating = reviewRepository.findAverageRatingByDish_Id(dish.getId());
        return dishMapper.dishToDishResponseDTO(dish, averageRating);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Builds a list of {@link DishResponseDTO} objects from a given list of {@link Dish} objects.
     *
     * @param dishes the list of Dish objects to convert
     * @return the list of constructed {@link DishResponseDTO} objects
     */
    @Override
    public List<DishResponseDTO> buildDishResponseDTOs(List<Dish> dishes) {
        return dishes.stream()
                .map(this::buildDishResponseDTO)
                .toList();
    }
}