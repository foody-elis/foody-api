package com.example.foody.utils.validator;

import jakarta.validation.ConstraintValidatorContext;

public class ValidatorUtils {
    public static void addConstraintViolation(ConstraintValidatorContext context, String message, String propertyNode) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message)
                .addPropertyNode(propertyNode)
                .addConstraintViolation();
    }
}