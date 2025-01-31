package com.example.foody.utils.validator.sequential_times;

import com.example.foody.utils.validator.ValidatorUtils;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.BeanWrapperImpl;

import java.time.LocalTime;

/**
 * Validator to check if a sequence of times are in the correct order.
 */
public class SequentialTimesValidator implements ConstraintValidator<SequentialTimes, Object> {

    private String message;
    private String[] fields;

    /**
     * Initializes the validator with the given annotation.
     *
     * @param constraintAnnotation the annotation instance for a given constraint declaration
     */
    @Override
    public void initialize(SequentialTimes constraintAnnotation) {
        message = constraintAnnotation.message();
        fields = constraintAnnotation.fields();
    }

    /**
     * Checks if the given object's times are in the correct order.
     *
     * @param value the object to validate
     * @param context context in which the constraint is evaluated
     * @return true if the times are in the correct order, false otherwise
     */
    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null || fields == null || fields.length < 2) {
            return true;
        }

        // Use a BeanWrapper to access the fields of the object
        BeanWrapperImpl wrapper = new BeanWrapperImpl(value);

        for (int i = 0; i < fields.length - 1; i++) {
            // Get the values of the fields
            LocalTime firstTime = (LocalTime) wrapper.getPropertyValue(fields[i]);
            LocalTime secondTime = (LocalTime) wrapper.getPropertyValue(fields[i + 1]);

            if (firstTime != null && secondTime != null && !firstTime.isBefore(secondTime)) {
                // Add a constraint violation to the context
                ValidatorUtils.addConstraintViolation(context, message, "sequentialTimes");
                return false;
            }
        }

        return true;
    }
}