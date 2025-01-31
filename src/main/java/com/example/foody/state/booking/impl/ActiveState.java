package com.example.foody.state.booking.impl;

import com.example.foody.model.Booking;
import com.example.foody.state.booking.BookingState;
import com.example.foody.utils.enums.BookingStatus;

/**
 * Represents the active state of a booking.
 * <p>
 * Provides methods to handle activation and cancellation of the booking.
 */
public class ActiveState extends BookingState {

    /**
     * Constructor for ActiveState.
     * <p>
     * Sets the booking status to ACTIVE.
     */
    public ActiveState() {
        super(BookingStatus.ACTIVE);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Throws an IllegalStateException as the booking is already active.
     *
     * @param booking the booking to be activated
     */
    @Override
    public void activate(Booking booking) {
        throw new IllegalStateException("Booking is already active.");
    }

    /**
     * {@inheritDoc}
     * <p>
     * Changes the state of the booking to CancelledState.
     *
     * @param booking the booking to be canceled
     */
    @Override
    public void cancel(Booking booking) {
        booking.setState(new CancelledState());
    }
}