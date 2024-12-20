package com.example.foody.state.booking.impl;

import com.example.foody.model.Booking;
import com.example.foody.state.booking.BookingState;
import com.example.foody.state.booking.BookingStatus;

public class CancelledState extends BookingState {
    public CancelledState(Booking booking) {
        super(booking, BookingStatus.CANCELLED.name());
    }

    @Override
    public void activate() {
        getBooking().setState(new ActiveState(getBooking()));
    }

    @Override
    public void cancel() {
        throw new IllegalStateException("Booking is already cancelled.");
    }
}
