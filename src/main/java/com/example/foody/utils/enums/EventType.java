package com.example.foody.utils.enums;

public enum EventType {
    ORDER_COMPLETED, // Order paid, notify the customer and the restaurant
    ORDER_CREATED, // New order, notify all cooks
    NEW_REVIEW, // New review, notify the restaurateur and all employees
}