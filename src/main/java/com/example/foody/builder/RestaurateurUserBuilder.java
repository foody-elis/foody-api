package com.example.foody.builder;

import com.example.foody.model.Restaurant;
import com.example.foody.model.user.RestaurateurUser;

public interface RestaurateurUserBuilder extends UserBuilder<RestaurateurUser> {
    RestaurateurUserBuilder restaurant(Restaurant restaurant);
}
