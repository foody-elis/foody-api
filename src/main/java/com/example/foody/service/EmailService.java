package com.example.foody.service;

import com.example.foody.utils.enums.EmailPlaceholder;
import com.example.foody.utils.enums.EmailTemplateType;

import java.util.Map;

public interface EmailService {
    void sendTemplatedEmail(String to, EmailTemplateType emailTemplateType, Map<EmailPlaceholder, Object> variables);
}