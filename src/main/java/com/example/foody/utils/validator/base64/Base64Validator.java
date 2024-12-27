package com.example.foody.utils.validator.base64;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class Base64Validator implements ConstraintValidator<Base64, String> {
    private static final String BASE64_REGEX = "^(?:[A-Za-z0-9+/]{4})*(?:[A-Za-z0-9+/]{2}==|[A-Za-z0-9+/]{3}=)?$";
    private boolean nullable;

    @Override
    public void initialize(Base64 constraintAnnotation) {
        this.nullable = constraintAnnotation.nullable();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) return nullable;

        return !value.trim().isEmpty() && value.matches(BASE64_REGEX);
    }
}