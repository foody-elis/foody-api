package com.example.foody.observer.listener.impl;

import com.example.foody.model.Order;
import com.example.foody.model.order_dish.OrderDish;
import com.example.foody.observer.listener.EventListener;
import com.example.foody.service.EmailService;
import com.example.foody.utils.enums.EmailPlaceholder;
import com.example.foody.utils.enums.EmailTemplateType;

import java.util.Map;
import java.util.stream.Collectors;

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
                EmailPlaceholder.RESTAURANT_REVIEW_LINK, getRestaurantReviewLink(order),
                EmailPlaceholder.DISHES_REVIEW_LINKS, getDishesReviewLinks(order)
        );
        emailService.sendTemplatedEmail(
                order.getBuyer().getUser().getEmail(),
                EmailTemplateType.REVIEW_INVITATION,
                variables
        );
    }

    private String getRestaurantReviewLink(Order order) {
        return String.format(
                "https://www.%s.com/reviews",
                order.getRestaurant().getName().replaceAll("\\s+", "").toLowerCase() // todo should be review link
        );
    }

    private String getDishesReviewLinks(Order order) {
        return order.getOrderDishes().stream()
                .map(this::getDishReviewLink)
                .collect(Collectors.joining("\n"));
    }

    private String getDishReviewLink(OrderDish orderDish) {
        return String.format(
                "•  %s: https://www.%s.com/reviews",
                orderDish.getDish().getName(),
                orderDish.getDish().getName().replaceAll("\\s+", "").toLowerCase() // todo should be review link
        );
    }
}