package com.example.foody.utils.validator.uniform_nullity;

import com.example.foody.utils.validator.ValidatorUtils;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.BeanWrapperImpl;

/**
 * Validator to check if all specified fields in a class are either all null or all non-null.
 */
public class UniformNullityValidator implements ConstraintValidator<UniformNullity, Object> {

    private String message;
    private String[] fields;

    /**
     * Initializes the validator with the given annotation.
     *
     * @param constraintAnnotation the annotation instance for a given constraint declaration
     */
    @Override
    public void initialize(UniformNullity constraintAnnotation) {
        message = constraintAnnotation.message();
        fields = constraintAnnotation.fields();
    }

    /**
     * Checks if the specified fields in the given object are either all null or all non-null.
     *
     * @param value the object to validate
     * @param context context in which the constraint is evaluated
     * @return true if the fields are either all null or all non-null, false otherwise
     */
    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null || fields == null || fields.length == 0) {
            return true;
        }

        // Use a BeanWrapper to access the fields of the object
        BeanWrapperImpl wrapper = new BeanWrapperImpl(value);

        boolean allNull = true;
        boolean allNotNull = true;

        for (String field : fields) {
            // Get the value of the field
            Object fieldValue = wrapper.getPropertyValue(field);

            if (fieldValue != null) {
                allNull = false;
            } else {
                allNotNull = false;
            }

            if (!allNull && !allNotNull) {
                // Add a constraint violation to the context
                ValidatorUtils.addConstraintViolation(context, message, "uniformNullity");
                return false;
            }
        }

        return true;
    }
}