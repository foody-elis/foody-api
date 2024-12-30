package com.example.foody.state.booking.impl;

import com.example.foody.model.Booking;
import com.example.foody.state.booking.BookingState;
import com.example.foody.utils.enums.BookingStatus;

public class ActiveState extends BookingState {
    public ActiveState() {
        super(BookingStatus.ACTIVE);
    }

    @Override
    public void activate(Booking booking) {
        throw new IllegalStateException("Booking is already active.");
    }

    @Override
    public void cancel(Booking booking) {
        booking.setState(new CancelledState());
    }
}