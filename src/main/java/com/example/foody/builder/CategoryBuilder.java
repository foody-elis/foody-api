package com.example.foody.builder;

import com.example.foody.model.Category;
import com.example.foody.model.Restaurant;

import java.util.List;

/**
 * Interface for building {@link Category} objects.
 */
public interface CategoryBuilder {
    CategoryBuilder id(long id);
    CategoryBuilder name(String name);
    CategoryBuilder restaurants(List<Restaurant> restaurants);
    Category build();
}