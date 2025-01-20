package com.example.foody.builder.impl;

import com.example.foody.builder.ReviewBuilder;
import com.example.foody.model.Dish;
import com.example.foody.model.Restaurant;
import com.example.foody.model.Review;
import com.example.foody.model.user.CustomerUser;
import org.springframework.stereotype.Component;

@Component
public class ReviewBuilderImpl implements ReviewBuilder {
    private long id;
    private String title;
    private String description;
    private int rating;
    private CustomerUser customer;
    private Restaurant restaurant;
    private Dish dish;

    @Override
    public ReviewBuilder id(long id) {
        this.id = id;
        return this;
    }

    @Override
    public ReviewBuilder title(String title) {
        this.title = title;
        return this;
    }

    @Override
    public ReviewBuilder description(String description) {
        this.description = description;
        return this;
    }

    @Override
    public ReviewBuilder rating(int rating) {
        this.rating = rating;
        return this;
    }

    @Override
    public ReviewBuilder customer(CustomerUser customer) {
        this.customer = customer;
        return this;
    }

    @Override
    public ReviewBuilder restaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
        return this;
    }

    @Override
    public ReviewBuilder dish(Dish dish) {
        this.dish = dish;
        return this;
    }

    @Override
    public Review build() {
        return new Review(id, title, description, rating, customer, restaurant, dish);
    }
}