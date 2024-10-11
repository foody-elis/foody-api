package com.example.foody.state.booking;

import com.example.foody.model.Booking;

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
