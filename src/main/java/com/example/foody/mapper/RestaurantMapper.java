package com.example.foody.mapper;

import com.example.foody.dto.request.RestaurantRequestDTO;
import com.example.foody.dto.response.DetailedRestaurantResponseDTO;
import com.example.foody.dto.response.RestaurantResponseDTO;
import com.example.foody.model.Dish;
import com.example.foody.model.Restaurant;
import com.example.foody.model.Review;
import com.example.foody.model.SittingTime;

import java.util.List;

/**
 * Mapper interface for converting between Restaurant entities and DTOs.
 */
public interface RestaurantMapper {

    /**
     * Converts a Restaurant entity to a RestaurantResponseDTO.
     *
     * @param restaurant the Restaurant entity to convert
     * @return the converted RestaurantResponseDTO
     */
    RestaurantResponseDTO restaurantToRestaurantResponseDTO(Restaurant restaurant);

    /**
     * Converts a Restaurant entity to a DetailedRestaurantResponseDTO.
     *
     * @param restaurant the Restaurant entity to convert
     * @param averageRating the average rating of the restaurant
     * @param sittingTimes the list of sitting times for the restaurant
     * @param dishes the list of dishes offered by the restaurant
     * @param reviews the list of reviews for the restaurant
     * @return the converted DetailedRestaurantResponseDTO
     */
    DetailedRestaurantResponseDTO restaurantToDetailedRestaurantResponseDTO(
            Restaurant restaurant,
            double averageRating,
            List<SittingTime> sittingTimes,
            List<Dish> dishes,
            List<Review> reviews
    );

    /**
     * Converts a RestaurantRequestDTO to a Restaurant entity.
     *
     * @param restaurantRequestDTO the RestaurantRequestDTO to convert
     * @return the converted Restaurant entity
     */
    Restaurant restaurantRequestDTOToRestaurant(RestaurantRequestDTO restaurantRequestDTO);

    /**
     * Updates a Restaurant entity from a RestaurantRequestDTO.
     *
     * @param restaurant the Restaurant entity to update
     * @param restaurantRequestDTO the RestaurantRequestDTO with updated information
     */
    void updateRestaurantFromRestaurantRequestDTO(Restaurant restaurant, RestaurantRequestDTO restaurantRequestDTO);
}