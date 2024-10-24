package com.example.foody.builder.impl;

import com.example.foody.builder.WaiterUserBuilder;
import com.example.foody.model.Restaurant;
import com.example.foody.model.user.WaiterUser;
import org.springframework.stereotype.Component;

@Component
public class WaiterUserBuilderImpl extends UserBuilderImpl<WaiterUser> implements WaiterUserBuilder {
    private Restaurant employerRestaurant;

    @Override
    public WaiterUserBuilder employerRestaurant(Restaurant employerRestaurant) {
        this.employerRestaurant = employerRestaurant;
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
                avatar,
                role,
                active,
                employerRestaurant
        );
    }
}
