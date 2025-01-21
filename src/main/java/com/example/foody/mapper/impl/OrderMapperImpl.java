package com.example.foody.mapper.impl;

import com.example.foody.builder.OrderBuilder;
import com.example.foody.dto.request.OrderRequestDTO;
import com.example.foody.dto.response.OrderResponseDTO;
import com.example.foody.helper.UserHelper;
import com.example.foody.mapper.OrderDishMapper;
import com.example.foody.mapper.OrderMapper;
import com.example.foody.mapper.RestaurantMapper;
import com.example.foody.model.Order;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class OrderMapperImpl implements OrderMapper {
    private final OrderBuilder orderBuilder;
    private final OrderDishMapper orderDishMapper;
    private final UserHelper userHelper;
    private final RestaurantMapper restaurantMapper;

    public OrderMapperImpl(OrderBuilder orderBuilder, OrderDishMapper orderDishMapper, UserHelper userHelper, RestaurantMapper restaurantMapper) {
        this.orderBuilder = orderBuilder;
        this.orderDishMapper = orderDishMapper;
        this.userHelper = userHelper;
        this.restaurantMapper = restaurantMapper;
    }

    @Override
    public OrderResponseDTO orderToOrderResponseDTO(Order order) {
        if (order == null) {
            return null;
        }

        OrderResponseDTO orderResponseDTO = new OrderResponseDTO();

        orderResponseDTO.setOrderDishes(
                orderDishMapper.orderDishesToOrderDishResponseDTOs(order.getOrderDishes())
        );
        orderResponseDTO.setBuyer(
                userHelper.buildUserResponseDTO(order.getBuyer().getUser())
        );
        orderResponseDTO.setRestaurant(
                restaurantMapper.restaurantToRestaurantResponseDTO(order.getRestaurant())
        );
        orderResponseDTO.setId(order.getId());
        orderResponseDTO.setCreatedAt(order.getCreatedAt());
        orderResponseDTO.setTableCode(order.getTableCode());
        if (order.getStatus() != null) {
            orderResponseDTO.setStatus(order.getStatus().name());
        }

        return orderResponseDTO;
    }

    @Override
    public Order orderRequestDTOToOrder(OrderRequestDTO orderRequestDTO) {
        if (orderRequestDTO == null) {
            return null;
        }

        return orderBuilder
                .tableCode(orderRequestDTO.getTableCode())
                .build();
    }

    @Override
    public List<OrderResponseDTO> ordersToOrderResponseDTOs(List<Order> orders) {
        if (orders == null) {
            return null;
        }

        List<OrderResponseDTO> list = new ArrayList<>(orders.size());
        orders.forEach(order -> list.add(orderToOrderResponseDTO(order)));

        return list;
    }
}