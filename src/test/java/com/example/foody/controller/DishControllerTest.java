package com.example.foody.controller;

import com.example.foody.TestDataUtil;
import com.example.foody.dto.request.DishRequestDTO;
import com.example.foody.dto.request.DishUpdateRequestDTO;
import com.example.foody.dto.response.DishResponseDTO;
import com.example.foody.exceptions.entity.EntityNotFoundException;
import com.example.foody.service.DishService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

/**
 * Test class for the endpoints in the {@link DishController} class using mock services.
 */
@ExtendWith(MockitoExtension.class)
public class DishControllerTest {

    @InjectMocks
    private DishController dishController;

    @Mock
    private DishService dishService;

    @Test
    void saveDishWhenValidRequestReturnsCreatedResponse() {
        // Arrange
        DishRequestDTO requestDTO = TestDataUtil.createTestDishRequestDTO();
        DishResponseDTO responseDTO = TestDataUtil.createTestDishResponseDTO();

        when(dishService.save(requestDTO)).thenReturn(responseDTO);

        // Act
        ResponseEntity<DishResponseDTO> response = dishController.saveDish(requestDTO);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(responseDTO, response.getBody());
    }

    @Test
    void getDishesWhenCalledReturnsOkResponse() {
        // Arrange
        List<DishResponseDTO> responseDTOs =
                Collections.singletonList(TestDataUtil.createTestDishResponseDTO());

        when(dishService.findAll()).thenReturn(responseDTOs);

        // Act
        ResponseEntity<List<DishResponseDTO>> response = dishController.getDishes();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseDTOs, response.getBody());
    }

    @Test
    void getDishByIdWhenValidIdReturnsOkResponse() {
        // Arrange
        DishResponseDTO responseDTO = TestDataUtil.createTestDishResponseDTO();

        when(dishService.findById(1L)).thenReturn(responseDTO);

        // Act
        ResponseEntity<DishResponseDTO> response = dishController.getDishById(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseDTO, response.getBody());
    }

    @Test
    void getDishByIdWhenInvalidIdThrowsEntityNotFoundException() {
        // Arrange
        when(dishService.findById(1L)).thenThrow(new EntityNotFoundException("dish", "id", 1L));

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> dishController.getDishById(1L));
    }

    @Test
    void getDishesByRestaurantWhenValidIdReturnsOkResponse() {
        // Arrange
        List<DishResponseDTO> responseDTOs =
                Collections.singletonList(TestDataUtil.createTestDishResponseDTO());

        when(dishService.findAllByRestaurant(1L)).thenReturn(responseDTOs);

        // Act
        ResponseEntity<List<DishResponseDTO>> response = dishController.getDishesByRestaurant(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseDTOs, response.getBody());
    }

    @Test
    void updateDishWhenValidRequestReturnsOkResponse() {
        // Arrange
        DishUpdateRequestDTO requestDTO = TestDataUtil.createTestDishUpdateRequestDTO();
        DishResponseDTO responseDTO = TestDataUtil.createTestDishResponseDTO();

        when(dishService.update(1L, requestDTO)).thenReturn(responseDTO);

        // Act
        ResponseEntity<DishResponseDTO> response = dishController.updateDish(1L, requestDTO);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseDTO, response.getBody());
    }

    @Test
    void removeDishWhenValidIdRemovesDish() {
        // Arrange
        when(dishService.remove(1L)).thenReturn(true);

        // Act
        ResponseEntity<Void> response = dishController.removeDish(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(dishService, times(1)).remove(1L);
    }
}