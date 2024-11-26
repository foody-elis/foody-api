package com.example.foody.utils.validators.sequential_times;

import com.example.foody.utils.ValidatorUtils;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.lang.reflect.Field;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

public class SequentialTimesValidator implements ConstraintValidator<SequentialTimes, Object> {
    private String message;
    private String[] fields;

    @Override
    public void initialize(SequentialTimes constraintAnnotation) {
        message = constraintAnnotation.message();
        fields = constraintAnnotation.fields();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        try {
            List<String> fieldList = Arrays.asList(fields);
            for (int i = 0; i < fieldList.size() - 1; i++) {
                Field firstField = value.getClass().getDeclaredField(fieldList.get(i));
                Field secondField = value.getClass().getDeclaredField(fieldList.get(i + 1));

                firstField.setAccessible(true);
                secondField.setAccessible(true);

                LocalTime firstValue = (LocalTime) firstField.get(value);
                LocalTime secondValue = (LocalTime) secondField.get(value);

                if (firstValue == null || secondValue == null || !firstValue.isBefore(secondValue)) {
                    ValidatorUtils.addConstraintViolation(context, message, "sequentialTimes");
                    return false;
                }
            }
        } catch (Exception e) {
            ValidatorUtils.addConstraintViolation(context, message, "sequentialTimes");
            return false;
        }

        return true;
    }
}