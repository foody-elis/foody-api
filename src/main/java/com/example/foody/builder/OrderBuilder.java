package com.example.foody.builder;

import com.example.foody.model.Dish;
import com.example.foody.model.Order;
import com.example.foody.model.Restaurant;
import com.example.foody.model.user.CustomerUser;
import com.example.foody.state.order.OrderState;

import java.util.List;

public interface OrderBuilder {
    OrderBuilder id(long id);
    OrderBuilder tableNumber(String tableNumber);
    OrderBuilder dishes(List<Dish> dishes);
    OrderBuilder customer(CustomerUser customer);
    OrderBuilder restaurant(Restaurant restaurant);
    OrderBuilder state(OrderState state);
    Order build();
}
