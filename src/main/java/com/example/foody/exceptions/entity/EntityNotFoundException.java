package com.example.foody.exceptions.entity;

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String objectName, String propertyName, String value) {
        super(String.format("No %s with %s = %s found.", objectName, propertyName, value));
    }

    public EntityNotFoundException(String objectName, String propertyName, long value) {
        super(String.format("No %s with %s = %d found.", objectName, propertyName, value));
    }
}
