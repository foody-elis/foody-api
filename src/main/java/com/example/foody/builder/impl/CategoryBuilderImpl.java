package com.example.foody.builder.impl;

import com.example.foody.builder.CategoryBuilder;
import com.example.foody.model.Category;
import com.example.foody.model.Restaurant;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the {@link CategoryBuilder} interface.
 */
@Component
public class CategoryBuilderImpl implements CategoryBuilder {

    private long id;
    private String name;
    private List<Restaurant> restaurants = new ArrayList<>();

    @Override
    public CategoryBuilder id(long id) {
        this.id = id;
        return this;
    }

    @Override
    public CategoryBuilder name(String name) {
        this.name = name;
        return this;
    }

    @Override
    public CategoryBuilder restaurants(List<Restaurant> restaurants) {
        this.restaurants = restaurants;
        return this;
    }

    @Override
    public Category build() {
        return new Category(id, name, restaurants);
    }
}