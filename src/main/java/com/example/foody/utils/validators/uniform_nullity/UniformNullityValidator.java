package com.example.foody.utils.validators.uniform_nullity;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.lang.reflect.Field;

public class UniformNullityValidator implements ConstraintValidator<UniformNullity, Object> {
    private String[] fields;

    @Override
    public void initialize(UniformNullity constraintAnnotation) {
        fields = constraintAnnotation.fields();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        boolean allNull = true;
        boolean allNotNull = true;

        for (String field : fields) {
            try {
                Field declaredField = value.getClass().getDeclaredField(field);
                declaredField.setAccessible(true);
                Object fieldValue = declaredField.get(value);
                if (fieldValue != null) {
                    allNull = false;
                } else {
                    allNotNull = false;
                }
            } catch (Exception e) {
                return false;
            }
        }

        return allNull || allNotNull;
    }
}