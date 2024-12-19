package com.example.foody.repository;

import com.example.foody.model.order_dish.OrderDish;
import com.example.foody.model.order_dish.OrderDishKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDishRepository extends JpaRepository<OrderDish, OrderDishKey> {
}