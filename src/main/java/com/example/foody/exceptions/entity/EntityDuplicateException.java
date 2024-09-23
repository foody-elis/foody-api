package com.example.foody.exceptions.entity;

public class EntityDuplicateException extends RuntimeException {

    public EntityDuplicateException(String objectName, String propertyName, String value) {
        super(String.format("There is already a %s with %s = %s.", objectName, propertyName, value));
    }

    public EntityDuplicateException(String objectName, String propertyName, long value) {
        super(String.format("There is already a %s with %s = %d.", objectName, propertyName, value));
    }

    public EntityDuplicateException(String objectName, String propertyName, long value, String propertyName2, long value2) {
        super(String.format("There is already a %s with %s = %d and %s = %d.", objectName, propertyName, value, propertyName2, value2));
    }

    public EntityDuplicateException(String objectName, String propertyName, String value, String propertyName2, long value2) {
        super(String.format("There is already a %s with %s = %s and %s = %d.", objectName, propertyName, value, propertyName2, value2));
    }
}
