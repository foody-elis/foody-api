package com.example.foody.builder;

import com.example.foody.model.*;
import com.example.foody.model.user.CookUser;
import com.example.foody.model.user.EmployeeUser;
import com.example.foody.model.user.RestaurateurUser;
import com.example.foody.model.user.WaiterUser;

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
    RestaurantBuilder weekDayInfos(List<WeekDayInfo> weekDayInfos);
    RestaurantBuilder orders(List<Order> orders);
    RestaurantBuilder bookings(List<Booking> bookings);
    RestaurantBuilder address(Address address);
    RestaurantBuilder restaurateur(RestaurateurUser restaurateur);
    RestaurantBuilder employees(List<EmployeeUser> employees);
    Restaurant build();
}
