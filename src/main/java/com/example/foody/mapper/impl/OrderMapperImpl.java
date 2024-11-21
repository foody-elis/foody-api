package com.example.foody.mapper.impl;

import com.example.foody.builder.OrderBuilder;
import com.example.foody.dto.request.OrderRequestDTO;
import com.example.foody.dto.response.OrderResponseDTO;
import com.example.foody.mapper.DishMapper;
import com.example.foody.mapper.OrderDishMapper;
import com.example.foody.mapper.OrderMapper;
import com.example.foody.model.Order;
import com.example.foody.model.Restaurant;
import com.example.foody.model.user.BuyerUser;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class OrderMapperImpl implements OrderMapper {
    private final OrderBuilder orderBuilder;
    private final OrderDishMapper orderDishMapper;

    public OrderMapperImpl(OrderBuilder orderBuilder, OrderDishMapper orderDishMapper) {
        this.orderBuilder = orderBuilder;
        this.orderDishMapper = orderDishMapper;
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
        orderResponseDTO.setBuyerId(orderBuyerId(order));
        orderResponseDTO.setRestaurantId(orderRestaurantId(order));
        orderResponseDTO.setId(order.getId());
        orderResponseDTO.setTableNumber(order.getTableNumber());
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
                .tableNumber(orderRequestDTO.getTableNumber())
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

    private long orderBuyerId(Order order) {
        if (order == null) {
            return 0L;
        }
        BuyerUser buyer = order.getBuyer();
        if (buyer == null) {
            return 0L;
        }
        return buyer.getId();
    }

    private long orderRestaurantId(Order order) {
        if (order == null) {
            return 0L;
        }
        Restaurant restaurant = order.getRestaurant();
        if (restaurant == null) {
            return 0L;
        }
        return restaurant.getId();
    }
}
