package com.example.foody.helper.impl;

import com.example.foody.TestDataUtil;
import com.example.foody.dto.response.DishResponseDTO;
import com.example.foody.mapper.DishMapper;
import com.example.foody.model.Dish;
import com.example.foody.repository.ReviewRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DishHelperImplTest {

    @InjectMocks
    private DishHelperImpl dishHelperImpl;

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private DishMapper dishMapper;

    @Test
    void buildDishResponseDTOWhenValidReturnsDishResponseDTO() {
        // Arrange
        Dish dish = TestDataUtil.createTestDish();
        DishResponseDTO dishResponseDTO = TestDataUtil.createTestDishResponseDTO();
        double averageRating = 4.5;

        when(reviewRepository.findAverageRatingByDish_Id(dish.getId())).thenReturn(averageRating);
        when(dishMapper.dishToDishResponseDTO(dish, averageRating)).thenReturn(dishResponseDTO);

        // Act
        DishResponseDTO result = dishHelperImpl.buildDishResponseDTO(dish);

        // Assert
        assertNotNull(result);
        assertEquals(dishResponseDTO, result);
        verify(reviewRepository).findAverageRatingByDish_Id(dish.getId());
        verify(dishMapper).dishToDishResponseDTO(dish, averageRating);
    }

    @Test
    void buildDishResponseDTOsWhenValidReturnsListOfDishResponseDTO() {
        // Arrange
        Dish dish = TestDataUtil.createTestDish();
        DishResponseDTO dishResponseDTO = TestDataUtil.createTestDishResponseDTO();
        double averageRating = 4.5;

        when(reviewRepository.findAverageRatingByDish_Id(dish.getId())).thenReturn(averageRating);
        when(dishMapper.dishToDishResponseDTO(dish, averageRating)).thenReturn(dishResponseDTO);

        // Act
        List<DishResponseDTO> result = dishHelperImpl.buildDishResponseDTOs(List.of(dish));

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(dishResponseDTO, result.getFirst());
        verify(reviewRepository).findAverageRatingByDish_Id(dish.getId());
        verify(dishMapper).dishToDishResponseDTO(dish, averageRating);
    }
}