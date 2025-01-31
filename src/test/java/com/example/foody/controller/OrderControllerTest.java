package com.example.foody.controller;

import com.example.foody.TestDataUtil;
import com.example.foody.dto.request.OrderRequestDTO;
import com.example.foody.dto.response.OrderResponseDTO;
import com.example.foody.exceptions.entity.EntityNotFoundException;
import com.example.foody.model.user.User;
import com.example.foody.service.OrderService;
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
 * Test class for the endpoints in the {@link OrderController} class using mock services.
 */
@ExtendWith(MockitoExtension.class)
public class OrderControllerTest {

    @InjectMocks
    private OrderController orderController;

    @Mock
    private OrderService orderService;

    @Test
    void saveOrderWhenValidRequestReturnsCreatedResponse() {
        // Arrange
        OrderRequestDTO requestDTO = TestDataUtil.createTestOrderRequestDTO();
        OrderResponseDTO responseDTO = TestDataUtil.createTestOrderResponseDTO();

        when(orderService.save(requestDTO)).thenReturn(responseDTO);

        // Act
        ResponseEntity<OrderResponseDTO> response = orderController.saveOrder(requestDTO);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(responseDTO, response.getBody());
    }

    @Test
    void getOrdersWhenCalledReturnsOkResponse() {
        // Arrange
        List<OrderResponseDTO> responseDTOs =
                Collections.singletonList(TestDataUtil.createTestOrderResponseDTO());

        when(orderService.findAll()).thenReturn(responseDTOs);

        // Act
        ResponseEntity<List<OrderResponseDTO>> response = orderController.getOrders();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseDTOs, response.getBody());
    }

    @Test
    void getOrderByIdWhenValidIdReturnsOkResponse() {
        // Arrange
        OrderResponseDTO responseDTO = TestDataUtil.createTestOrderResponseDTO();

        when(orderService.findById(1L)).thenReturn(responseDTO);

        // Act
        ResponseEntity<OrderResponseDTO> response = orderController.getOrderById(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseDTO, response.getBody());
    }

    @Test
    void getOrderByIdWhenInvalidIdThrowsEntityNotFoundException() {
        // Arrange
        when(orderService.findById(1L)).thenThrow(new EntityNotFoundException("order", "id", 1L));

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> orderController.getOrderById(1L));
    }

    @Test
    void getOrdersByBuyerWhenCalledReturnsOkResponse() {
        // Arrange
        List<OrderResponseDTO> responseDTOs =
                Collections.singletonList(TestDataUtil.createTestOrderResponseDTO());
        User buyer = TestDataUtil.createTestCustomerUser();

        when(orderService.findAllByBuyer(buyer.getId())).thenReturn(responseDTOs);

        // Act
        ResponseEntity<List<OrderResponseDTO>> response = orderController.getOrdersByBuyer(buyer);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseDTOs, response.getBody());
    }

    @Test
    void getOrdersByRestaurantWhenValidIdReturnsOkResponse() {
        // Arrange
        List<OrderResponseDTO> responseDTOs =
                Collections.singletonList(TestDataUtil.createTestOrderResponseDTO());

        when(orderService.findAllByRestaurant(1L)).thenReturn(responseDTOs);

        // Act
        ResponseEntity<List<OrderResponseDTO>> response = orderController.getOrdersByRestaurant(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseDTOs, response.getBody());
    }

    @Test
    void getORdersByRestaurantAndInProgressWhenValidIdReturnsOkResponse() {
        // Arrange
        List<OrderResponseDTO> responseDTOs =
                Collections.singletonList(TestDataUtil.createTestOrderResponseDTO());

        when(orderService.findAllByRestaurantAndInProgress(1L)).thenReturn(responseDTOs);

        // Act
        ResponseEntity<List<OrderResponseDTO>> response = orderController.getOrdersByRestaurantAndInProgress(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseDTOs, response.getBody());
    }

    @Test
    void payOrderWhenValidIdReturnsOkResponse() {
        // Arrange
        OrderResponseDTO responseDTO = TestDataUtil.createTestOrderResponseDTO();

        when(orderService.payById(1L)).thenReturn(responseDTO);

        // Act
        ResponseEntity<OrderResponseDTO> response = orderController.payOrder(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseDTO, response.getBody());
    }

    @Test
    void prepareOrderWhenValidIdReturnsOkResponse() {
        // Arrange
        OrderResponseDTO responseDTO = TestDataUtil.createTestOrderResponseDTO();

        when(orderService.prepareById(1L)).thenReturn(responseDTO);

        // Act
        ResponseEntity<OrderResponseDTO> response = orderController.prepareOrder(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseDTO, response.getBody());
    }

    @Test
    void completeOrderWhenValidIdReturnsOkResponse() {
        // Arrange
        OrderResponseDTO responseDTO = TestDataUtil.createTestOrderResponseDTO();

        when(orderService.completeById(1L)).thenReturn(responseDTO);

        // Act
        ResponseEntity<OrderResponseDTO> response = orderController.completeOrder(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseDTO, response.getBody());
    }

    @Test
    void removeOrderWhenValidIdRemovesOrder() {
        // Arrange
        when(orderService.remove(1L)).thenReturn(true);

        // Act
        ResponseEntity<Void> response = orderController.removeOrder(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(orderService, times(1)).remove(1L);
    }
}