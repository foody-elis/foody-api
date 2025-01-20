package com.example.foody.builder.impl;

import com.example.foody.builder.DishBuilder;
import com.example.foody.model.Dish;
import com.example.foody.model.Restaurant;
import com.example.foody.model.Review;
import com.example.foody.model.order_dish.OrderDish;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class DishBuilderImpl implements DishBuilder {
    private long id;
    private String name;
    private String description;
    private BigDecimal price;
    private String photoUrl;
    private Restaurant restaurant;
    private List<Review> reviews = new ArrayList<>();
    private List<OrderDish> orderDishes = new ArrayList<>();

    @Override
    public DishBuilder id(long id) {
        this.id = id;
        return this;
    }

    @Override
    public DishBuilder name(String name) {
        this.name = name;
        return this;
    }

    @Override
    public DishBuilder description(String description) {
        this.description = description;
        return this;
    }

    @Override
    public DishBuilder price(BigDecimal price) {
        this.price = price;
        return this;
    }

    @Override
    public DishBuilder photoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
        return this;
    }

    @Override
    public DishBuilder restaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
        return this;
    }

    @Override
    public DishBuilder reviews(List<Review> reviews) {
        this.reviews = reviews;
        return this;
    }

    @Override
    public DishBuilder orderDishes(List<OrderDish> orderDishes) {
        this.orderDishes = orderDishes;
        return this;
    }

    @Override
    public Dish build() {
        return new Dish(id, name, description, price, photoUrl, restaurant, reviews, orderDishes);
    }
}