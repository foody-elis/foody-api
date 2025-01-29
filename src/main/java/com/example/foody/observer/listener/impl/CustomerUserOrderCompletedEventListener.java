package com.example.foody.observer.listener.impl;

import com.example.foody.model.Order;
import com.example.foody.observer.listener.EventListener;
import com.example.foody.service.EmailService;
import com.example.foody.utils.enums.EmailPlaceholder;
import com.example.foody.utils.enums.EmailTemplateType;
import lombok.AllArgsConstructor;

import java.util.Map;

/**
 * Listener implementation for handling order completion events for customer users.
 * <p>
 * Implements the {@link EventListener} interface for {@link Order} events.
 * <p>
 * Sends an email notification to the customer user when an order is completed.
 */
@AllArgsConstructor
public class CustomerUserOrderCompletedEventListener implements EventListener<Order> {

    private final EmailService emailService;

    /**
     * {@inheritDoc}
     * <p>
     * This method is called when an order is completed.
     * <p>
     * Sends an email notification to the customer user with the order details.
     *
     * @param order the order that was completed
     */
    @Override
    public void update(Order order) {
        Map<EmailPlaceholder, Object> variables = Map.of(
                EmailPlaceholder.CUSTOMER_NAME, order.getBuyer().getUser().getName(),
                EmailPlaceholder.CUSTOMER_SURNAME, order.getBuyer().getUser().getSurname(),
                EmailPlaceholder.RESTAURANT_NAME, order.getRestaurant().getName()
        );
        emailService.sendTemplatedEmail(
                order.getBuyer().getUser().getEmail(),
                EmailTemplateType.REVIEW_INVITATION,
                variables
        );
    }
}