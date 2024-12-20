package com.example.foody.state.booking.impl;

import com.example.foody.model.Booking;
import com.example.foody.state.booking.BookingState;
import com.example.foody.state.booking.BookingStatus;

public class ActiveState extends BookingState {
    public ActiveState(Booking booking) {
        super(booking, BookingStatus.ACTIVE.name());
    }

    @Override
    public void activate() {
        throw new IllegalStateException("Booking is already active.");
    }

    @Override
    public void cancel() {
        getBooking().setState(new CancelledState(getBooking()));
    }
}
