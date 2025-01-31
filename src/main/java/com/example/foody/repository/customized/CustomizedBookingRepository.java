package com.example.foody.repository.customized;

import com.example.foody.model.Booking;

import java.util.Optional;

/**
 * Customized repository interface for managing {@link Booking} entities.
 * <p>
 * Provides custom query methods for specific booking-related operations.
 */
public interface CustomizedBookingRepository {

    /**
     * Finds a booking by its ID.
     *
     * @param id the ID of the booking
     * @return an {@link Optional} containing the booking if found, or empty if not found
     */
    Optional<Booking> findById(long id);
}