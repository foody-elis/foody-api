package com.example.foody.mapper.impl;

import com.example.foody.builder.OrderBuilder;
import com.example.foody.dto.request.OrderRequestDTO;
import com.example.foody.dto.response.OrderResponseDTO;
import com.example.foody.helper.UserHelper;
import com.example.foody.mapper.OrderDishMapper;
import com.example.foody.mapper.OrderMapper;
import com.example.foody.mapper.RestaurantMapper;
import com.example.foody.model.Order;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the {@link OrderMapper} interface.
 * <p>
 * Provides methods to convert between {@link Order} entities and DTOs.
 */
@Component
@AllArgsConstructor
public class OrderMapperImpl implements OrderMapper {

    private final OrderBuilder orderBuilder;
    private final OrderDishMapper orderDishMapper;
    private final UserHelper userHelper;
    private final RestaurantMapper restaurantMapper;

    /**
     * {@inheritDoc}
     * <p>
     * Converts an {@link Order} entity to an {@link OrderResponseDTO}.
     *
     * @param order the Order entity to convert
     * @return the converted OrderResponseDTO
     */
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

    /**
     * {@inheritDoc}
     * <p>
     * Converts an {@link OrderRequestDTO} to an {@link Order} entity.
     *
     * @param orderRequestDTO the OrderRequestDTO to convert
     * @return the converted Order entity
     */
    @Override
    public Order orderRequestDTOToOrder(OrderRequestDTO orderRequestDTO) {
        if (orderRequestDTO == null) {
            return null;
        }

        return orderBuilder
                .tableCode(orderRequestDTO.getTableCode())
                .build();
    }

    /**
     * {@inheritDoc}
     * <p>
     * Converts a list of {@link Order} entities to a list of {@link OrderResponseDTO} objects.
     *
     * @param orders the list of Order entities to convert
     * @return the list of converted OrderResponseDTO objects
     */
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