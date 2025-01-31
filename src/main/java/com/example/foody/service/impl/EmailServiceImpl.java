package com.example.foody.service.impl;

import com.example.foody.exceptions.email.EmailSendingException;
import com.example.foody.service.EmailService;
import com.example.foody.service.EmailTemplateService;
import com.example.foody.utils.enums.EmailPlaceholder;
import com.example.foody.utils.enums.EmailTemplateType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Implementation of the {@link EmailService} interface.
 * <p>
 * Provides methods to send templated emails.
 */
@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;
    private final EmailTemplateService emailTemplateService;

    @Value("${spring.mail.username}")
    private String FROM;

    /**
     * Constructs an instance of {@link EmailServiceImpl}.
     *
     * @param javaMailSender the JavaMailSender to use for sending emails
     * @param emailTemplateService the service to use for preparing email templates
     */
    public EmailServiceImpl(JavaMailSender javaMailSender, EmailTemplateService emailTemplateService) {
        this.javaMailSender = javaMailSender;
        this.emailTemplateService = emailTemplateService;
    }

    /**
     * Sends a templated email asynchronously.
     *
     * @param to the recipient email address
     * @param emailTemplateType the type of email template to use
     * @param variables the variables to replace in the email template
     */
    @Async
    @Override
    public void sendTemplatedEmail(
            String to,
            EmailTemplateType emailTemplateType,
            Map<EmailPlaceholder, Object> variables
    ) {
        String emailContent = emailTemplateService.prepareEmailContent(emailTemplateType, variables);
        String subject = emailTemplateService.extractSubject(emailContent);
        String body = emailTemplateService.extractBody(emailContent);
        send(to, subject, body);
    }

    /**
     * Sends an email.
     *
     * @param to the recipient email address
     * @param subject the subject of the email
     * @param text the body of the email
     * @throws EmailSendingException if there is an error sending the email
     */
    private void send(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();

        try {
            message.setFrom(FROM);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);

            this.javaMailSender.send(message);
        } catch (Exception e) {
            throw new EmailSendingException(to, subject);
        }
    }
}