package com.example.foody.exceptions.user;

/**
 * Exception thrown when a user is not active.
 */
public class UserNotActiveException extends RuntimeException {

    /**
     * Constructs a new UserNotActiveException with a formatted message indicating the email of the user who is not active.
     *
     * @param email the email of the user who is not active
     */
    public UserNotActiveException(String email) {
        super(String.format("User with the email %s is not active.", email));
    }
}