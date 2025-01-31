package com.example.foody.utils.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Enumeration representing different sitting time steps.
 */
@Getter
@AllArgsConstructor
public enum SittingTimeStep {

    FIFTEEN(15),
    THIRTY(30),
    SIXTY(60);

    private final int minutes;
}