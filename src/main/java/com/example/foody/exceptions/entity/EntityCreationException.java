package com.example.foody.exceptions.entity;

/**
 * Exception thrown when there is an error during the creation of an entity.
 */
public class EntityCreationException extends RuntimeException {

    /**
     * Constructs a new EntityCreationException with a formatted message indicating the object name that failed to be created.
     *
     * @param objectName the name of the object that failed to be created
     */
    public EntityCreationException(String objectName) {
        super(String.format("There was an error while trying to create this %s.", objectName));
    }
}