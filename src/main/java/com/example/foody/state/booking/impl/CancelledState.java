package com.example.foody.state.booking.impl;

import com.example.foody.model.Booking;
import com.example.foody.state.booking.BookingState;
import com.example.foody.utils.enums.BookingStatus;

/**
 * Represents the cancelled state of a booking.
 * <p>
 * Provides methods to handle activation and cancellation of the booking.
 */
public class CancelledState extends BookingState {

    /**
     * Constructor for CancelledState.
     * <p>
     * Sets the booking status to CANCELLED.
     */
    public CancelledState() {
        super(BookingStatus.CANCELLED);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Throws an IllegalStateException as the booking is already cancelled.
     *
     * @param booking the booking to be activated
     */
    @Override
    public void activate(Booking booking) {
        throw new IllegalStateException("Booking is already cancelled.");
    }

    /**
     * {@inheritDoc}
     * <p>
     * Throws an IllegalStateException as the booking is already cancelled.
     *
     * @param booking the booking to be cancelled
     */
    @Override
    public void cancel(Booking booking) {
        throw new IllegalStateException("Booking is already cancelled.");
    }
}