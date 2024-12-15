package com.example.foody.utils.validator.uniform_nullity;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(TYPE)
@Retention(RUNTIME)
@Constraint(validatedBy = UniformNullityValidator.class)
@Repeatable(UniformNullity.List.class)
public @interface UniformNullity {
    String message() default "all fields must be null or all fields must be not null";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    String[] fields();

    @Target(TYPE)
    @Retention(RUNTIME)
    @interface List {
        UniformNullity[] value();
    }
}
