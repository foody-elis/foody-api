package com.example.foody.exceptions.user;

public class InvalidPasswordException extends RuntimeException {
    public InvalidPasswordException() {
        super("Invalid password: the password provided is incorrect.");
    }
}