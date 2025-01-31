package com.example.foody.controller;

import com.example.foody.TestDataUtil;
import com.example.foody.dto.request.RestaurantRequestDTO;
import com.example.foody.dto.response.DetailedRestaurantResponseDTO;
import com.example.foody.dto.response.RestaurantResponseDTO;
import com.example.foody.exceptions.entity.EntityNotFoundException;
import com.example.foody.service.RestaurantService;
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
import static org.mockito.Mockito.when;

/**
 * Test class for the endpoints in the {@link RestaurantController} class using mock services.
 */
@ExtendWith(MockitoExtension.class)
public class RestaurantControllerTest {

    @InjectMocks
    private RestaurantController restaurantController;

    @Mock
    private RestaurantService restaurantService;

    @Test
    void saveRestaurantWhenValidRequestReturnsCreatedResponse() {
        // Arrange
        RestaurantRequestDTO requestDTO = TestDataUtil.createTestRestaurantRequestDTO();
        RestaurantResponseDTO responseDTO = TestDataUtil.createTestRestaurantResponseDTO();

        when(restaurantService.save(requestDTO)).thenReturn(responseDTO);

        // Act
        ResponseEntity<RestaurantResponseDTO> response = restaurantController.saveRestaurant(requestDTO);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(responseDTO, response.getBody());
    }

    @Test
    void getRestaurantsWhenCalledReturnsOkResponse() {
        // Arrange
        List<DetailedRestaurantResponseDTO> responseDTOs =
                Collections.singletonList(TestDataUtil.createTestDetailedRestaurantResponseDTO());

        when(restaurantService.findAll()).thenReturn(responseDTOs);

        // Act
        ResponseEntity<List<DetailedRestaurantResponseDTO>> response = restaurantController.getRestaurants();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseDTOs, response.getBody());
    }

    @Test
    void getRestaurantByIdWhenExistsReturnsOkResponse() {
        // Arrange
        DetailedRestaurantResponseDTO responseDTO = TestDataUtil.createTestDetailedRestaurantResponseDTO();

        when(restaurantService.findById(1L)).thenReturn(responseDTO);

        // Act
        ResponseEntity<DetailedRestaurantResponseDTO> response = restaurantController.getRestaurantById(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseDTO, response.getBody());
    }

    @Test
    void getRestaurantByIdWhenDoesNotExistReturnsNotFoundResponse() {
        // Arrange
        when(restaurantService.findById(1L)).thenThrow(new EntityNotFoundException("restaurant", "id", 1L));

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> restaurantController.getRestaurantById(1L));
    }

    @Test
    void getRestaurantByRestaurateurWhenExistsReturnsOkResponse() {
        // Arrange
        DetailedRestaurantResponseDTO responseDTO = TestDataUtil.createTestDetailedRestaurantResponseDTO();

        when(restaurantService.findByRestaurateur(1L)).thenReturn(responseDTO);

        // Act
        ResponseEntity<DetailedRestaurantResponseDTO> response =
                restaurantController.getRestaurantByRestaurateur(TestDataUtil.createTestRestaurateurUser());

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseDTO, response.getBody());
    }

    @Test
    void getRestaurantByCategoryWhenExistsReturnsOkResponse() {
        // Arrange
        List<DetailedRestaurantResponseDTO> responseDTOs =
                Collections.singletonList(TestDataUtil.createTestDetailedRestaurantResponseDTO());

        when(restaurantService.findAllByCategory(1L)).thenReturn(responseDTOs);

        // Act
        ResponseEntity<List<DetailedRestaurantResponseDTO>> response =
                restaurantController.getRestaurantsByCategory(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseDTOs, response.getBody());
    }

    @Test
    void approveRestaurantWhenValidIdReturnsOkResponse() {
        // Arrange
        DetailedRestaurantResponseDTO responseDTO = TestDataUtil.createTestDetailedRestaurantResponseDTO();

        when(restaurantService.approveById(1L)).thenReturn(responseDTO);

        // Act
        ResponseEntity<DetailedRestaurantResponseDTO> response = restaurantController.approveRestaurant(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseDTO, response.getBody());
    }

    @Test
    void updateRestaurantWhenValidRequestReturnsOkResponse() {
        // Arrange
        DetailedRestaurantResponseDTO responseDTO = TestDataUtil.createTestDetailedRestaurantResponseDTO();
        RestaurantRequestDTO requestDTO = TestDataUtil.createTestRestaurantRequestDTO();

        when(restaurantService.update(1L, requestDTO)).thenReturn(responseDTO);

        // Act
        ResponseEntity<DetailedRestaurantResponseDTO> response =
                restaurantController.updateRestaurant(1L, requestDTO);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseDTO, response.getBody());
    }

    @Test
    void removeRestaurantWhenValidIdRemovesRestaurant() throws Exception {
        // Arrange
        when(restaurantService.remove(1L)).thenReturn(true);

        // Act
        ResponseEntity<Void> response = restaurantController.removeRestaurant(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(restaurantService, times(1)).remove(1L);
    }
}