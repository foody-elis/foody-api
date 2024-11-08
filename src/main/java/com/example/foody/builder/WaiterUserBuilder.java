package com.example.foody.builder;

import com.example.foody.model.Order;
import com.example.foody.model.Restaurant;
import com.example.foody.model.user.WaiterUser;

import java.util.List;

public interface WaiterUserBuilder extends UserBuilder<WaiterUser> {
    WaiterUserBuilder employerRestaurant(Restaurant employerRestaurant);
    WaiterUserBuilder orders(List<Order> orders);
}
