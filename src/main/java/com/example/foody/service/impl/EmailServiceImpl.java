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

@Service
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender javaMailSender;
    private final EmailTemplateService emailTemplateService;
    @Value("${spring.mail.username}")
    private String FROM;

    public EmailServiceImpl(JavaMailSender javaMailSender, EmailTemplateService emailTemplateService) {
        this.javaMailSender = javaMailSender;
        this.emailTemplateService = emailTemplateService;
    }

    @Async
    @Override
    public void sendTemplatedEmail(String to, EmailTemplateType emailTemplateType, Map<EmailPlaceholder, Object> variables) {
        String emailContent = emailTemplateService.prepareEmailContent(emailTemplateType, variables);
        String subject = emailTemplateService.extractSubject(emailContent);
        String body = emailTemplateService.extractBody(emailContent);
        send(to, subject, body);
    }

    private void send(String to, String subject, String text) {
//        MimeMessage message = javaMailSender.createMimeMessage();
        SimpleMailMessage message = new SimpleMailMessage();

        try {
//            MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "UTF-8"); // todo multipart?

            message.setFrom(FROM);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text); // todo html?

            this.javaMailSender.send(message);
        } catch (Exception e) {
            throw new EmailSendingException(to, subject);
        }
    }
}