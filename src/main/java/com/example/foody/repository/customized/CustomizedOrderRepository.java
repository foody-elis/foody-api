package com.example.foody.repository.customized;

import com.example.foody.model.Order;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CustomizedOrderRepository {
    List<Order> findAll();
    Optional<Order> findById(long id);
    List<Order> findAllByBuyer_IdOrderByCreatedAtDesc(long buyerId);
    List<Order> findAllByRestaurant_IdOrderByCreatedAtDesc(long restaurantId);
    List<Order> findAllByRestaurant_IdAndStatusInAndCreatedAt_DateOrderByCreatedAtDesc(
            long restaurantId, List<String> statuses, LocalDate date);
}