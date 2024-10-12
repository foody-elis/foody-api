package com.example.foody.builder;

import com.example.foody.model.Dish;
import com.example.foody.model.Order;
import com.example.foody.model.Restaurant;
import com.example.foody.model.Review;

import java.math.BigDecimal;
import java.util.List;

public interface DishBuilder {
    DishBuilder id(long id);
    DishBuilder name(String name);
    DishBuilder description(String description);
    DishBuilder price(BigDecimal price);
    DishBuilder photo(String photo);
    DishBuilder restaurant(Restaurant restaurant);
    DishBuilder reviews(List<Review> reviews);
    DishBuilder orders(List<Order> orders);
    Dish build();
}
