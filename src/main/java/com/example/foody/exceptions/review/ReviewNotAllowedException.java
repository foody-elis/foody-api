package com.example.foody.exceptions.review;

public class ReviewNotAllowedException extends RuntimeException {
    public ReviewNotAllowedException() {
        super("Review not allowed.");
    }

    public ReviewNotAllowedException(String objectName, long objectId) {
        super(String.format("Review not allowed for %s with id = %s.", objectName, objectId));
    }

    public ReviewNotAllowedException(String objectName, long objectId, String cause) {
        super(String.format("Review not allowed for %s with id = %s. Cause: %s.", objectName, objectId, cause));
    }
}