package com.example.foody.state.booking;

import com.example.foody.model.Booking;
import com.example.foody.utils.enums.BookingStatus;
import lombok.Getter;
import lombok.Setter;

/**
 * Abstract class representing the state of a booking.
 * <p>
 * Provides methods to activate and cancel a booking.
 */
@Getter
@Setter
public abstract class BookingState {

    private BookingStatus status;

    /**
     * Constructor for BookingState.
     *
     * @param status the status of the booking
     */
    public BookingState(BookingStatus status) {
        this.status = status;
    }

    /**
     * Activates the booking.
     *
     * @param booking the booking to be activated
     */
    public abstract void activate(Booking booking);

    /**
     * Cancels the booking.
     *
     * @param booking the booking to be canceled
     */
    public abstract void cancel(Booking booking);
}