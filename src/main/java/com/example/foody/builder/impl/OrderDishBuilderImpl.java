package com.example.foody.builder.impl;

import com.example.foody.builder.OrderDishBuilder;
import com.example.foody.model.Dish;
import com.example.foody.model.Order;
import com.example.foody.model.order_dish.OrderDish;
import org.springframework.stereotype.Component;

@Component
public class OrderDishBuilderImpl implements OrderDishBuilder {
    private Order order;
    private Dish dish;
    private int quantity;

    @Override
    public OrderDishBuilder order(Order order) {
        this.order = order;
        return this;
    }

    @Override
    public OrderDishBuilder dish(Dish dish) {
        this.dish = dish;
        return this;
    }

    @Override
    public OrderDishBuilder quantity(int quantity) {
        this.quantity = quantity;
        return this;
    }

    @Override
    public OrderDish build() {
        return new OrderDish(order, dish, quantity);
    }
}