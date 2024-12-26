package com.example.foody.utils.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EmailTemplateType {
    REVIEW_INVITATION("review-invitation-email"),
    PAYMENT_RECEIVED("payment-received-email");

    private final String templateName;
}