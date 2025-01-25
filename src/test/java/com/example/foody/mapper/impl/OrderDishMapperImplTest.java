package com.example.foody.mapper.impl;

import com.example.foody.builder.OrderDishBuilder;
import com.example.foody.dto.request.OrderDishRequestDTO;
import com.example.foody.dto.response.OrderDishResponseDTO;
import com.example.foody.model.Dish;
import com.example.foody.model.order_dish.OrderDish;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderDishMapperImplTest {

    @InjectMocks
    private OrderDishMapperImpl orderDishMapper;

    @Mock
    private OrderDishBuilder orderDishBuilder;

    @Test
    void orderDishToOrderDishResponseDTOWhenOrderDishIsNullReturnsNull() {
        // Act
        OrderDishResponseDTO result = orderDishMapper.orderDishToOrderDishResponseDTO(null);

        // Assert
        assertNull(result);
    }

    @Test
    void orderDishToOrderDishResponseDTOWhenValidReturnsDTO() {
        // Arrange
        OrderDish orderDish = mock(OrderDish.class);
        Dish dish = mock(Dish.class);

        when(orderDish.getDish()).thenReturn(dish);
        when(dish.getId()).thenReturn(1L);
        when(dish.getName()).thenReturn("Test Dish");
        when(dish.getPrice()).thenReturn(BigDecimal.valueOf(10.0));
        when(orderDish.getQuantity()).thenReturn(2);

        // Act
        OrderDishResponseDTO result = orderDishMapper.orderDishToOrderDishResponseDTO(orderDish);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getDishId());
        assertEquals("Test Dish", result.getDishName());
        assertEquals(BigDecimal.valueOf(10.0), result.getPrice());
        assertEquals(2, result.getQuantity());
    }

    @Test
    void orderDishToOrderDishResponseDTOWhenDishIsNullReturnsDTO() {
        // Arrange
        OrderDish orderDish = mock(OrderDish.class);

        when(orderDish.getDish()).thenReturn(null);
        when(orderDish.getQuantity()).thenReturn(2);

        // Act
        OrderDishResponseDTO result = orderDishMapper.orderDishToOrderDishResponseDTO(orderDish);

        // Assert
        assertNotNull(result);
        assertNull(result.getDishId());
        assertNull(result.getDishName());
        assertNull(result.getPrice());
        assertEquals(2, result.getQuantity());
    }

    @Test
    void orderDishRequestDTOToOrderDishWhenRequestIsNullReturnsNull() {
        // Act
        OrderDish result = orderDishMapper.orderDishRequestDTOToOrderDish(null);

        // Assert
        assertNull(result);
    }

    @Test
    void orderDishRequestDTOToOrderDishWhenValidReturnsOrderDish() {
        // Arrange
        OrderDishRequestDTO requestDTO = mock(OrderDishRequestDTO.class);
        when(requestDTO.getQuantity()).thenReturn(2);

        OrderDish orderDish = mock(OrderDish.class);
        when(orderDishBuilder.quantity(2)).thenReturn(orderDishBuilder);
        when(orderDishBuilder.build()).thenReturn(orderDish);

        // Act
        OrderDish result = orderDishMapper.orderDishRequestDTOToOrderDish(requestDTO);

        // Assert
        assertNotNull(result);
        verify(orderDishBuilder).quantity(2);
        verify(orderDishBuilder).build();
    }

    @Test
    void orderDishesToOrderDishResponseDTOsWhenOrderDishesIsNullReturnsNull() {
        // Act
        List<OrderDishResponseDTO> result = orderDishMapper.orderDishesToOrderDishResponseDTOs(null);

        // Assert
        assertNull(result);
    }

    @Test
    void orderDishesToOrderDishResponseDTOsWhenValidReturnsDTOList() {
        // Arrange
        OrderDish orderDish = mock(OrderDish.class);
        Dish dish = mock(Dish.class);

        when(orderDish.getDish()).thenReturn(dish);
        when(dish.getId()).thenReturn(1L);
        when(dish.getName()).thenReturn("Test Dish");
        when(dish.getPrice()).thenReturn(BigDecimal.valueOf(10.0));
        when(orderDish.getQuantity()).thenReturn(2);

        List<OrderDish> orderDishes = Collections.singletonList(orderDish);

        // Act
        List<OrderDishResponseDTO> result = orderDishMapper.orderDishesToOrderDishResponseDTOs(orderDishes);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.getFirst().getDishId());
        assertEquals("Test Dish", result.getFirst().getDishName());
        assertEquals(BigDecimal.valueOf(10.0), result.getFirst().getPrice());
        assertEquals(2, result.getFirst().getQuantity());
    }
}