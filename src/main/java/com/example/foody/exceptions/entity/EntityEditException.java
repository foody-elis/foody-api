package com.example.foody.exceptions.entity;

public class EntityEditException extends RuntimeException {
    public EntityEditException(String objectName, String propertyName, long value) {
        super(String.format("There was an error while trying to edit %s with %s = %d.", objectName, propertyName, value));
    }
}
