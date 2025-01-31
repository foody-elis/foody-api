package com.example.foody.utils.state;

import com.example.foody.state.booking.BookingState;
import com.example.foody.state.booking.impl.ActiveState;
import com.example.foody.state.booking.impl.CancelledState;
import com.example.foody.utils.enums.BookingStatus;

/**
 * Utility class for handling booking states.
 */
public class BookingStateUtils {

    /**
     * Returns the appropriate BookingState instance based on the given BookingStatus.
     *
     * @param status the booking status
     * @return the corresponding BookingState instance
     */
    public static BookingState getState(BookingStatus status) {
        return switch (status) {
            case ACTIVE -> new ActiveState();
            case CANCELLED -> new CancelledState();
        };
    }
}