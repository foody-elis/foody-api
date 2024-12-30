package com.example.foody.observer.listener.impl;

import com.example.foody.model.Review;
import com.example.foody.model.user.User;
import com.example.foody.observer.listener.EventListener;
import com.example.foody.service.EmailService;
import com.example.foody.utils.enums.EmailPlaceholder;
import com.example.foody.utils.enums.EmailTemplateType;

import java.time.format.DateTimeFormatter;
import java.util.Map;

public class RestaurantStaffNewReviewEventListener implements EventListener<Review> {
    private final EmailService emailService;
    private final User user; // RestaurateurUser or EmployeeUser

    public RestaurantStaffNewReviewEventListener(EmailService emailService, User user) {
        this.emailService = emailService;
        this.user = user;
    }

    @Override
    public void update(Review review) {
        Map<EmailPlaceholder, Object> variables = Map.of(
                EmailPlaceholder.RESTAURANT_NAME, review.getRestaurant().getName(),
                EmailPlaceholder.NAME, user.getName(),
                EmailPlaceholder.SURNAME, user.getSurname(),
                EmailPlaceholder.CUSTOMER_NAME, review.getCustomer().getName(),
                EmailPlaceholder.CUSTOMER_SURNAME, review.getCustomer().getSurname(),
                EmailPlaceholder.DATE_TIME, review.getCreatedAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")),
                EmailPlaceholder.RATING, review.getRating()
        );
        emailService.sendTemplatedEmail(
                user.getEmail(),
                EmailTemplateType.NEW_REVIEW,
                variables
        );
    }
}