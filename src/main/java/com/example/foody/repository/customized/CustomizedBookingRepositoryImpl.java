package com.example.foody.repository.customized;

import com.example.foody.model.Booking;
import com.example.foody.state.booking.BookingStatus;
import com.example.foody.state.booking.impl.ActiveState;
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
        booking.ifPresent(this::setBookingState);

        return booking;
    }

    private void setBookingState(Booking booking) {
        if (booking.getState() == null && booking.getStatus() != null) {
            switch (booking.getStatus()) {
                case BookingStatus.ACTIVE -> booking.setState(new ActiveState(booking));
                case BookingStatus.CANCELLED -> booking.setState(new CancelledState(booking));
            }
        }
    }
}