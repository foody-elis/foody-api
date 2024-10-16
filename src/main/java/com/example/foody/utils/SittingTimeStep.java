package com.example.foody.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SittingTimeStep {
    FIFTEEN(15),
    THIRTY(30),
    SIXTY(60);

    private final int minutes;
}
