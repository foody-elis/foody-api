package com.example.foody.utils.validator.sequential_times;

import com.example.foody.utils.validator.ValidatorUtils;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.BeanWrapperImpl;

import java.time.LocalTime;

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
        if (value == null || fields == null || fields.length < 2) {
            return true;
        }

        BeanWrapperImpl wrapper = new BeanWrapperImpl(value);

        for (int i = 0; i < fields.length - 1; i++) {
            LocalTime firstTime = (LocalTime) wrapper.getPropertyValue(fields[i]);
            LocalTime secondTime = (LocalTime) wrapper.getPropertyValue(fields[i + 1]);

            if (firstTime != null && secondTime != null && !firstTime.isBefore(secondTime)) {
                ValidatorUtils.addConstraintViolation(context, message, "sequentialTimes");
                return false;
            }
        }

        return true;
    }
}