package com.example.foody.state.booking;

import com.example.foody.model.Booking;
import com.example.foody.utils.enums.BookingStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class BookingState {
    private BookingStatus status;

    public BookingState(BookingStatus status) {
        this.status = status;
    }

    public abstract void activate(Booking booking);
    public abstract void cancel(Booking booking);
}