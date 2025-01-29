package com.example.foody.exceptions.review;

/**
 * Exception thrown when a review is not allowed.
 */
public class ReviewNotAllowedException extends RuntimeException {

    /**
     * Constructs a new ReviewNotAllowedException with a default message indicating that the review is not allowed.
     */
    public ReviewNotAllowedException() {
        super("Review not allowed.");
    }

    /**
     * Constructs a new ReviewNotAllowedException with a formatted message indicating the object name and ID for which the review is not allowed.
     *
     * @param objectName the name of the object for which the review is not allowed
     * @param objectId   the ID of the object for which the review is not allowed
     */
    public ReviewNotAllowedException(String objectName, long objectId) {
        super(String.format("Review not allowed for %s with id = %s.", objectName, objectId));
    }

    /**
     * Constructs a new ReviewNotAllowedException with a formatted message indicating the object name, ID, and the cause that made the review not allowed.
     *
     * @param objectName the name of the object for which the review is not allowed
     * @param objectId   the ID of the object for which the review is not allowed
     * @param cause      the cause of the review not being allowed
     */
    public ReviewNotAllowedException(String objectName, long objectId, String cause) {
        super(String.format("Review not allowed for %s with id = %s. Cause: %s.", objectName, objectId, cause));
    }
}