package com.example.foody.repository.customized;

import com.example.foody.model.Booking;

import java.util.Optional;

public interface CustomizedBookingRepository {
    Optional<Booking> findById(long id);
}