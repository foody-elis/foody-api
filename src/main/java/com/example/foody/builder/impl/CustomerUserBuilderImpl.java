package com.example.foody.builder.impl;

import com.example.foody.builder.CustomerUserBuilder;
import com.example.foody.model.Booking;
import com.example.foody.model.CreditCard;
import com.example.foody.model.Order;
import com.example.foody.model.Review;
import com.example.foody.model.user.CustomerUser;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CustomerUserBuilderImpl extends UserBuilderImpl<CustomerUser> implements CustomerUserBuilder {
    private CreditCard creditCard;
    private List<Review> reviews = new ArrayList<>();
    private List<Booking> bookings = new ArrayList<>();
    private List<Order> orders = new ArrayList<>();

    @Override
    public CustomerUserBuilder creditCard(CreditCard creditCard) {
        this.creditCard = creditCard;
        return this;
    }

    @Override
    public CustomerUserBuilder reviews(List<Review> reviews) {
        this.reviews = reviews;
        return this;
    }

    @Override
    public CustomerUserBuilder bookings(List<Booking> bookings) {
        this.bookings = bookings;
        return this;
    }

    @Override
    public CustomerUserBuilder orders(List<Order> orders) {
        this.orders = orders;
        return this;
    }

    @Override
    public CustomerUser build() {
        return new CustomerUser(
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
                creditCard,
                reviews,
                bookings,
                orders
        );
    }
}