package com.example.foody.exceptions.review;

public class ForbiddenReviewAccessException extends RuntimeException {
    public ForbiddenReviewAccessException() {
        super("Review access denied.");
    }
}