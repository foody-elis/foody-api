package com.example.foody.builder.impl;

import com.example.foody.builder.OrderBuilder;
import com.example.foody.model.Dish;
import com.example.foody.model.Order;
import com.example.foody.model.Restaurant;
import com.example.foody.model.user.BuyerUser;
import com.example.foody.state.order.OrderState;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class OrderBuilderImpl implements OrderBuilder {
    private long id;
    private String tableNumber;
    private List<Dish> dishes = new ArrayList<>();
    private BuyerUser buyer;
    private Restaurant restaurant;
    private OrderState state;

    @Override
    public OrderBuilder id(long id) {
        this.id = id;
        return this;
    }

    @Override
    public OrderBuilder tableNumber(String tableNumber) {
        this.tableNumber = tableNumber;
        return this;
    }

    @Override
    public OrderBuilder dishes(List<Dish> dishes) {
        this.dishes = dishes;
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
        return new Order(id, tableNumber, dishes, buyer, restaurant, state);
    }
}
