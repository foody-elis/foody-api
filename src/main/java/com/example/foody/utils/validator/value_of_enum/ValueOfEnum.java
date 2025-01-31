package com.example.foody.utils.validator.value_of_enum;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Annotation to validate that a field or parameter has a value that is one of the specified enum values.
 */
@Target({FIELD, PARAMETER})
@Retention(RUNTIME)
@Constraint(validatedBy = ValueOfEnumValidator.class)
public @interface ValueOfEnum {

    /**
     * The error message that will be shown when the value is not valid.
     *
     * @return the error message
     */
    String message() default "Invalid value. Must be any of enum {enumClass}";

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
     * Specifies the enum class containing the valid values.
     *
     * @return the enum class
     */
    Class<? extends Enum<?>> enumClass();
}