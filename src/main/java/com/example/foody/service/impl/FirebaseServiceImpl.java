package com.example.foody.service.impl;

import com.example.foody.exceptions.firebase.FirebaseCustomTokenCreationException;
import com.example.foody.service.FirebaseService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import org.springframework.stereotype.Service;

/**
 * Implementation of the {@link FirebaseService} interface.
 * <p>
 * Provides methods to create and verify Firebase custom tokens.
 */
@Service
public class FirebaseServiceImpl implements FirebaseService {

    /**
     * {@inheritDoc}
     * <p>
     * This method creates a custom Firebase token for the given user ID.
     *
     * @param uid the user ID for which to create the custom token
     * @return the created custom token
     * @throws FirebaseCustomTokenCreationException if there is an error during the creation of the custom token
     */
    @Override
    public String createCustomToken(String uid) {
        try {
            return FirebaseAuth.getInstance().createCustomToken(uid);
        } catch (FirebaseAuthException | IllegalArgumentException e) {
            throw new FirebaseCustomTokenCreationException(uid);
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * This method verifies the given Firebase token.
     *
     * @param token the token to verify
     * @return true if the token is valid, false otherwise
     */
    @Override
    public boolean verifyToken(String token) {
        try {
            FirebaseAuth.getInstance().verifyIdToken(token);
        } catch (FirebaseAuthException | IllegalArgumentException e) {
            return false;
        }

        return true;
    }
}