package com.example.foody.mapper;

import com.example.foody.dto.request.OrderDishRequestDTO;
import com.example.foody.dto.response.OrderDishResponseDTO;
import com.example.foody.model.order_dish.OrderDish;

import java.util.List;

/**
 * Mapper interface for converting between OrderDish entities and DTOs.
 */
public interface OrderDishMapper {

    /**
     * Converts an OrderDish entity to an OrderDishResponseDTO.
     *
     * @param orderDish the OrderDish entity to convert
     * @return the converted OrderDishResponseDTO
     */
    OrderDishResponseDTO orderDishToOrderDishResponseDTO(OrderDish orderDish);

    /**
     * Converts an OrderDishRequestDTO to an OrderDish entity.
     *
     * @param orderDishRequestDTO the OrderDishRequestDTO to convert
     * @return the converted OrderDish entity
     */
    OrderDish orderDishRequestDTOToOrderDish(OrderDishRequestDTO orderDishRequestDTO);

    /**
     * Converts a list of OrderDish entities to a list of OrderDishResponseDTOs.
     *
     * @param orderDishes the list of OrderDish entities to convert
     * @return the list of converted OrderDishResponseDTOs
     */
    List<OrderDishResponseDTO> orderDishesToOrderDishResponseDTOs(List<OrderDish> orderDishes);
}