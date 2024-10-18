package com.example.foody.builder.impl;

import com.example.foody.builder.RestaurantBuilder;
import com.example.foody.model.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class RestaurantBuilderImpl implements RestaurantBuilder {
    private long id;
    private String name;
    private String description;
    private String phoneNumber;
    private int seats;
    private boolean approved = false;
    private List<Category> categories = new ArrayList<>();
    private List<Dish> dishes = new ArrayList<>();
    private List<Review> reviews = new ArrayList<>();
    private List<WeekDayInfo> weekDayInfos = new ArrayList<>();
    private List<Order> orders = new ArrayList<>();
    private List<Booking> bookings = new ArrayList<>();
    private User user;
    private Address address;

    @Override
    public RestaurantBuilder id(long id) {
        this.id = id;
        return this;
    }

    @Override
    public RestaurantBuilder name(String name) {
        this.name = name;
        return this;
    }

    @Override
    public RestaurantBuilder description(String description) {
        this.description = description;
        return this;
    }

    @Override
    public RestaurantBuilder phoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    @Override
    public RestaurantBuilder seats(int seats) {
        this.seats = seats;
        return this;
    }

    @Override
    public RestaurantBuilder approved(boolean approved) {
        this.approved = approved;
        return this;
    }

    @Override
    public RestaurantBuilder categories(List<Category> categories) {
        this.categories = categories;
        return this;
    }

    @Override
    public RestaurantBuilder dishes(List<Dish> dishes) {
        this.dishes = dishes;
        return this;
    }

    @Override
    public RestaurantBuilder reviews(List<Review> reviews) {
        this.reviews = reviews;
        return this;
    }

    @Override
    public RestaurantBuilder weekDayInfos(List<WeekDayInfo> weekDayInfos) {
        this.weekDayInfos = weekDayInfos;
        return this;
    }

    @Override
    public RestaurantBuilder orders(List<Order> orders) {
        this.orders = orders;
        return this;
    }

    @Override
    public RestaurantBuilder bookings(List<Booking> bookings) {
        this.bookings = bookings;
        return this;
    }

    @Override
    public RestaurantBuilder user(User user) {
        this.user = user;
        return this;
    }

    @Override
    public RestaurantBuilder address(Address address) {
        this.address = address;
        return this;
    }

    @Override
    public Restaurant build() {
        return new Restaurant(
                id,
                name,
                description,
                phoneNumber,
                seats,
                approved,
                categories,
                dishes,
                reviews,
                weekDayInfos,
                orders,
                bookings,
                user,
                address
        );
    }
}
