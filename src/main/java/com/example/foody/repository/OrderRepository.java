package com.example.foody.repository;

import com.example.foody.model.Order;
import com.example.foody.repository.customized.CustomizedOrderRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Repository interface for managing {@link Order} entities.
 * <p>
 * Extends the {@link JpaRepository} interface to provide CRUD operations for {@link Order} entities.
 * Also includes custom query methods for specific order-related operations.
 */
public interface OrderRepository extends JpaRepository<Order, Long>, CustomizedOrderRepository {

    /**
     * Checks if an order exists for a specific buyer and dish.
     *
     * @param buyerId the ID of the buyer
     * @param dishId the ID of the dish
     * @return true if an order exists for the specified buyer and dish, false otherwise
     */
    @Query("""
            SELECT COUNT(o) > 0
            FROM Order o
            JOIN o.orderDishes od
            WHERE o.buyer.id = :buyerId
            AND od.dish.id = :dishId
            """)
    boolean existsByBuyer_IdAndDish_Id(long buyerId, long dishId);

    /**
     * Finds the total amount for a specific order.
     *
     * @param orderId the ID of the order
     * @return the total amount of the order
     */
    @Query("""
            SELECT COALESCE(SUM(od.quantity * od.dish.price), 0)
            FROM Order o
            JOIN o.orderDishes od
            WHERE o.id = :orderId
            """)
    double findAmountByOrder_Id(long orderId);
}