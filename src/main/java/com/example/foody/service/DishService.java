package com.example.foody.service;

import com.example.foody.dto.request.DishRequestDTO;
import com.example.foody.dto.response.DishResponseDTO;
import com.example.foody.model.Dish;
import com.example.foody.model.Order;

import java.util.List;

public interface DishService {
    DishResponseDTO save(DishRequestDTO dishDTO);
    List<DishResponseDTO> findAll();
    DishResponseDTO findById(long id);
    List<DishResponseDTO> findAllByRestaurant(long restaurantId);
    Dish addOrder(long id, Order order);
    boolean remove(long id);
}
