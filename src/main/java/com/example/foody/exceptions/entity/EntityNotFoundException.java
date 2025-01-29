package com.example.foody.exceptions.entity;

/**
 * Exception thrown when an entity is not found.
 */
public class EntityNotFoundException extends RuntimeException {

    /**
     * Constructs a new EntityNotFoundException with a formatted message indicating the object name, property name, and value that caused the not found error.
     *
     * @param objectName   the name of the object that was not found
     * @param propertyName the name of the property that caused the not found error
     * @param value        the value of the property that caused the not found error
     */
    public EntityNotFoundException(String objectName, String propertyName, String value) {
        super(String.format("No %s with %s = %s found.", objectName, propertyName, value));
    }

    /**
     * Constructs a new EntityNotFoundException with a formatted message indicating the object name, property name, and value that caused the not found error.
     *
     * @param objectName   the name of the object that was not found
     * @param propertyName the name of the property that caused the not found error
     * @param value        the value of the property that caused the not found error
     */
    public EntityNotFoundException(String objectName, String propertyName, long value) {
        super(String.format("No %s with %s = %d found.", objectName, propertyName, value));
    }
}