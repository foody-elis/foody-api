package com.example.foody.mapper.impl;

import com.example.foody.TestDataUtil;
import com.example.foody.builder.DishBuilder;
import com.example.foody.dto.request.DishRequestDTO;
import com.example.foody.dto.request.DishUpdateRequestDTO;
import com.example.foody.dto.response.DishResponseDTO;
import com.example.foody.model.Dish;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DishMapperImplTest {

    @InjectMocks
    private DishMapperImpl dishMapper;

    @Mock
    private DishBuilder dishBuilder;

    @Test
    void dishToDishResponseDTOWhenDishIsNullReturnsNull() {
        // Act
        DishResponseDTO result = dishMapper.dishToDishResponseDTO(null, 0.0);

        // Assert
        assertNull(result);
    }

    @Test
    void dishToDishResponseDTOWhenValidReturnsDTO() {
        // Arrange
        Dish dish = TestDataUtil.createTestDish();
        double averageRating = 4.5;

        // Act
        DishResponseDTO result = dishMapper.dishToDishResponseDTO(dish, averageRating);

        // Assert
        assertNotNull(result);
        assertEquals(dish.getId(), result.getId());
        assertEquals(dish.getName(), result.getName());
        assertEquals(dish.getDescription(), result.getDescription());
        assertEquals(dish.getPrice(), result.getPrice());
        assertEquals(dish.getPhotoUrl(), result.getPhotoUrl());
        assertEquals(dish.getRestaurant().getId(), result.getRestaurantId());
        assertEquals(averageRating, result.getAverageRating());
    }

    @Test
    void dishToDishResponseDTOWhenDishRestaurantIsNullReturnsDTO() {
        // Arrange
        Dish dish = TestDataUtil.createTestDish();
        dish.setRestaurant(null);
        double averageRating = 4.5;

        // Act
        DishResponseDTO result = dishMapper.dishToDishResponseDTO(dish, averageRating);

        // Assert
        assertNotNull(result);
        assertEquals(dish.getId(), result.getId());
        assertEquals(dish.getName(), result.getName());
        assertEquals(dish.getDescription(), result.getDescription());
        assertEquals(dish.getPrice(), result.getPrice());
        assertEquals(dish.getPhotoUrl(), result.getPhotoUrl());
        assertNull(result.getRestaurantId());
        assertEquals(averageRating, result.getAverageRating());
    }

    @Test
    void dishRequestDTOToDishWhenRequestIsNullReturnsNull() {
        // Act
        Dish result = dishMapper.dishRequestDTOToDish(null);

        // Assert
        assertNull(result);
    }

    @Test
    void dishRequestDTOToDishWhenValidReturnsDish() {
        // Arrange
        DishRequestDTO requestDTO = mock(DishRequestDTO.class);
        when(requestDTO.getName()).thenReturn("Test Dish");
        when(requestDTO.getDescription()).thenReturn("Test Description");
        when(requestDTO.getPrice()).thenReturn(BigDecimal.valueOf(10.0));

        Dish dish = mock(Dish.class);
        when(dishBuilder.name("Test Dish")).thenReturn(dishBuilder);
        when(dishBuilder.description("Test Description")).thenReturn(dishBuilder);
        when(dishBuilder.price(BigDecimal.valueOf(10.0))).thenReturn(dishBuilder);
        when(dishBuilder.build()).thenReturn(dish);

        // Act
        Dish result = dishMapper.dishRequestDTOToDish(requestDTO);

        // Assert
        assertNotNull(result);
        verify(dishBuilder).name("Test Dish");
        verify(dishBuilder).description("Test Description");
        verify(dishBuilder).price(BigDecimal.valueOf(10.0));
        verify(dishBuilder).build();
    }

    @Test
    void updateDishFromDishUpdateRequestDTOWhenDishIsNullDoesNothing() {
        // Act
        dishMapper.updateDishFromDishUpdateRequestDTO(null, null);
    }

    @Test
    void updateDishFromDishUpdateRequestDTOWhenUpdateRequestDTOIsNullDoesNothing() {
        // Act
        dishMapper.updateDishFromDishUpdateRequestDTO(mock(Dish.class), null);
    }

    @Test
    void updateDishFromDishUpdateRequestDTOWhenValidUpdatesDish() {
        // Arrange
        Dish dish = mock(Dish.class);
        DishUpdateRequestDTO updateRequestDTO = mock(DishUpdateRequestDTO.class);
        
        when(updateRequestDTO.getName()).thenReturn("Updated Dish");
        when(updateRequestDTO.getDescription()).thenReturn("Updated Description");
        when(updateRequestDTO.getPrice()).thenReturn(BigDecimal.valueOf(15.0));

        // Act
        dishMapper.updateDishFromDishUpdateRequestDTO(dish, updateRequestDTO);

        // Assert
        verify(dish).setName("Updated Dish");
        verify(dish).setDescription("Updated Description");
        verify(dish).setPrice(BigDecimal.valueOf(15.0));
    }
}