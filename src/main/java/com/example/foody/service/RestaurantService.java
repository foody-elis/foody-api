package com.example.foody.service;

import com.example.foody.dto.request.RestaurantRequestDTO;
import com.example.foody.dto.response.DetailedRestaurantResponseDTO;
import com.example.foody.dto.response.RestaurantResponseDTO;

import java.util.List;

/**
 * Service interface for managing restaurants.
 */
public interface RestaurantService {

    /**
     * Saves a new restaurant.
     *
     * @param restaurantDTO the restaurant data transfer object containing restaurant details
     * @return the saved restaurant response data transfer object
     */
    RestaurantResponseDTO save(RestaurantRequestDTO restaurantDTO);

    /**
     * Retrieves all restaurants.
     *
     * @return a list of all detailed restaurant response data transfer objects
     */
    List<DetailedRestaurantResponseDTO> findAll();

    /**
     * Finds a restaurant by its ID.
     *
     * @param id the ID of the restaurant to find
     * @return the found detailed restaurant response data transfer object
     */
    DetailedRestaurantResponseDTO findById(long id);

    /**
     * Finds a restaurant by the restaurateur ID.
     *
     * @param restaurateurId the ID of the restaurateur
     * @return the found detailed restaurant response data transfer object
     */
    DetailedRestaurantResponseDTO findByRestaurateur(long restaurateurId);

    /**
     * Finds all restaurants by category ID.
     *
     * @param categoryId the ID of the category
     * @return a list of detailed restaurant response data transfer objects for the specified category
     */
    List<DetailedRestaurantResponseDTO> findAllByCategory(long categoryId);

    /**
     * Approves a restaurant by its ID.
     *
     * @param id the ID of the restaurant to approve
     * @return the updated detailed restaurant response data transfer object
     */
    DetailedRestaurantResponseDTO approveById(long id);

    /**
     * Updates a restaurant by its ID.
     *
     * @param id the ID of the restaurant to update
     * @param restaurantDTO the restaurant data transfer object containing updated restaurant details
     * @return the updated detailed restaurant response data transfer object
     */
    DetailedRestaurantResponseDTO update(long id, RestaurantRequestDTO restaurantDTO);

    /**
     * Removes a restaurant by its ID.
     *
     * @param id the ID of the restaurant to remove
     * @return true if the restaurant was successfully removed, false otherwise
     */
    boolean remove(long id);
}