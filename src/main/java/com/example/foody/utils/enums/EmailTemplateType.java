package com.example.foody.utils.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EmailTemplateType {
    REVIEW_INVITATION("review-invitation-email"),
    PAYMENT_RECEIVED("payment-received-email"),
    NEW_ORDER("new-order-email"),
    NEW_REVIEW("new-review-email"),
    RESTAURANT_APPROVED("restaurant-approved-email"),
    RESTAURANT_REGISTRATION("restaurant-registration-email"),
    BOOKING_CREATED("booking-created-email"),
    USER_REGISTRATION("user-registration-email"),
    BOOKING_CANCELLED_BY_CUSTOMER("booking-cancelled-by-customer-email"),
    BOOKING_CANCELLED_BY_RESTAURANT("booking-cancelled-by-restaurant-email");

    private final String templateName;
}