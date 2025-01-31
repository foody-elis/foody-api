package com.example.foody.service;

import com.example.foody.dto.request.CategoryRequestDTO;
import com.example.foody.dto.response.CategoryResponseDTO;
import com.example.foody.model.Category;
import com.example.foody.model.Restaurant;

import java.util.List;

/**
 * Service interface for managing categories.
 */
public interface CategoryService {

    /**
     * Saves a new category.
     *
     * @param categoryDTO the category request data transfer object
     * @return the saved category response data transfer object
     */
    CategoryResponseDTO save(CategoryRequestDTO categoryDTO);

    /**
     * Retrieves all categories.
     *
     * @return the list of category response data transfer objects
     */
    List<CategoryResponseDTO> findAll();

    /**
     * Retrieves a category by its ID.
     *
     * @param id the ID of the category to retrieve
     * @return the category response data transfer object
     */
    CategoryResponseDTO findById(long id);

    /**
     * Adds a restaurant to a category.
     *
     * @param id the ID of the category
     * @param restaurant the restaurant to add
     * @return the updated category
     */
    Category addRestaurant(long id, Restaurant restaurant);

    /**
     * Removes a category by its ID.
     *
     * @param id the ID of the category to remove
     * @return true if the category was removed, false otherwise
     */
    boolean remove(long id);
}