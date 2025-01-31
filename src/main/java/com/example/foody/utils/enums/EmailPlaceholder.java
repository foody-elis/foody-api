package com.example.foody.utils.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Enumeration representing placeholders used in email templates.
 */
@Getter
@AllArgsConstructor
public enum EmailPlaceholder {

    CUSTOMER_NAME("customerName"),
    CUSTOMER_SURNAME("customerSurname"),
    RESTAURANT_NAME("restaurantName"),
    ORDER_ID("orderId"),
    RESTAURATEUR_NAME("restaurateurName"),
    RESTAURATEUR_SURNAME("restaurateurSurname"),
    AMOUNT("amount"),
    COOK_NAME("cookName"),
    COOK_SURNAME("cookSurname"),
    NAME("name"),
    SURNAME("surname"),
    DATE_TIME("dateTime"),
    RATING("rating"),
    BOOKING_ID("bookingId"),
    DATE("date"),
    TIME("time"),
    DURATION("duration"),
    SEATS("seats");

    private final String placeholder;

    /**
     * Returns the placeholder in a formatted string.
     *
     * @return the formatted placeholder string
     */
    @Override
    public String toString() {
        return "{" + placeholder + "}";
    }
}