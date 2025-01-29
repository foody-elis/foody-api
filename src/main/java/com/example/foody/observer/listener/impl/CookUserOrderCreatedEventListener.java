package com.example.foody.observer.listener.impl;

import com.example.foody.model.Order;
import com.example.foody.model.user.CookUser;
import com.example.foody.observer.listener.EventListener;
import com.example.foody.service.EmailService;
import com.example.foody.utils.enums.EmailPlaceholder;
import com.example.foody.utils.enums.EmailTemplateType;
import lombok.AllArgsConstructor;

import java.util.Map;

/**
 * Listener implementation for handling order creation events for cook users.
 * <p>
 * Implements the {@link EventListener} interface for {@link Order} events.
 * <p>
 * Sends an email notification to the cook user when a new order is created.
 */
@AllArgsConstructor
public class CookUserOrderCreatedEventListener implements EventListener<Order> {

    private final EmailService emailService;
    private final CookUser cookUser;

    /**
     * {@inheritDoc}
     * <p>
     * This method is called when an order is created.
     * <p>
     * Sends an email notification to the cook user with the order details.
     *
     * @param order the order that was created
     */
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