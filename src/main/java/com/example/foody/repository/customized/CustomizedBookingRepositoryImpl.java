package com.example.foody.repository.customized;

import com.example.foody.model.Booking;
import com.example.foody.utils.state.BookingStateUtils;
import jakarta.persistence.EntityManager;

import java.util.Optional;

public class CustomizedBookingRepositoryImpl implements CustomizedBookingRepository {
    private final EntityManager entityManager;

    public CustomizedBookingRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Optional<Booking> findByIdAndDeletedAtIsNull(long id) {
        return entityManager
                .createQuery("SELECT b FROM Booking b WHERE b.id = :id AND b.deletedAt IS NULL", Booking.class)
                .setParameter("id", id)
                .getResultList()
                .stream()
                .findFirst()
                .map(b -> {
                    setBookingState(b);
                    return b;
                });
    }

    private void setBookingState(Booking booking) {
        if (booking.getState() == null && booking.getStatus() != null) {
            booking.setState(BookingStateUtils.getState(booking.getStatus()));
        }
    }
}