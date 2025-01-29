package com.example.foody.builder;

import com.example.foody.model.Dish;
import com.example.foody.model.Restaurant;
import com.example.foody.model.Review;
import com.example.foody.model.order_dish.OrderDish;

import java.math.BigDecimal;
import java.util.List;

/**
 * Interface for building {@link Dish} objects.
 */
public interface DishBuilder {
    DishBuilder id(long id);
    DishBuilder name(String name);
    DishBuilder description(String description);
    DishBuilder price(BigDecimal price);
    DishBuilder photoUrl(String photoUrl);
    DishBuilder restaurant(Restaurant restaurant);
    DishBuilder reviews(List<Review> reviews);
    DishBuilder orderDishes(List<OrderDish> orderDishes);
    Dish build();
}