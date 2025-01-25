package com.example.foody.mapper.impl;

import com.example.foody.builder.OrderBuilder;
import com.example.foody.dto.request.OrderRequestDTO;
import com.example.foody.dto.response.OrderResponseDTO;
import com.example.foody.dto.response.RestaurantResponseDTO;
import com.example.foody.dto.response.UserResponseDTO;
import com.example.foody.helper.UserHelper;
import com.example.foody.mapper.OrderDishMapper;
import com.example.foody.mapper.RestaurantMapper;
import com.example.foody.model.Order;
import com.example.foody.model.Restaurant;
import com.example.foody.model.order_dish.OrderDish;
import com.example.foody.model.user.BuyerUser;
import com.example.foody.model.user.User;
import com.example.foody.utils.enums.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderMapperImplTest {

    @InjectMocks
    private OrderMapperImpl orderMapper;

    @Mock
    private OrderBuilder orderBuilder;

    @Mock
    private OrderDishMapper orderDishMapper;

    @Mock
    private UserHelper userHelper;

    @Mock
    private RestaurantMapper restaurantMapper;

    @Test
    void orderToOrderResponseDTOWhenOrderIsNullReturnsNull() {
        // Act
        OrderResponseDTO result = orderMapper.orderToOrderResponseDTO(null);

        // Assert
        assertNull(result);
    }

    @Test
    void orderToOrderResponseDTOWhenValidReturnsDTO() {
        // Arrange
        Order order = mock(Order.class);
        BuyerUser buyer = mock(BuyerUser.class);
        User user = mock(User.class);
        Restaurant restaurant = mock(Restaurant.class);
        OrderDish orderDish = mock(OrderDish.class);

        when(order.getId()).thenReturn(1L);
        when(order.getBuyer()).thenReturn(buyer);
        when(buyer.getUser()).thenReturn(user);
        when(order.getRestaurant()).thenReturn(restaurant);
        when(order.getOrderDishes()).thenReturn(Collections.singletonList(orderDish));
        when(order.getCreatedAt()).thenReturn(LocalDateTime.of(2025, 1, 1, 0, 0));
        when(order.getTableCode()).thenReturn("A1");
        when(order.getStatus()).thenReturn(OrderStatus.CREATED);

        when(orderDishMapper.orderDishesToOrderDishResponseDTOs(anyList())).thenReturn(Collections.emptyList());
        when(userHelper.buildUserResponseDTO(user)).thenReturn(mock(UserResponseDTO.class));
        when(restaurantMapper.restaurantToRestaurantResponseDTO(restaurant)).thenReturn(mock(RestaurantResponseDTO.class));

        // Act
        OrderResponseDTO result = orderMapper.orderToOrderResponseDTO(order);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(LocalDateTime.of(2025, 1, 1, 0, 0), result.getCreatedAt());
        assertEquals("A1", result.getTableCode());
        assertEquals(OrderStatus.CREATED.name(), result.getStatus());
        verify(orderDishMapper).orderDishesToOrderDishResponseDTOs(anyList());
        verify(userHelper).buildUserResponseDTO(user);
        verify(restaurantMapper).restaurantToRestaurantResponseDTO(restaurant);
    }

    @Test
    void orderToOrderResponseDTOWhenOrderStatusIsNullReturnsDTO() {
        // Arrange
        Order order = mock(Order.class);
        BuyerUser buyer = mock(BuyerUser.class);
        User user = mock(User.class);
        Restaurant restaurant = mock(Restaurant.class);
        OrderDish orderDish = mock(OrderDish.class);

        when(order.getId()).thenReturn(1L);
        when(order.getBuyer()).thenReturn(buyer);
        when(buyer.getUser()).thenReturn(user);
        when(order.getRestaurant()).thenReturn(restaurant);
        when(order.getOrderDishes()).thenReturn(Collections.singletonList(orderDish));
        when(order.getCreatedAt()).thenReturn(LocalDateTime.of(2025, 1, 1, 0, 0));
        when(order.getTableCode()).thenReturn("A1");
        when(order.getStatus()).thenReturn(null);

        when(orderDishMapper.orderDishesToOrderDishResponseDTOs(anyList())).thenReturn(Collections.emptyList());
        when(userHelper.buildUserResponseDTO(user)).thenReturn(mock(UserResponseDTO.class));
        when(restaurantMapper.restaurantToRestaurantResponseDTO(restaurant)).thenReturn(mock(RestaurantResponseDTO.class));

        // Act
        OrderResponseDTO result = orderMapper.orderToOrderResponseDTO(order);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(LocalDateTime.of(2025, 1, 1, 0, 0), result.getCreatedAt());
        assertEquals("A1", result.getTableCode());
        assertNull(result.getStatus());
        verify(orderDishMapper).orderDishesToOrderDishResponseDTOs(anyList());
        verify(userHelper).buildUserResponseDTO(user);
        verify(restaurantMapper).restaurantToRestaurantResponseDTO(restaurant);
    }

    @Test
    void orderRequestDTOToOrderWhenRequestIsNullReturnsNull() {
        // Act
        Order result = orderMapper.orderRequestDTOToOrder(null);

        // Assert
        assertNull(result);
    }

    @Test
    void orderRequestDTOToOrderWhenValidReturnsOrder() {
        // Arrange
        OrderRequestDTO requestDTO = mock(OrderRequestDTO.class);
        when(requestDTO.getTableCode()).thenReturn("A1");

        Order order = mock(Order.class);
        when(orderBuilder.tableCode("A1")).thenReturn(orderBuilder);
        when(orderBuilder.build()).thenReturn(order);

        // Act
        Order result = orderMapper.orderRequestDTOToOrder(requestDTO);

        // Assert
        assertNotNull(result);
        verify(orderBuilder).tableCode("A1");
        verify(orderBuilder).build();
    }

    @Test
    void ordersToOrderResponseDTOsWhenOrdersIsNullReturnsNull() {
        // Act
        List<OrderResponseDTO> result = orderMapper.ordersToOrderResponseDTOs(null);

        // Assert
        assertNull(result);
    }

    @Test
    void ordersToOrderResponseDTOsWhenValidReturnsDTOList() {
        // Arrange
        Order order = mock(Order.class);
        BuyerUser buyer = mock(BuyerUser.class);
        User user = mock(User.class);

        when(order.getBuyer()).thenReturn(buyer);
        when(buyer.getUser()).thenReturn(user);
        when(order.getId()).thenReturn(1L);
        when(order.getCreatedAt()).thenReturn(LocalDateTime.of(2025, 1, 1, 0, 0));
        when(order.getTableCode()).thenReturn("A1");
        when(order.getStatus()).thenReturn(OrderStatus.CREATED);

        Restaurant restaurant = mock(Restaurant.class);
        OrderDish orderDish = mock(OrderDish.class);
        when(order.getRestaurant()).thenReturn(restaurant);
        when(order.getOrderDishes()).thenReturn(Collections.singletonList(orderDish));

        when(orderDishMapper.orderDishesToOrderDishResponseDTOs(anyList())).thenReturn(Collections.emptyList());
        when(userHelper.buildUserResponseDTO(user)).thenReturn(mock(UserResponseDTO.class));
        when(restaurantMapper.restaurantToRestaurantResponseDTO(restaurant)).thenReturn(mock(RestaurantResponseDTO.class));

        List<Order> orders = Collections.singletonList(order);

        // Act
        List<OrderResponseDTO> result = orderMapper.ordersToOrderResponseDTOs(orders);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals(LocalDateTime.of(2025, 1, 1, 0, 0), result.get(0).getCreatedAt());
        assertEquals("A1", result.get(0).getTableCode());
        assertEquals(OrderStatus.CREATED.name(), result.get(0).getStatus());
        verify(orderDishMapper).orderDishesToOrderDishResponseDTOs(anyList());
        verify(userHelper).buildUserResponseDTO(user);
        verify(restaurantMapper).restaurantToRestaurantResponseDTO(restaurant);
    }
}