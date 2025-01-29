package com.example.foody.builder;

import com.example.foody.model.Restaurant;
import com.example.foody.model.user.CookUser;

/**
 * Interface for building {@link CookUser} objects.
 * Extends the {@link UserBuilder} interface with CookUser as the type parameter.
 */
public interface CookUserBuilder extends UserBuilder<CookUser> {
    CookUserBuilder employerRestaurant(Restaurant employerRestaurant);
}