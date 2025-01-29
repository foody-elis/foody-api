package com.example.foody.exceptions.firebase;

/**
 * Exception thrown when there is an error during the creation of a Firebase custom token.
 */
public class FirebaseCustomTokenCreationException extends RuntimeException {

    /**
     * Constructs a new FirebaseCustomTokenCreationException with a formatted message indicating the user ID that caused the token creation error.
     *
     * @param uid the user ID for which the custom token creation failed
     */
    public FirebaseCustomTokenCreationException(String uid) {
        super(String.format("Failed to create custom token for user with uid = %s.", uid));
    }
}