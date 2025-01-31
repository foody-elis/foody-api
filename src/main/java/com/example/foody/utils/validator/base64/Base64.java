package com.example.foody.utils.validator.base64;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Annotation to validate that a field is either null or a valid non-empty Base64 string.
 */
@Target(FIELD)
@Retention(RUNTIME)
@Constraint(validatedBy = Base64Validator.class)
public @interface Base64 {

    /**
     * The error message that will be shown when the field is not valid.
     *
     * @return the error message
     */
    String message() default "must be null or a valid non-empty Base64 string";

    /**
     * Allows the specification of validation groups, to which this constraint belongs.
     *
     * @return the array of class groups
     */
    Class<?>[] groups() default {};

    /**
     * Can be used by clients of the Jakarta Bean Validation API to assign custom payload objects to a constraint.
     *
     * @return the array of payload classes
     */
    Class<? extends Payload>[] payload() default {};

    /**
     * Specifies whether the field can be null.
     *
     * @return true if the field can be null, false otherwise
     */
    boolean nullable() default true;
}