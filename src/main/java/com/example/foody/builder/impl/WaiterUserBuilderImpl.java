package com.example.foody.builder.impl;

import com.example.foody.builder.WaiterUserBuilder;
import com.example.foody.model.Order;
import com.example.foody.model.Restaurant;
import com.example.foody.model.user.WaiterUser;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the {@link WaiterUserBuilder} interface.
 */
@Component
public class WaiterUserBuilderImpl extends UserBuilderImpl<WaiterUser> implements WaiterUserBuilder {

    private Restaurant employerRestaurant;
    private List<Order> orders = new ArrayList<>();

    @Override
    public WaiterUserBuilder employerRestaurant(Restaurant employerRestaurant) {
        this.employerRestaurant = employerRestaurant;
        return this;
    }

    @Override
    public WaiterUserBuilder orders(List<Order> orders) {
        this.orders = orders;
        return this;
    }

    @Override
    public WaiterUser build() {
        return new WaiterUser(
                id,
                email,
                password,
                name,
                surname,
                birthDate,
                phoneNumber,
                avatarUrl,
                role,
                active,
                firebaseCustomToken,
                employerRestaurant,
                orders
        );
    }
}