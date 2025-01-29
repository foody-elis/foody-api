package com.example.foody.builder;

import com.example.foody.model.Order;
import com.example.foody.model.Restaurant;
import com.example.foody.model.user.WaiterUser;

import java.util.List;

/**
 * Interface for building {@link WaiterUser} objects.
 * Extends the {@link UserBuilder} interface with WaiterUser as the type parameter.
 */
public interface WaiterUserBuilder extends UserBuilder<WaiterUser> {
    WaiterUserBuilder employerRestaurant(Restaurant employerRestaurant);
    WaiterUserBuilder orders(List<Order> orders);
}