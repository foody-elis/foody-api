package com.example.foody.utils.validator.uniform_nullity;

import com.example.foody.utils.validator.ValidatorUtils;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.lang.reflect.Field;

public class UniformNullityValidator implements ConstraintValidator<UniformNullity, Object> {
    private String message;
    private String[] fields;

    @Override
    public void initialize(UniformNullity constraintAnnotation) {
        message = constraintAnnotation.message();
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
                ValidatorUtils.addConstraintViolation(context, message, "uniformNullity");
                return false;
            }
        }

        if (!allNull && !allNotNull) {
            ValidatorUtils.addConstraintViolation(context, message, "uniformNullity");
            return false;
        }

        return true;
    }
}