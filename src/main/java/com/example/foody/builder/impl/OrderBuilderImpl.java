package com.example.foody.builder.impl;

import com.example.foody.builder.OrderBuilder;
import com.example.foody.model.Order;
import com.example.foody.model.Restaurant;
import com.example.foody.model.order_dish.OrderDish;
import com.example.foody.model.user.BuyerUser;
import com.example.foody.state.order.OrderState;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class OrderBuilderImpl implements OrderBuilder {
    private long id;
    private String tableCode;
    private List<OrderDish> orderDishes = new ArrayList<>();
    private BuyerUser buyer;
    private Restaurant restaurant;
    private OrderState state;

    @Override
    public OrderBuilder id(long id) {
        this.id = id;
        return this;
    }

    @Override
    public OrderBuilder tableCode(String tableCode) {
        this.tableCode = tableCode;
        return this;
    }

    @Override
    public OrderBuilder orderDishes(List<OrderDish> orderDishes) {
        this.orderDishes = orderDishes;
        return this;
    }

    @Override
    public OrderBuilder buyer(BuyerUser buyer) {
        this.buyer = buyer;
        return this;
    }

    @Override
    public OrderBuilder restaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
        return this;
    }

    @Override
    public OrderBuilder state(OrderState state) {
        this.state = state;
        return this;
    }

    @Override
    public Order build() {
        return new Order(id, tableCode, orderDishes, buyer, restaurant, state);
    }
}