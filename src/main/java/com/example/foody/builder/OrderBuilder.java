package com.example.foody.builder;

import com.example.foody.model.Dish;
import com.example.foody.model.Order;
import com.example.foody.model.Restaurant;
import com.example.foody.model.user.BuyerUser;
import com.example.foody.state.order.OrderState;

import java.util.List;

public interface OrderBuilder {
    OrderBuilder id(long id);
    OrderBuilder tableNumber(String tableNumber);
    OrderBuilder dishes(List<Dish> dishes);
    OrderBuilder buyer(BuyerUser buyer);
    OrderBuilder restaurant(Restaurant restaurant);
    OrderBuilder state(OrderState state);
    Order build();
}
