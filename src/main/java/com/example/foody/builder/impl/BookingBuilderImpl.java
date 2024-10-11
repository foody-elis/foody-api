package com.example.foody.builder.impl;

import com.example.foody.builder.BookingBuilder;
import com.example.foody.model.Booking;
import com.example.foody.model.Restaurant;
import com.example.foody.model.SittingTime;
import com.example.foody.model.User;
import com.example.foody.state.booking.BookingState;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class BookingBuilderImpl implements BookingBuilder {
    private long id;
    private LocalDate date;
    private int seats;
    private SittingTime sittingTime;
    private User user;
    private Restaurant restaurant;
    private BookingState state;

    @Override
    public BookingBuilder id(long id) {
        this.id = id;
        return this;
    }

    @Override
    public BookingBuilder date(LocalDate date) {
        this.date = date;
        return this;
    }

    @Override
    public BookingBuilder seats(int seats) {
        this.seats = seats;
        return this;
    }

    @Override
    public BookingBuilder sittingTime(SittingTime sittingTime) {
        this.sittingTime = sittingTime;
        return this;
    }

    @Override
    public BookingBuilder user(User user) {
        this.user = user;
        return this;
    }

    @Override
    public BookingBuilder restaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
        return this;
    }

    @Override
    public BookingBuilder state(BookingState state) {
        this.state = state;
        return this;
    }

    @Override
    public Booking build() {
        return new Booking(id, date, seats, sittingTime, user, restaurant, state);
    }
}
