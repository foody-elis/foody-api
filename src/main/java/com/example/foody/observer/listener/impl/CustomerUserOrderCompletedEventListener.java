package com.example.foody.observer.listener.impl;

import com.example.foody.model.Order;
import com.example.foody.observer.listener.EventListener;
import com.example.foody.service.EmailService;
import com.example.foody.utils.enums.EmailPlaceholder;
import com.example.foody.utils.enums.EmailTemplateType;

import java.util.Map;

public class CustomerUserOrderCompletedEventListener implements EventListener<Order> {
    private final EmailService emailService;

    public CustomerUserOrderCompletedEventListener(EmailService emailService) {
        this.emailService = emailService;
    }

    @Override
    public void update(Order order) {
        Map<EmailPlaceholder, Object> variables = Map.of(
                EmailPlaceholder.CUSTOMER_NAME, order.getBuyer().getUser().getName(),
                EmailPlaceholder.CUSTOMER_SURNAME, order.getBuyer().getUser().getSurname(),
                EmailPlaceholder.RESTAURANT_NAME, order.getRestaurant().getName(),
                EmailPlaceholder.REVIEW_LINK, order.getRestaurant().getName() + "/reviews" // todo should be review link
        );
        emailService.sendTemplatedEmail(
                order.getBuyer().getUser().getEmail(),
                EmailTemplateType.REVIEW_INVITATION,
                variables
        );
    }
}