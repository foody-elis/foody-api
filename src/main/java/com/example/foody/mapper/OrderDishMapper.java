package com.example.foody.mapper;

import com.example.foody.dto.request.OrderDishRequestDTO;
import com.example.foody.dto.response.OrderDishResponseDTO;
import com.example.foody.model.order_dish.OrderDish;

import java.util.List;

public interface OrderDishMapper {
    OrderDishResponseDTO orderDishToOrderDishResponseDTO(OrderDish orderDish);
    OrderDish orderDishRequestDTOToOrderDish(OrderDishRequestDTO orderDishRequestDTO);
    List<OrderDishResponseDTO> orderDishesToOrderDishResponseDTOs(List<OrderDish> orderDishes);
}