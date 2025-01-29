package com.example.foody.exceptions.entity;

/**
 * Exception thrown when there is an error during the editing of an entity.
 */
public class EntityEditException extends RuntimeException {

    /**
     * Constructs a new EntityEditException with a formatted message indicating the object name, property name, and value that caused the editing error.
     *
     * @param objectName   the name of the object that failed to be edited
     * @param propertyName the name of the property that caused the editing error
     * @param value        the value of the property that caused the editing error
     */
    public EntityEditException(String objectName, String propertyName, long value) {
        super(String.format("There was an error while trying to edit %s with %s = %d.", objectName, propertyName, value));
    }
}