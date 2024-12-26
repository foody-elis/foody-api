package com.example.foody.utils.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EmailPlaceholder {
    // review-invitation-email
    CUSTOMER_NAME("customerName"),
    CUSTOMER_SURNAME("customerSurname"),
    RESTAURANT_NAME("restaurantName"),
    REVIEW_LINK("reviewLink"),

    // payment-received-email
    ORDER_ID("orderId"),
    RESTAURATEUR_NAME("restaurateurName"),
    RESTAURATEUR_SURNAME("restaurateurSurname"),
    AMOUNT("amount"),

    // new-order-email
    COOK_NAME("cookName"),
    COOK_SURNAME("cookSurname");

    private final String placeholder;

    @Override
    public String toString() {
        return "{" + placeholder + "}";
    }
}