package com.example.foody.service;

import com.example.foody.utils.enums.EmailPlaceholder;
import com.example.foody.utils.enums.EmailTemplateType;

import java.util.Map;

/**
 * Service interface for sending templated emails.
 */
public interface EmailService {

    /**
     * Sends a templated email.
     *
     * @param to the recipient's email address
     * @param emailTemplateType the type of email template to use
     * @param variables the variables to replace in the email template
     */
    void sendTemplatedEmail(String to, EmailTemplateType emailTemplateType, Map<EmailPlaceholder, Object> variables);
}