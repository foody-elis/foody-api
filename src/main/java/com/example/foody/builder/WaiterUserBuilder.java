package com.example.foody.builder;

import com.example.foody.model.Restaurant;
import com.example.foody.model.user.WaiterUser;

public interface WaiterUserBuilder extends UserBuilder<WaiterUser> {
    WaiterUserBuilder employerRestaurant(Restaurant employerRestaurant);
}
