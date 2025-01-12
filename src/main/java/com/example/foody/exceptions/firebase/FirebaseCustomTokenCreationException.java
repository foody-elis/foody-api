package com.example.foody.exceptions.firebase;

public class FirebaseCustomTokenCreationException extends RuntimeException {
    public FirebaseCustomTokenCreationException(String uid) {
        super(String.format("Failed to create custom token for user with uid = %s.", uid));
    }
}