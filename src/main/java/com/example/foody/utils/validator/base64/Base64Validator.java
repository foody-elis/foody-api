package com.example.foody.utils.validator.base64;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validator to check if a string is a valid Base64 encoded string.
 */
public class Base64Validator implements ConstraintValidator<Base64, String> {

    /**
     * Regular expression to validate Base64 encoded strings.
     */
    private static final String BASE64_REGEX = "^(?:[A-Za-z0-9+/]{4})*(?:[A-Za-z0-9+/]{2}==|[A-Za-z0-9+/]{3}=)?$";
    private boolean nullable;

    /**
     * Initializes the validator with the given annotation.
     *
     * @param constraintAnnotation the annotation instance for a given constraint declaration
     */
    @Override
    public void initialize(Base64 constraintAnnotation) {
        this.nullable = constraintAnnotation.nullable();
    }

    /**
     * Checks if the given string is a valid Base64 encoded string.
     *
     * @param value the string to validate
     * @param context context in which the constraint is evaluated
     * @return true if the string is valid, false otherwise
     * {@inheritDoc}
     */
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) return nullable;

        return !value.trim().isEmpty() && value.matches(BASE64_REGEX);
    }
}