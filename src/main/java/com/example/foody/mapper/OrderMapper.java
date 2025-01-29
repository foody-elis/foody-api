package com.example.foody.mapper;

import com.example.foody.dto.request.OrderRequestDTO;
import com.example.foody.dto.response.OrderResponseDTO;
import com.example.foody.model.Order;

import java.util.List;

/**
 * Mapper interface for converting between Order entities and DTOs.
 */
public interface OrderMapper {

    /**
     * Converts an Order entity to an OrderResponseDTO.
     *
     * @param order the Order entity to convert
     * @return the converted OrderResponseDTO
     */
    OrderResponseDTO orderToOrderResponseDTO(Order order);

    /**
     * Converts an OrderRequestDTO to an Order entity.
     *
     * @param orderRequestDTO the OrderRequestDTO to convert
     * @return the converted Order entity
     */
    Order orderRequestDTOToOrder(OrderRequestDTO orderRequestDTO);

    /**
     * Converts a list of Order entities to a list of OrderResponseDTOs.
     *
     * @param orders the list of Order entities to convert
     * @return the list of converted OrderResponseDTOs
     */
    List<OrderResponseDTO> ordersToOrderResponseDTOs(List<Order> orders);
}