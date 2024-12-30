package com.example.foody.state.booking.impl;

import com.example.foody.model.Booking;
import com.example.foody.state.booking.BookingState;
import com.example.foody.utils.enums.BookingStatus;

public class CancelledState extends BookingState {
    public CancelledState() {
        super(BookingStatus.CANCELLED);
    }

    @Override
    public void activate(Booking booking) {
        throw new IllegalStateException("Booking is already cancelled.");
    }

    @Override
    public void cancel(Booking booking) {
        throw new IllegalStateException("Booking is already cancelled.");
    }
}