package com.example.foody.utils.validator.value_of_enum;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Validator to check if a value is one of the specified enum values.
 */
public class ValueOfEnumValidator implements ConstraintValidator<ValueOfEnum, CharSequence> {

    private List<String> acceptedValues;

    /**
     * Initializes the validator with the given annotation.
     *
     * @param annotation the annotation instance for a given constraint declaration
     */
    @Override
    public void initialize(ValueOfEnum annotation) {
        acceptedValues = Stream.of(annotation.enumClass().getEnumConstants())
                .map(Enum::name)
                .collect(Collectors.toList());
    }

    /**
     * Checks if the given value is one of the specified enum values.
     *
     * @param value the value to validate
     * @param context context in which the constraint is evaluated
     * @return true if the value is valid, false otherwise
     */
    @Override
    public boolean isValid(CharSequence value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        return acceptedValues.contains(value.toString());
    }
}