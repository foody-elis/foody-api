package com.example.foody.mapper.impl;

import com.example.foody.builder.OrderDishBuilder;
import com.example.foody.dto.request.OrderDishRequestDTO;
import com.example.foody.dto.response.OrderDishResponseDTO;
import com.example.foody.mapper.OrderDishMapper;
import com.example.foody.model.Dish;
import com.example.foody.model.order_dish.OrderDish;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the {@link OrderDishMapper} interface.
 * <p>
 * Provides methods to convert between {@link OrderDish} entities and DTOs.
 */
@Component
@AllArgsConstructor
public class OrderDishMapperImpl implements OrderDishMapper {

    private final OrderDishBuilder orderDishBuilder;

    /**
     * {@inheritDoc}
     * <p>
     * Converts an {@link OrderDish} entity to an {@link OrderDishResponseDTO}.
     *
     * @param orderDish the OrderDish entity to convert
     * @return the converted OrderDishResponseDTO
     */
    @Override
    public OrderDishResponseDTO orderDishToOrderDishResponseDTO(OrderDish orderDish) {
        if (orderDish == null) {
            return null;
        }

        OrderDishResponseDTO orderDishResponseDTO = new OrderDishResponseDTO();

        orderDishResponseDTO.setDishId(orderDishDishId(orderDish));
        orderDishResponseDTO.setDishName(orderDishDishName(orderDish));
        orderDishResponseDTO.setPrice(orderDishDishPrice(orderDish));
        orderDishResponseDTO.setQuantity(orderDish.getQuantity());

        return orderDishResponseDTO;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Converts an {@link OrderDishRequestDTO} to an {@link OrderDish} entity.
     *
     * @param orderDishRequestDTO the OrderDishRequestDTO to convert
     * @return the converted OrderDish entity
     */
    @Override
    public OrderDish orderDishRequestDTOToOrderDish(OrderDishRequestDTO orderDishRequestDTO) {
        if (orderDishRequestDTO == null) {
            return null;
        }

        return orderDishBuilder
                .quantity(orderDishRequestDTO.getQuantity())
                .build();
    }

    /**
     * {@inheritDoc}
     * <p>
     * Converts a list of {@link OrderDish} entities to a list of {@link OrderDishResponseDTO} objects.
     *
     * @param orderDishes the list of OrderDish entities to convert
     * @return the list of converted OrderDishResponseDTO objects
     */
    @Override
    public List<OrderDishResponseDTO> orderDishesToOrderDishResponseDTOs(List<OrderDish> orderDishes) {
        if (orderDishes == null) {
            return null;
        }

        List<OrderDishResponseDTO> list = new ArrayList<>(orderDishes.size());
        orderDishes.forEach(orderDish -> list.add(orderDishToOrderDishResponseDTO(orderDish)));

        return list;
    }

    /**
     * Retrieves the dish ID from an {@link OrderDish} entity.
     *
     * @param orderDish the OrderDish entity
     * @return the dish ID, or null if not available
     */
    private Long orderDishDishId(OrderDish orderDish) {
        if (orderDish == null) {
            return null;
        }
        Dish dish = orderDish.getDish();
        if (dish == null) {
            return null;
        }
        return dish.getId();
    }

    /**
     * Retrieves the dish name from an {@link OrderDish} entity.
     *
     * @param orderDish the OrderDish entity
     * @return the dish name, or null if not available
     */
    private String orderDishDishName(OrderDish orderDish) {
        if (orderDish == null) {
            return null;
        }
        Dish dish = orderDish.getDish();
        if (dish == null) {
            return null;
        }
        return dish.getName();
    }

    /**
     * Retrieves the dish price from an {@link OrderDish} entity.
     *
     * @param orderDish the OrderDish entity
     * @return the dish price, or null if not available
     */
    private BigDecimal orderDishDishPrice(OrderDish orderDish) {
        if (orderDish == null) {
            return null;
        }
        Dish dish = orderDish.getDish();
        if (dish == null) {
            return null;
        }
        return dish.getPrice();
    }
}