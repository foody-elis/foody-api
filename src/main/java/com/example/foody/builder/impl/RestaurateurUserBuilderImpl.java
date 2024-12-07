package com.example.foody.builder.impl;

import com.example.foody.builder.RestaurateurUserBuilder;
import com.example.foody.model.Restaurant;
import com.example.foody.model.user.RestaurateurUser;
import org.springframework.stereotype.Component;

@Component
public class RestaurateurUserBuilderImpl extends UserBuilderImpl<RestaurateurUser> implements RestaurateurUserBuilder {
    private Restaurant restaurant;

    @Override
    public RestaurateurUserBuilder restaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
        return this;
    }

    @Override
    public RestaurateurUser build() {
        return new RestaurateurUser(
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
                restaurant
        );
    }
}