package com.example.foody.exceptions.auth;

public class InvalidCredentialsException extends RuntimeException {
    public InvalidCredentialsException() {
        super("Authentication failed: the credentials provided are invalid.");
    }
}
