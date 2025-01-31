package com.example.foody.utils.validator;

import jakarta.validation.ConstraintValidatorContext;

/**
 * Utility class for validator-related operations.
 */
public class ValidatorUtils {

    /**
     * Adds a constraint violation to the given context with the specified message and property node.
     *
     * @param context the context in which the constraint is evaluated
     * @param message the error message to be shown
     * @param propertyNode the property node where the violation occurred
     */
    public static void addConstraintViolation(ConstraintValidatorContext context, String message, String propertyNode) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message)
                .addPropertyNode(propertyNode)
                .addConstraintViolation();
    }
}