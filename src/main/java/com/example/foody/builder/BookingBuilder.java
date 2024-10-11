package com.example.foody.builder;

import com.example.foody.model.Booking;
import com.example.foody.model.Restaurant;
import com.example.foody.model.SittingTime;
import com.example.foody.model.User;
import com.example.foody.state.booking.BookingState;

import java.time.LocalDate;

public interface BookingBuilder {
    BookingBuilder id(long id);
    BookingBuilder date(LocalDate date);
    BookingBuilder seats(int seats);
    BookingBuilder sittingTime(SittingTime sittingTime);
    BookingBuilder user(User user);
    BookingBuilder restaurant(Restaurant restaurant);
    BookingBuilder state(BookingState state);
    Booking build();
}
