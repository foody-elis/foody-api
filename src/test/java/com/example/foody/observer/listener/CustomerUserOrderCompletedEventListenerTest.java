package com.example.foody.observer.listener;

import com.example.foody.model.Dish;
import com.example.foody.model.Order;
import com.example.foody.model.Restaurant;
import com.example.foody.model.order_dish.OrderDish;
import com.example.foody.model.user.BuyerUser;
import com.example.foody.model.user.User;
import com.example.foody.observer.listener.impl.CustomerUserOrderCompletedEventListener;
import com.example.foody.service.EmailService;
import com.example.foody.utils.enums.EmailPlaceholder;
import com.example.foody.utils.enums.EmailTemplateType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;

/**
 * Test class for {@link CustomerUserOrderCompletedEventListener} class using mock services.
 */
public class CustomerUserOrderCompletedEventListenerTest {

    private EmailService emailService;
    private CustomerUserOrderCompletedEventListener listener;

    @BeforeEach
    void setUp() {
        emailService = mock(EmailService.class);
        listener = new CustomerUserOrderCompletedEventListener(emailService);
    }

    @Test
    void updateWhenCalledSendsEmailWithCorrectDetails() {
        // Arrange
        Order order = mock(Order.class);
        Restaurant restaurant = mock(Restaurant.class);
        BuyerUser buyer = mock(BuyerUser.class);
        User buyerUser = mock(User.class);
        OrderDish dish1 = mock(OrderDish.class);
        OrderDish dish2 = mock(OrderDish.class);
        Dish mockDish1 = mock(Dish.class);
        Dish mockDish2 = mock(Dish.class);

        when(order.getRestaurant()).thenReturn(restaurant);
        when(restaurant.getName()).thenReturn("Test Restaurant");

        when(order.getBuyer()).thenReturn(buyer);
        when(buyer.getUser()).thenReturn(buyerUser);
        when(buyerUser.getName()).thenReturn("John");
        when(buyerUser.getSurname()).thenReturn("Doe");
        when(buyerUser.getEmail()).thenReturn("john.doe@example.com");

        when(order.getOrderDishes()).thenReturn(List.of(dish1, dish2));
        when(dish1.getDish()).thenReturn(mockDish1);
        when(dish2.getDish()).thenReturn(mockDish2);
        when(mockDish1.getName()).thenReturn("Dish1");
        when(mockDish2.getName()).thenReturn("Dish2");

        String expectedRestaurantLink = "https://www.testrestaurant.com/reviews";
        String expectedDishLinks = "\u2022  Dish1: https://www.dish1.com/reviews\n\u2022  Dish2: https://www.dish2.com/reviews";

        Map<EmailPlaceholder, Object> expectedVariables = Map.of(
                EmailPlaceholder.CUSTOMER_NAME, "John",
                EmailPlaceholder.CUSTOMER_SURNAME, "Doe",
                EmailPlaceholder.RESTAURANT_NAME, "Test Restaurant"
        );

        // Act
        listener.update(order);

        // Assert
        verify(emailService, times(1)).sendTemplatedEmail(
                "john.doe@example.com",
                EmailTemplateType.REVIEW_INVITATION,
                expectedVariables
        );
    }
}