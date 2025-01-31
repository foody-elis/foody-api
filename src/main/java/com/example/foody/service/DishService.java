package com.example.foody.service;

import com.example.foody.dto.request.DishRequestDTO;
import com.example.foody.dto.request.DishUpdateRequestDTO;
import com.example.foody.dto.response.DishResponseDTO;

import java.util.List;

/**
 * Service interface for managing dishes.
 */
public interface DishService {

    /**
     * Saves a new dish.
     *
     * @param dishDTO the dish request data transfer object
     * @return the saved dish response data transfer object
     */
    DishResponseDTO save(DishRequestDTO dishDTO);

    /**
     * Retrieves all dishes.
     *
     * @return the list of dish response data transfer objects
     */
    List<DishResponseDTO> findAll();

    /**
     * Retrieves a dish by its ID.
     *
     * @param id the ID of the dish to retrieve
     * @return the dish response data transfer object
     */
    DishResponseDTO findById(long id);

    /**
     * Retrieves all dishes for a specific restaurant.
     *
     * @param restaurantId the ID of the restaurant
     * @return the list of dish response data transfer objects
     */
    List<DishResponseDTO> findAllByRestaurant(long restaurantId);

    /**
     * Updates an existing dish.
     *
     * @param id the ID of the dish to update
     * @param dishDTO the dish update request data transfer object
     * @return the updated dish response data transfer object
     */
    DishResponseDTO update(long id, DishUpdateRequestDTO dishDTO);

    /**
     * Removes a dish by its ID.
     *
     * @param id the ID of the dish to remove
     * @return true if the dish was removed, false otherwise
     */
    boolean remove(long id);
}