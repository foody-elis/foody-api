package com.example.foody.repository;

import com.example.foody.model.Order;
import com.example.foody.repository.customized.CustomizedOrderRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long>, CustomizedOrderRepository {
    List<Order> findAllByDeletedAtIsNull();
    List<Order> findAllByDeletedAtIsNullAndBuyer_IdOrderByCreatedAtDesc(long buyerId);
    List<Order> findAllByDeletedAtIsNullAndRestaurant_IdOrderByCreatedAtDesc(long restaurantId);
}
