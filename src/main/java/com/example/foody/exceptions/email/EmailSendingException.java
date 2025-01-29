package com.example.foody.exceptions.email;

/**
 * Exception thrown when there is an error sending an email.
 */
public class EmailSendingException extends RuntimeException {

    /**
     * Constructs a new EmailSendingException with the specified detail message.
     *
     * @param message the detail message
     */
    public EmailSendingException(String message) {
        super(message);
    }

    /**
     * Constructs a new EmailSendingException with a formatted message indicating the email and subject that failed to send.
     *
     * @param email   the recipient email address
     * @param subject the subject of the email
     */
    public EmailSendingException(String email, String subject) {
        super(String.format("Failed to send email to %s with subject '%s'.", email, subject));
    }
}