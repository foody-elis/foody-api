package com.example.foody.service;

import com.example.foody.utils.enums.EmailPlaceholder;
import com.example.foody.utils.enums.EmailTemplateType;

import java.util.Map;

/**
 * Service interface for preparing email templates.
 */
public interface EmailTemplateService {

    /**
     * Prepares the email content based on the given template type and variables.
     *
     * @param emailTemplateType the type of email template to use
     * @param variables the variables to replace in the email template
     * @return the prepared email content
     */
    String prepareEmailContent(EmailTemplateType emailTemplateType, Map<EmailPlaceholder, Object> variables);

    /**
     * Extracts the subject from the given template content.
     *
     * @param templateContent the content of the email template
     * @return the extracted subject
     */
    String extractSubject(String templateContent);

    /**
     * Extracts the body from the given template content.
     *
     * @param templateContent the content of the email template
     * @return the extracted body
     */
    String extractBody(String templateContent);
}