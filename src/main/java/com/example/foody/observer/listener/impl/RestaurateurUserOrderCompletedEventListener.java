package com.example.foody.observer.listener.impl;

import com.example.foody.helper.OrderHelper;
import com.example.foody.model.Order;
import com.example.foody.observer.listener.EventListener;
import com.example.foody.service.EmailService;
import com.example.foody.utils.enums.EmailPlaceholder;
import com.example.foody.utils.enums.EmailTemplateType;

import java.util.Map;

public class RestaurateurUserOrderCompletedEventListener implements EventListener<Order> {
    private final EmailService emailService;
    private final OrderHelper orderHelper;

    public RestaurateurUserOrderCompletedEventListener(EmailService emailService, OrderHelper orderHelper) {
        this.emailService = emailService;
        this.orderHelper = orderHelper;
    }

    @Override
    public void update(Order order) {
        Map<EmailPlaceholder, Object> variables = Map.of(
                EmailPlaceholder.ORDER_ID, order.getId(),
                EmailPlaceholder.RESTAURATEUR_NAME, order.getRestaurant().getRestaurateur().getName(),
                EmailPlaceholder.RESTAURATEUR_SURNAME, order.getRestaurant().getRestaurateur().getSurname(),
                EmailPlaceholder.AMOUNT, getOrderedAmount(order)
        );
        emailService.sendTemplatedEmail(
                order.getRestaurant().getRestaurateur().getEmail(),
                EmailTemplateType.PAYMENT_RECEIVED,
                variables
        );
    }

    private double getOrderedAmount(Order order) {
        double orderAmount = orderHelper.findAmountByOrderId(order.getId());
        return Math.round(orderAmount * 100.0) / 100.0;
    }
}