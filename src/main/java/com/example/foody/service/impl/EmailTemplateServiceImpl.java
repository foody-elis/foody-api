package com.example.foody.service.impl;

import com.example.foody.exceptions.email.EmailSendingException;
import com.example.foody.service.EmailTemplateService;
import com.example.foody.utils.enums.EmailPlaceholder;
import com.example.foody.utils.enums.EmailTemplateType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

/**
 * Implementation of the {@link EmailTemplateService} interface.
 * <p>
 * Provides methods to prepare email content from templates.
 */
@Service
public class EmailTemplateServiceImpl implements EmailTemplateService {

    private static final String SUBJECT_MARKER = "# Subject";
    private static final String BODY_MARKER = "# Body";

    @Value("${spring.mail.templates-path}")
    private String TEMPLATES_PATH;

    /**
     * {@inheritDoc}
     * <p>
     * This method prepares the email content by reading the template and replacing placeholders with actual values.
     *
     * @param emailTemplateType the type of email template to use
     * @param variables the variables to replace in the email template
     * @return the prepared email content
     */
    @Override
    public String prepareEmailContent(EmailTemplateType emailTemplateType, Map<EmailPlaceholder, Object> variables) {
        String templateContent = readTemplateContent(emailTemplateType);
        return replacePlaceholders(templateContent, variables);
    }

    /**
     * {@inheritDoc}
     * <p>
     * This method extracts the subject from the email template content.
     *
     * @param templateContent the email template content
     * @return the subject of the email
     */
    @Override
    public String extractSubject(String templateContent) {
        String[] parts = templateContent.split(SUBJECT_MARKER + "|" + BODY_MARKER);
        return parts[1].trim();
    }

    /**
     * {@inheritDoc}
     * <p>
     * This method extracts the body from the email template content.
     *
     * @param templateContent the email template content
     * @return the body of the email
     */
    @Override
    public String extractBody(String templateContent) {
        String[] parts = templateContent.split(SUBJECT_MARKER + "|" + BODY_MARKER);
        return parts[2].trim();
    }

    /**
     * Reads the content of the email template from the file system.
     * <p>
     * This method reads the email template file based on the template type.
     *
     * @param emailTemplateType the type of email template to read
     * @return the content of the email template
     * @throws EmailSendingException if there is an error reading the email template
     */
    private String readTemplateContent(EmailTemplateType emailTemplateType) {
        try {
            Path templatePath = Path.of(TEMPLATES_PATH, emailTemplateType.getTemplateName() + ".txt");
            ClassPathResource classPathResource = new ClassPathResource(templatePath.toString());
            return classPathResource.getContentAsString(StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new EmailSendingException(
                    "Error while reading email template: " + emailTemplateType.getTemplateName()
            );
        }
    }

    /**
     * Replaces placeholders in the email template content with actual values.
     * <p>
     * This method iterates through the variables and replaces placeholders in the template content.
     *
     * @param templateContent the email template content
     * @param variables the variables to replace in the email template
     * @return the email content with placeholders replaced
     */
    private String replacePlaceholders(String templateContent, Map<EmailPlaceholder, Object> variables) {
        for (Map.Entry<EmailPlaceholder, Object> entry : variables.entrySet()) {
            templateContent = templateContent.replace(
                    entry.getKey().toString(),
                    entry.getValue().toString()
            );
        }
        return templateContent;
    }
}