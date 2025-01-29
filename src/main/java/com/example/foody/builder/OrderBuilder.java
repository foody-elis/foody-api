package com.example.foody.builder;

import com.example.foody.model.Order;
import com.example.foody.model.Restaurant;
import com.example.foody.model.order_dish.OrderDish;
import com.example.foody.model.user.BuyerUser;
import com.example.foody.state.order.OrderState;

import java.util.List;

/**
 * Interface for building {@link Order} objects.
 */
public interface OrderBuilder {
    OrderBuilder id(long id);
    OrderBuilder tableCode(String tableCode);
    OrderBuilder orderDishes(List<OrderDish> orderDishes);
    OrderBuilder buyer(BuyerUser buyer);
    OrderBuilder restaurant(Restaurant restaurant);
    OrderBuilder state(OrderState state);
    Order build();
}