package com.example.foody.mapper;

import com.example.foody.dto.request.OrderRequestDTO;
import com.example.foody.dto.response.OrderResponseDTO;
import com.example.foody.model.Order;

import java.util.List;

public interface OrderMapper {
    OrderResponseDTO orderToOrderResponseDTO(Order order);
    Order orderRequestDTOToOrder(OrderRequestDTO orderRequestDTO);
    List<OrderResponseDTO> ordersToOrderResponseDTOs(List<Order> orders);
}
