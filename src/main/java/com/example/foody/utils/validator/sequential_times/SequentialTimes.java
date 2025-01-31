package com.example.foody.utils.validator.sequential_times;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Annotation to validate that the second time is after the first time.
 */
@Target(TYPE)
@Retention(RUNTIME)
@Constraint(validatedBy = SequentialTimesValidator.class)
public @interface SequentialTimes {

    /**
     * The error message that will be shown when the validation fails.
     *
     * @return the error message
     */
    String message() default "second time must be after first time";

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
     * Specifies the fields to be validated.
     *
     * @return the array of field names
     */
    String[] fields();
}