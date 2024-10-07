package com.example.foody.state.booking;

import com.example.foody.model.Booking;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class BookingState {
    private Booking booking;
    private String name;

    public BookingState(Booking booking, String name) {
        this.booking = booking;
        this.name = name;
    }

    public abstract void activate();
    public abstract void delete();
}
