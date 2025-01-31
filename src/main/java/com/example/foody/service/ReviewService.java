package com.example.foody.service;

import com.example.foody.dto.request.ReviewRequestDTO;
import com.example.foody.dto.response.ReviewResponseDTO;

import java.util.List;

/**
 * Service interface for managing reviews.
 */
public interface ReviewService {

    /**
     * Saves a new review.
     *
     * @param reviewDTO the review data transfer object containing review details
     * @return the saved review response data transfer object
     */
    ReviewResponseDTO save(ReviewRequestDTO reviewDTO);

    /**
     * Retrieves all reviews.
     *
     * @return a list of all review response data transfer objects
     */
    List<ReviewResponseDTO> findAll();

    /**
     * Finds a review by its ID.
     *
     * @param id the ID of the review to find
     * @return the found review response data transfer object
     */
    ReviewResponseDTO findById(long id);

    /**
     * Finds all reviews by customer ID.
     *
     * @param customerId the ID of the customer
     * @return a list of review response data transfer objects for the specified customer
     */
    List<ReviewResponseDTO> findAllByCustomer(long customerId);

    /**
     * Finds all reviews by restaurant ID.
     *
     * @param restaurantId the ID of the restaurant
     * @return a list of review response data transfer objects for the specified restaurant
     */
    List<ReviewResponseDTO> findAllByRestaurant(long restaurantId);

    /**
     * Finds all reviews by dish ID.
     *
     * @param dishId the ID of the dish
     * @return a list of review response data transfer objects for the specified dish
     */
    List<ReviewResponseDTO> findAllByDish(long dishId);

    /**
     * Removes a review by its ID.
     *
     * @param id the ID of the review to remove
     * @return true if the review was successfully removed, false otherwise
     */
    boolean remove(long id);
}