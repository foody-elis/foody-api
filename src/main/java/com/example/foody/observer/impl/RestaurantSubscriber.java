package com.example.foody.observer.impl;

import com.example.foody.model.Order;
import com.example.foody.observer.Subscriber;
import com.example.foody.service.EmailService;
import com.example.foody.utils.enums.EmailPlaceholder;
import com.example.foody.utils.enums.EmailTemplateType;

import java.util.Map;

public class RestaurantSubscriber implements Subscriber<Order> {
    private final EmailService emailService;

    public RestaurantSubscriber(EmailService emailService) {
        this.emailService = emailService;
    }

    @Override
    public void update(Order order) {
        Map<EmailPlaceholder, Object> variables = Map.of(
                EmailPlaceholder.NAME, order.getRestaurant().getRestaurateur().getName(),
                EmailPlaceholder.SURNAME, order.getRestaurant().getRestaurateur().getSurname(),
                EmailPlaceholder.ORDER_ID, order.getId(),
                EmailPlaceholder.AMOUNT, 30 // todo set the correct amount
        );
        emailService.sendTemplatedEmail(
                order.getRestaurant().getRestaurateur().getEmail(),
                EmailTemplateType.PAYMENT_RECEIVED,
                variables
        );
    }
}