package com.example.foody.state.booking;

import com.example.foody.model.Booking;

public class DeletedState extends BookingState {
    public DeletedState(Booking booking) {
        super(booking, BookingStatus.DELETED.name());
    }

    @Override
    public void activate() {
        getBooking().setState(new ActiveState(getBooking()));
    }

    @Override
    public void delete() {
        throw new IllegalStateException("Booking is already deleted");
    }
}
