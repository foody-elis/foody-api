package com.example.foody.exceptions.user;

/**
 * Exception thrown when an invalid password is provided.
 */
public class InvalidPasswordException extends RuntimeException {

    /**
     * Constructs a new InvalidPasswordException with a default message indicating that the password provided is incorrect.
     */
    public InvalidPasswordException() {
        super("Invalid password: the password provided is incorrect.");
    }
}