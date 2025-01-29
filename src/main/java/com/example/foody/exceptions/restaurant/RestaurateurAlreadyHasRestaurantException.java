package com.example.foody.exceptions.restaurant;

/**
 * Exception thrown when a restaurateur already has a restaurant.
 */
public class RestaurateurAlreadyHasRestaurantException extends RuntimeException {

    /**
     * Constructs a new RestaurateurAlreadyHasRestaurantException with a formatted message indicating the restaurateur ID that already has a restaurant.
     *
     * @param restaurateurId the ID of the restaurateur who already has a restaurant
     */
    public RestaurateurAlreadyHasRestaurantException(long restaurateurId) {
        super(String.format("Restaurateur with id %d already has a restaurant", restaurateurId));
    }
}