package com.example.foody.builder;

import com.example.foody.model.Restaurant;
import com.example.foody.model.user.CookUser;

public interface CookUserBuilder extends UserBuilder<CookUser> {
    CookUserBuilder employerRestaurant(Restaurant employerRestaurant);
}
