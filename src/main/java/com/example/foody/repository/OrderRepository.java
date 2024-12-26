package com.example.foody.repository;

import com.example.foody.model.Order;
import com.example.foody.repository.customized.CustomizedOrderRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long>, CustomizedOrderRepository {
    List<Order> findAllByDeletedAtIsNull();

    List<Order> findAllByDeletedAtIsNullAndBuyer_IdOrderByCreatedAtDesc(long buyerId);

    List<Order> findAllByDeletedAtIsNullAndRestaurant_IdOrderByCreatedAtDesc(long restaurantId);

    @Query("""
            SELECT COUNT(o) > 0
            FROM Order o
            JOIN o.orderDishes od
            WHERE o.buyer.id = :buyerId
            AND od.dish.id = :dishId
            """)
    boolean existsByBuyer_IdAndDish_Id(long buyerId, long dishId);

    @Query("""
            SELECT COALESCE(SUM(od.quantity * od.dish.price), 0)
            FROM Order o
            JOIN o.orderDishes od
            WHERE o.id = :orderId
            """)
    double findAmountByOrder_Id(long orderId);
}