package com.example.foody.exceptions.entity;

public class EntityDataIntegrityViolationException extends RuntimeException {
    public EntityDataIntegrityViolationException(String objectName) {
        super(String.format("Data integrity violation while trying to create this %s.", objectName));
    }

    public EntityDataIntegrityViolationException(String objectName, String constraintName) {
        super(String.format("Data integrity violation while trying to create this %s. Violated constraint: %s.", objectName, constraintName));
    }
}