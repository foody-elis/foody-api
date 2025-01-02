package com.example.foody.service;

import com.example.foody.dto.request.OrderRequestDTO;
import com.example.foody.dto.response.OrderResponseDTO;

import java.util.List;

public interface OrderService {
    OrderResponseDTO save(OrderRequestDTO orderDTO);
    List<OrderResponseDTO> findAll();
    OrderResponseDTO findById(long id);
    List<OrderResponseDTO> findAllByBuyer(long buyerId);
    List<OrderResponseDTO> findAllByRestaurant(long restaurantId);
    OrderResponseDTO payById(long id);
    OrderResponseDTO prepareById(long id);
    OrderResponseDTO completeById(long id);
    boolean remove(long id);
}
