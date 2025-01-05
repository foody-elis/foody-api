package com.example.foody.repository.customized;

import com.example.foody.model.Order;

import java.util.List;
import java.util.Optional;

public interface CustomizedOrderRepository {
    List<Order> findAllByDeletedAtIsNull();
    Optional<Order> findByIdAndDeletedAtIsNull(long id);
    List<Order> findAllByDeletedAtIsNullAndBuyer_IdOrderByCreatedAtDesc(long buyerId);
    List<Order> findAllByDeletedAtIsNullAndRestaurant_IdOrderByCreatedAtDesc(long restaurantId);
}