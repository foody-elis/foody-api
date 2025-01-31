package com.example.foody.repository;

import com.example.foody.model.order_dish.OrderDish;
import com.example.foody.model.order_dish.OrderDishKey;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for managing {@link OrderDish} entities.
 * <p>
 * Extends the {@link JpaRepository} interface to provide CRUD operations for {@link OrderDish} entities.
 */
public interface OrderDishRepository extends JpaRepository<OrderDish, OrderDishKey> {
}