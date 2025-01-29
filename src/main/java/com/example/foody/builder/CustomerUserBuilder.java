package com.example.foody.builder;

import com.example.foody.model.Booking;
import com.example.foody.model.CreditCard;
import com.example.foody.model.Order;
import com.example.foody.model.Review;
import com.example.foody.model.user.CustomerUser;

import java.util.List;

/**
 * Interface for building {@link CustomerUser} objects.
 * Extends the {@link UserBuilder} interface with CustomerUser as the type parameter.
 */
public interface CustomerUserBuilder extends UserBuilder<CustomerUser> {
    CustomerUserBuilder creditCard(CreditCard creditCard);

    CustomerUserBuilder reviews(List<Review> reviews);

    CustomerUserBuilder bookings(List<Booking> bookings);

    CustomerUserBuilder orders(List<Order> orders);
}