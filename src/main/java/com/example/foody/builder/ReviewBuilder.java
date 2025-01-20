package com.example.foody.builder;

import com.example.foody.model.Dish;
import com.example.foody.model.Restaurant;
import com.example.foody.model.Review;
import com.example.foody.model.user.CustomerUser;

public interface ReviewBuilder {
    ReviewBuilder id(long id);
    ReviewBuilder title(String title);
    ReviewBuilder description(String description);
    ReviewBuilder rating(int rating);
    ReviewBuilder customer(CustomerUser customer);
    ReviewBuilder restaurant(Restaurant restaurant);
    ReviewBuilder dish(Dish dish);
    Review build();
}