package com.example.foody.utils.validator.uniform_nullity;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Annotation to validate that all specified fields in a class are either all null or all non-null.
 */
@Target(TYPE)
@Retention(RUNTIME)
@Constraint(validatedBy = UniformNullityValidator.class)
@Repeatable(UniformNullity.List.class)
public @interface UniformNullity {

    /**
     * The error message that will be shown when the fields do not have uniform nullity.
     *
     * @return the error message
     */
    String message() default "all fields must be null or all fields must be not null";

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
     * Specifies the fields to be validated for uniform nullity.
     *
     * @return the array of field names
     */
    String[] fields();

    /**
     * Container annotation to allow multiple `UniformNullity` annotations on the same element.
     */
    @Target(TYPE)
    @Retention(RUNTIME)
    @interface List {
        /**
         * @return the array of `UniformNullity` annotations
         */
        UniformNullity[] value();
    }
}