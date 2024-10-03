package com.example.foody.builder;

import com.example.foody.model.*;

import java.util.List;

public interface RestaurantBuilder {
    RestaurantBuilder id(long id);
    RestaurantBuilder name(String name);
    RestaurantBuilder description(String description);
    RestaurantBuilder phoneNumber(String phoneNumber);
    RestaurantBuilder seats(int seats);
    RestaurantBuilder approved(boolean approved);
    RestaurantBuilder categories(List<Category> categories);
    RestaurantBuilder dishes(List<Dish> dishes);
    RestaurantBuilder reviews(List<Review> reviews);
    RestaurantBuilder sittingTimes(List<SittingTime> sittingTimes);
    RestaurantBuilder orders(List<Order> orders);
    RestaurantBuilder user(User user);
    RestaurantBuilder address(Address address);
    Restaurant build();
}
