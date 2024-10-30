package com.example.foody.utils.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CustomHttpStatus {
    INVALID_TOKEN(498, "Invalid JWT Token");

    private final int value;
    private final String reason;
}
