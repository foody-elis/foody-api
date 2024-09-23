package com.example.foody.exceptions.entity;

public class EntityCreationException extends RuntimeException {

    public EntityCreationException(String objectName) {
        super(String.format("There was an error while trying to create this %s.", objectName));
    }
}
