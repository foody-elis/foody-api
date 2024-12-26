package com.example.foody.service.impl;

import com.example.foody.exceptions.email.EmailSendingException;
import com.example.foody.service.EmailTemplateService;
import com.example.foody.utils.enums.EmailPlaceholder;
import com.example.foody.utils.enums.EmailTemplateType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

@Service
public class EmailTemplateServiceImpl implements EmailTemplateService {
    private static final String SUBJECT_MARKER = "# Subject";
    private static final String BODY_MARKER = "# Body";

    @Value("${spring.mail.templates-path}")
    private String TEMPLATES_PATH;

    @Override
    public String prepareEmailContent(EmailTemplateType emailTemplateType, Map<EmailPlaceholder, Object> variables) {
        String templateContent = readTemplateContent(emailTemplateType);
        return replacePlaceholders(templateContent, variables);
    }

    @Override
    public String extractSubject(String templateContent) {
        String[] parts = templateContent.split(SUBJECT_MARKER + "|" + BODY_MARKER);
        return parts[1].trim();
    }

    @Override
    public String extractBody(String templateContent) {
        String[] parts = templateContent.split(SUBJECT_MARKER + "|" + BODY_MARKER);
        return parts[2].trim();
    }

    private String readTemplateContent(EmailTemplateType emailTemplateType) {
        try {
            Path templatePath = Path.of(TEMPLATES_PATH, emailTemplateType.getTemplateName() + ".txt");
            return Files.readString(templatePath);
        } catch (IOException e) {
            throw new EmailSendingException("Error while reading email template: " + emailTemplateType.getTemplateName());
        }
    }

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