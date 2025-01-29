package com.example.foody.exceptions.entity;

/**
 * Exception thrown when there is a duplicate entity.
 */
public class EntityDuplicateException extends RuntimeException {

    /**
     * Constructs a new EntityDuplicateException with a formatted message indicating the object name, property name, and value that caused the duplication error.
     *
     * @param objectName   the name of the object that is duplicated
     * @param propertyName the name of the property that caused the duplication error
     * @param value        the value of the property that caused the duplication error
     */
    public EntityDuplicateException(String objectName, String propertyName, String value) {
        super(String.format("There is already a %s with %s = %s.", objectName, propertyName, value));
    }

    /**
     * Constructs a new EntityDuplicateException with a formatted message indicating the object name, property name, and value that caused the duplication error.
     *
     * @param objectName   the name of the object that is duplicated
     * @param propertyName the name of the property that caused the duplication error
     * @param value        the value of the property that caused the duplication error
     */
    public EntityDuplicateException(String objectName, String propertyName, long value) {
        super(String.format("There is already a %s with %s = %d.", objectName, propertyName, value));
    }

    /**
     * Constructs a new EntityDuplicateException with a formatted message indicating the object name, two property names, and their values that caused the duplication error.
     *
     * @param objectName    the name of the object that is duplicated
     * @param propertyName  the name of the first property that caused the duplication error
     * @param value         the value of the first property that caused the duplication error
     * @param propertyName2 the name of the second property that caused the duplication error
     * @param value2        the value of the second property that caused the duplication error
     */
    public EntityDuplicateException(String objectName, String propertyName, long value, String propertyName2, long value2) {
        super(
                String.format(
                        "There is already a %s with %s = %d and %s = %d.",
                        objectName,
                        propertyName,
                        value,
                        propertyName2,
                        value2
                )
        );
    }

    /**
     * Constructs a new EntityDuplicateException with a formatted message indicating the object name, two property names, and their values that caused the duplication error.
     *
     * @param objectName    the name of the object that is duplicated
     * @param propertyName  the name of the first property that caused the duplication error
     * @param value         the value of the first property that caused the duplication error
     * @param propertyName2 the name of the second property that caused the duplication error
     * @param value2        the value of the second property that caused the duplication error
     */
    public EntityDuplicateException(String objectName, String propertyName, String value, String propertyName2, long value2) {
        super(
                String.format(
                        "There is already a %s with %s = %s and %s = %d.",
                        objectName,
                        propertyName,
                        value,
                        propertyName2,
                        value2
                )
        );
    }
}