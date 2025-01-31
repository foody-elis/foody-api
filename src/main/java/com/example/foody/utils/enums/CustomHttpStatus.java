package com.example.foody.utils.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Enumeration representing custom HTTP status codes.
 */
@Getter
@AllArgsConstructor
public enum CustomHttpStatus {

    INVALID_TOKEN(498, "Invalid JWT Token");

    private final int value;
    private final String reason;
}