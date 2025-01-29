package com.example.foody.exceptions.review;

/**
 * Exception thrown when there is an attempt to access a review that is forbidden.
 */
public class ForbiddenReviewAccessException extends RuntimeException {

    /**
     * Constructs a new ForbiddenReviewAccessException with a default message indicating that review access is denied.
     */
    public ForbiddenReviewAccessException() {
        super("Review access denied.");
    }
}