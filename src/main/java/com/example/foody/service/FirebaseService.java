package com.example.foody.service;

/**
 * Service interface for Firebase operations.
 */
public interface FirebaseService {

    /**
     * Creates a custom token for a given user ID.
     *
     * @param uid the user ID for which to create the custom token
     * @return the created custom token
     */
    String createCustomToken(String uid);

    /**
     * Verifies the given token.
     *
     * @param token the token to verify
     * @return true if the token is valid, false otherwise
     */
    boolean verifyToken(String token);
}