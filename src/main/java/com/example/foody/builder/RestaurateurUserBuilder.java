package com.example.foody.builder;

import com.example.foody.model.Restaurant;
import com.example.foody.model.user.RestaurateurUser;

/**
 * Interface for building {@link RestaurateurUser} objects.
 * Extends the {@link UserBuilder} interface with RestaurateurUser as the type parameter.
 */
public interface RestaurateurUserBuilder extends UserBuilder<RestaurateurUser> {
    RestaurateurUserBuilder restaurant(Restaurant restaurant);
}