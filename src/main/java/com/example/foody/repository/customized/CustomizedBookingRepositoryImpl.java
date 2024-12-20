package com.example.foody.repository.customized;

import com.example.foody.model.Booking;
import com.example.foody.state.booking.impl.ActiveState;
import com.example.foody.state.booking.BookingStatus;
import com.example.foody.state.booking.impl.CancelledState;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.Optional;

public class CustomizedBookingRepositoryImpl implements CustomizedBookingRepository {
    private final EntityManager entityManager;

    public CustomizedBookingRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Optional<Booking> findByIdAndDeletedAtIsNull(long id) {
        TypedQuery<Booking> query = entityManager.createQuery(
                "select b from Booking b where b.id = :id and b.deletedAt is null", Booking.class);
        query.setParameter("id", id);

        Optional<Booking> booking = query.getResultList().stream().findFirst();
        booking.ifPresent(b -> {
            if (b.getState() == null && b.getStatus() != null) {
                switch (b.getStatus()) {
                    case BookingStatus.ACTIVE -> b.setState(new ActiveState(b));
                    case BookingStatus.CANCELLED -> b.setState(new CancelledState(b));
                }
            }
        });

        return booking;
    }
}
