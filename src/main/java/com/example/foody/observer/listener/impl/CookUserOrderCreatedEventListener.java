package com.example.foody.observer.listener.impl;

import com.example.foody.model.Order;
import com.example.foody.model.user.CookUser;
import com.example.foody.observer.listener.EventListener;
import com.example.foody.service.EmailService;
import com.example.foody.utils.enums.EmailPlaceholder;
import com.example.foody.utils.enums.EmailTemplateType;

import java.util.Map;

public class CookUserOrderCreatedEventListener implements EventListener<Order> {
    private final EmailService emailService;
    private final CookUser cookUser;

    public CookUserOrderCreatedEventListener(EmailService emailService, CookUser cookUser) {
        this.emailService = emailService;
        this.cookUser = cookUser;
    }

    @Override
    public void update(Order order) {
        Map<EmailPlaceholder, Object> variables = Map.of(
                EmailPlaceholder.RESTAURANT_NAME, order.getRestaurant().getName(),
                EmailPlaceholder.COOK_NAME, cookUser.getName(),
                EmailPlaceholder.COOK_SURNAME, cookUser.getSurname(),
                EmailPlaceholder.ORDER_ID, order.getId(),
                EmailPlaceholder.CUSTOMER_NAME, order.getBuyer().getUser().getName(),
                EmailPlaceholder.CUSTOMER_SURNAME, order.getBuyer().getUser().getSurname()
        );
        emailService.sendTemplatedEmail(
                cookUser.getEmail(),
                EmailTemplateType.NEW_ORDER,
                variables
        );
    }
}