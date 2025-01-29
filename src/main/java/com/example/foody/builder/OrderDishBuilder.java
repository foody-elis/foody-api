package com.example.foody.builder;

import com.example.foody.model.Dish;
import com.example.foody.model.Order;
import com.example.foody.model.order_dish.OrderDish;

/**
 * Interface for building {@link OrderDish} objects.
 */
public interface OrderDishBuilder {
    OrderDishBuilder order(Order order);
    OrderDishBuilder dish(Dish dish);
    OrderDishBuilder quantity(int quantity);
    OrderDish build();
}