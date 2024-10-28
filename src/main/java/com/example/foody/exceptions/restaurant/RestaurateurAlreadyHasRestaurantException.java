package com.example.foody.exceptions.restaurant;

public class RestaurateurAlreadyHasRestaurantException extends RuntimeException {
    public RestaurateurAlreadyHasRestaurantException(long restaurateurId) {
        super(String.format("Restaurateur with id %d already has a restaurant", restaurateurId));
    }
}
