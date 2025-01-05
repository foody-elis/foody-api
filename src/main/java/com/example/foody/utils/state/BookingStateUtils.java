package com.example.foody.utils.state;

import com.example.foody.state.booking.BookingState;
import com.example.foody.state.booking.impl.ActiveState;
import com.example.foody.state.booking.impl.CancelledState;
import com.example.foody.utils.enums.BookingStatus;

public class BookingStateUtils {
    public static BookingState getState(BookingStatus status) {
        return switch (status) {
            case ACTIVE -> new ActiveState();
            case CANCELLED -> new CancelledState();
        };
    }
}