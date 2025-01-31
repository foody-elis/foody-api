package com.example.foody.repository.customized;

import com.example.foody.model.Booking;
import com.example.foody.utils.state.BookingStateUtils;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;

import java.util.Optional;

/**
 * Implementation of the {@link CustomizedBookingRepository} interface.
 * <p>
 * Provides custom query methods for specific booking-related operations.
 */
@AllArgsConstructor
public class CustomizedBookingRepositoryImpl implements CustomizedBookingRepository {

    private final EntityManager entityManager;

    /**
     * {@inheritDoc}
     * <p>
     * Also sets the booking state if it is not already set.
     *
     * @param id the ID of the booking
     * @return an {@link Optional} containing the booking if found, or empty if not found
     */
    @Override
    public Optional<Booking> findById(long id) {
        return entityManager
                .createQuery("SELECT b FROM Booking b WHERE b.id = :id", Booking.class)
                .setParameter("id", id)
                .getResultList()
                .stream()
                .findFirst()
                .map(b -> {
                    setBookingState(b);
                    return b;
                });
    }

    /**
     * Sets the state of the booking based on its status.
     *
     * @param booking the booking entity
     */
    private void setBookingState(Booking booking) {
        if (booking.getState() == null && booking.getStatus() != null) {
            booking.setState(BookingStateUtils.getState(booking.getStatus()));
        }
    }
}