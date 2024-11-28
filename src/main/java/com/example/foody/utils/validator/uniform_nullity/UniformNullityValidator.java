package com.example.foody.utils.validator.uniform_nullity;

import com.example.foody.utils.validator.ValidatorUtils;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.BeanWrapperImpl;

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
        if (value == null || fields == null || fields.length == 0) {
            return true;
        }

        BeanWrapperImpl wrapper = new BeanWrapperImpl(value);

        boolean allNull = true;
        boolean allNotNull = true;

        for (String field : fields) {
            Object fieldValue = wrapper.getPropertyValue(field);

            if (fieldValue != null) {
                allNull = false;
            } else {
                allNotNull = false;
            }

            if (!allNull && !allNotNull) {
                ValidatorUtils.addConstraintViolation(context, message, "uniformNullity");
                return false;
            }
        }

        return true;
    }
}