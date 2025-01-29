package com.example.foody.service;

import com.example.foody.utils.enums.EmailPlaceholder;
import com.example.foody.utils.enums.EmailTemplateType;

import java.util.Map;

public interface EmailTemplateService {
    String prepareEmailContent(EmailTemplateType emailTemplateType, Map<EmailPlaceholder, Object> variables);

    String extractSubject(String templateContent);

    String extractBody(String templateContent);
}