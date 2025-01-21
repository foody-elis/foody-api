package com.example.foody.mapper.impl;

import com.example.foody.builder.OrderDishBuilder;
import com.example.foody.dto.request.OrderDishRequestDTO;
import com.example.foody.dto.response.OrderDishResponseDTO;
import com.example.foody.mapper.OrderDishMapper;
import com.example.foody.model.Dish;
import com.example.foody.model.order_dish.OrderDish;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class OrderDishMapperImpl implements OrderDishMapper {
    private final OrderDishBuilder orderDishBuilder;

    public OrderDishMapperImpl(OrderDishBuilder orderDishBuilder) {
        this.orderDishBuilder = orderDishBuilder;
    }

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

    @Override
    public OrderDish orderDishRequestDTOToOrderDish(OrderDishRequestDTO orderDishRequestDTO) {
        if (orderDishRequestDTO == null) {
            return null;
        }

        return orderDishBuilder
                .quantity(orderDishRequestDTO.getQuantity())
                .build();
    }

    @Override
    public List<OrderDishResponseDTO> orderDishesToOrderDishResponseDTOs(List<OrderDish> orderDishes) {
        if (orderDishes == null) {
            return null;
        }

        List<OrderDishResponseDTO> list = new ArrayList<>(orderDishes.size());
        orderDishes.forEach(orderDish -> list.add(orderDishToOrderDishResponseDTO(orderDish)));

        return list;
    }

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