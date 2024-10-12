package com.example.foody.exceptions.dish;

public class ForbiddenDishAccessException extends RuntimeException {
    public ForbiddenDishAccessException() {
        super("Dish access denied.");
    }
}
