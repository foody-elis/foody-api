package com.example.foody.state.booking;

import com.example.foody.model.Booking;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class BookingState {
    private Booking booking;

    public BookingState(Booking booking) {
        this.booking = booking;
    }

    public abstract void activate();
    public abstract void delete();
}
