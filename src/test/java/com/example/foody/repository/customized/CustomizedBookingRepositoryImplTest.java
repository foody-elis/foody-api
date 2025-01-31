package com.example.foody.repository.customized;

import com.example.foody.model.Booking;
import com.example.foody.utils.enums.BookingStatus;
import com.example.foody.utils.state.BookingStateUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link CustomizedBookingRepositoryImpl} class using mock services.
 */
public class CustomizedBookingRepositoryImplTest {

    private EntityManager entityManager;
    private CustomizedBookingRepositoryImpl repository;

    @BeforeEach
    void setUp() {
        entityManager = mock(EntityManager.class);
        repository = new CustomizedBookingRepositoryImpl(entityManager);
    }

    @Test
    void findByIdWhenBookingExistsReturnsBooking() {
        // Arrange
        long bookingId = 1L;
        Booking booking = mock(Booking.class);
        TypedQuery<Booking> query = mock(TypedQuery.class);

        when(entityManager.createQuery("SELECT b FROM Booking b WHERE b.id = :id", Booking.class)).thenReturn(query);
        when(query.setParameter("id", bookingId)).thenReturn(query);
        when(query.getResultList()).thenReturn(List.of(booking));
        when(booking.getState()).thenReturn(null);
        when(booking.getStatus()).thenReturn(BookingStatus.ACTIVE);
        doNothing().when(booking).setState(any());

        // Act
        Optional<Booking> result = repository.findById(bookingId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(booking, result.get());
        verify(booking, times(1)).getState();
        verify(booking, times(1)).setState(any());
    }

    @Test
    void findByIdWhenBookingDoesNotExistReturnsEmpty() {
        // Arrange
        long bookingId = 1L;
        TypedQuery<Booking> query = mock(TypedQuery.class);

        when(entityManager.createQuery("SELECT b FROM Booking b WHERE b.id = :id", Booking.class)).thenReturn(query);
        when(query.setParameter("id", bookingId)).thenReturn(query);
        when(query.getResultList()).thenReturn(List.of());

        // Act
        Optional<Booking> result = repository.findById(bookingId);

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    void setBookingStateWhenStateIsNullAndStatusIsNotNullSetsState() {
        // Arrange
        long bookingId = 1L;
        Booking booking = mock(Booking.class);
        TypedQuery<Booking> query = mock(TypedQuery.class);

        // Configure the EntityManager mock to return a TypedQuery mock
        when(entityManager.createQuery("SELECT b FROM Booking b WHERE b.id = :id", Booking.class)).thenReturn(query);
        when(query.setParameter("id", bookingId)).thenReturn(query);
        when(query.getResultList()).thenReturn(List.of(booking));

        when(booking.getState()).thenReturn(null);
        when(booking.getStatus()).thenReturn(BookingStatus.ACTIVE);

        // Act
        Optional<Booking> result = repository.findById(bookingId);

        // Assert
        assertTrue(result.isPresent());
        verify(booking, times(1)).getState();
        verify(booking, times(1)).setState(any());
    }

    @Test
    void setBookingStateWhenStateIsNotNullDoesNothing() {
        // Arrange
        long bookingId = 1L;
        Booking booking = mock(Booking.class);
        TypedQuery<Booking> query = mock(TypedQuery.class);

        // Configure the EntityManager mock to return a TypedQuery mock
        when(entityManager.createQuery("SELECT b FROM Booking b WHERE b.id = :id", Booking.class)).thenReturn(query);
        when(query.setParameter("id", bookingId)).thenReturn(query);
        when(query.getResultList()).thenReturn(List.of(booking));

        when(booking.getState()).thenReturn(BookingStateUtils.getState(BookingStatus.ACTIVE));

        // Act
        Optional<Booking> result = repository.findById(bookingId);

        // Assert
        assertTrue(result.isPresent());
        verify(booking, times(1)).getState();
        verify(booking, never()).getStatus();
        verify(booking, never()).setState(any());
    }
}