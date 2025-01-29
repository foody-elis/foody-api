package com.example.foody.exceptions.auth;

/**
 * Exception thrown when authentication fails due to invalid credentials.
 */
public class InvalidCredentialsException extends RuntimeException {

    /**
     * Constructs a new InvalidCredentialsException with a default error message.
     */
    public InvalidCredentialsException() {
        super("Authentication failed: the credentials provided are invalid.");
    }
}