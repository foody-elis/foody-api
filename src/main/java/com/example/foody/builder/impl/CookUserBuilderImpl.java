package com.example.foody.builder.impl;

import com.example.foody.builder.CookUserBuilder;
import com.example.foody.model.Restaurant;
import com.example.foody.model.user.CookUser;
import org.springframework.stereotype.Component;

@Component
public class CookUserBuilderImpl extends UserBuilderImpl<CookUser> implements CookUserBuilder {
    private Restaurant employerRestaurant;

    @Override
    public CookUserBuilder employerRestaurant(Restaurant employerRestaurant) {
        this.employerRestaurant = employerRestaurant;
        return this;
    }

    @Override
    public CookUser build() {
        return new CookUser(
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
                employerRestaurant
        );
    }
}