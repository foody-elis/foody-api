package com.example.foody.mapper.impl;

import com.example.foody.builder.OrderBuilder;
import com.example.foody.dto.request.OrderRequestDTO;
import com.example.foody.dto.response.OrderResponseDTO;
import com.example.foody.dto.response.UserResponseDTO;
import com.example.foody.mapper.OrderDishMapper;
import com.example.foody.mapper.OrderMapper;
import com.example.foody.mapper.RestaurantMapper;
import com.example.foody.mapper.UserMapper;
import com.example.foody.model.Order;
import com.example.foody.model.user.CustomerUser;
import com.example.foody.model.user.WaiterUser;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class OrderMapperImpl implements OrderMapper {
    private final OrderBuilder orderBuilder;
    private final OrderDishMapper orderDishMapper;
    private final UserMapper<CustomerUser> customerUserMapper;
    private final UserMapper<WaiterUser> waiterUserMapper;
    private final RestaurantMapper restaurantMapper;

    public OrderMapperImpl(OrderBuilder orderBuilder, OrderDishMapper orderDishMapper, UserMapper<CustomerUser> customerUserMapper, UserMapper<WaiterUser> waiterUserMapper, RestaurantMapper restaurantMapper) {
        this.orderBuilder = orderBuilder;
        this.orderDishMapper = orderDishMapper;
        this.customerUserMapper = customerUserMapper;
        this.waiterUserMapper = waiterUserMapper;
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
        orderResponseDTO.setBuyer(orderBuyerUserResponseDTO(order));
        orderResponseDTO.setRestaurant(
                restaurantMapper.restaurantToRestaurantResponseDTO(order.getRestaurant())
        );
        orderResponseDTO.setId(order.getId());
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

    private UserResponseDTO orderBuyerUserResponseDTO(Order order) {
        return switch (order.getBuyer().getUser()) {
            case CustomerUser customerUser -> customerUserMapper.userToUserResponseDTO(customerUser);
            case WaiterUser waiterUser -> waiterUserMapper.userToUserResponseDTO(waiterUser);
            default -> null;
        };
    }
}