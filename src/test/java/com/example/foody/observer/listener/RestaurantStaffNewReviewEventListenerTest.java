package com.example.foody.observer.listener;

import com.example.foody.model.Restaurant;
import com.example.foody.model.Review;
import com.example.foody.model.user.CustomerUser;
import com.example.foody.model.user.User;
import com.example.foody.observer.listener.impl.RestaurantStaffNewReviewEventListener;
import com.example.foody.service.EmailService;
import com.example.foody.utils.enums.EmailPlaceholder;
import com.example.foody.utils.enums.EmailTemplateType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Map;

import static org.mockito.Mockito.*;

public class RestaurantStaffNewReviewEventListenerTest {

    private EmailService emailService;
    private User restaurantStaff;
    private RestaurantStaffNewReviewEventListener listener;

    @BeforeEach
    void setUp() {
        emailService = mock(EmailService.class);
        restaurantStaff = mock(User.class);
        listener = new RestaurantStaffNewReviewEventListener(emailService, restaurantStaff);
    }

    @Test
    void updateWhenCalledSendsEmailWithCorrectDetails() {
        // Arrange
        Review review = mock(Review.class);
        Restaurant restaurant = mock(Restaurant.class);
        CustomerUser customer = mock(CustomerUser.class);
        User customerUser = mock(User.class);

        when(review.getRestaurant()).thenReturn(restaurant);
        when(restaurant.getName()).thenReturn("Test Restaurant");
        when(review.getRating()).thenReturn(4);
        when(review.getCreatedAt()).thenReturn(LocalDateTime.of(2025, 1, 25, 15, 30, 45));

        when(review.getCustomer()).thenReturn(customer);
        when(customer.getName()).thenReturn("John");
        when(customer.getSurname()).thenReturn("Doe");

        when(restaurantStaff.getName()).thenReturn("StaffName");
        when(restaurantStaff.getSurname()).thenReturn("StaffSurname");
        when(restaurantStaff.getEmail()).thenReturn("staff@example.com");

        Map<EmailPlaceholder, Object> expectedVariables = Map.of(
                EmailPlaceholder.RESTAURANT_NAME, "Test Restaurant",
                EmailPlaceholder.NAME, "StaffName",
                EmailPlaceholder.SURNAME, "StaffSurname",
                EmailPlaceholder.CUSTOMER_NAME, "John",
                EmailPlaceholder.CUSTOMER_SURNAME, "Doe",
                EmailPlaceholder.DATE_TIME, "25/01/2025 15:30:45",
                EmailPlaceholder.RATING, 4
        );

        // Act
        listener.update(review);

        // Assert
        verify(emailService, times(1)).sendTemplatedEmail(
                "staff@example.com",
                EmailTemplateType.NEW_REVIEW,
                expectedVariables
        );
    }
}
