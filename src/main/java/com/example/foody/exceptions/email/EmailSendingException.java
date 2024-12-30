package com.example.foody.exceptions.email;

public class EmailSendingException extends RuntimeException {
    public EmailSendingException(String message) {
        super(message);
    }

    public EmailSendingException(String email, String subject) {
        super(String.format("Failed to send email to %s with subject '%s'.", email, subject));
    }
}