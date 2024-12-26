package com.example.foody.utils.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EmailPlaceholder {
    // review-invitation-email
    NAME("name"),
    SURNAME("surname"),
    RESTAURANT_NAME("restaurantName"),
    REVIEW_LINK("reviewLink"),

    // payment-received-email
    ORDER_ID("orderId"),
    AMOUNT("amount");

    private final String placeholder;

    @Override
    public String toString() {
        return "{" + placeholder + "}";
    }
}