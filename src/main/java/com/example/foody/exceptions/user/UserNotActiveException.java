package com.example.foody.exceptions.user;

public class UserNotActiveException extends RuntimeException {

    public UserNotActiveException(String email) {
        super(String.format("User with the email %s is not active.", email));
    }
}
