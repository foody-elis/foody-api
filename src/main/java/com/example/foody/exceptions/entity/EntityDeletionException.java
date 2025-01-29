package com.example.foody.exceptions.entity;

/**
 * Exception thrown when there is an error during the deletion of an entity.
 */
public class EntityDeletionException extends RuntimeException {

    /**
     * Constructs a new EntityDeletionException with a formatted message indicating the object name, property name, and value that caused the deletion error.
     *
     * @param objectName   the name of the object that failed to be deleted
     * @param propertyName the name of the property that caused the deletion error
     * @param value        the value of the property that caused the deletion error
     */
    public EntityDeletionException(String objectName, String propertyName, long value) {
        super(
                String.format(
                        "There was an error while trying to delete %s with %s = %d.",
                        objectName,
                        propertyName,
                        value
                )
        );
    }
}