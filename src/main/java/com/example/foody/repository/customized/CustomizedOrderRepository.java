package com.example.foody.repository.customized;

import com.example.foody.model.Order;

import java.util.List;
import java.util.Optional;

/**
 * Customized repository interface for managing {@link Order} entities.
 * <p>
 * Provides custom query methods for specific order-related operations.
 */
public interface CustomizedOrderRepository {

    /**
     * Finds all orders.
     *
     * @return a list of all orders
     */
    List<Order> findAll();

    /**
     * Finds an order by its ID.
     *
     * @param id the ID of the order
     * @return an {@link Optional} containing the order if found, or empty if not found
     */
    Optional<Order> findById(long id);

    /**
     * Finds all orders by buyer ID.
     *
     * @param buyerId the ID of the buyer
     * @return a list of all orders by the buyer
     */
    List<Order> findAllByBuyer_IdOrderByCreatedAtDesc(long buyerId);

    /**
     * Finds all orders by restaurant ID.
     *
     * @param restaurantId the ID of the restaurant
     * @return a list of all orders by the restaurant
     */
    List<Order> findAllByRestaurant_IdOrderByCreatedAtDesc(long restaurantId);

    /**
     * Finds all orders by restaurant ID and status.
     *
     * @param restaurantId the ID of the restaurant
     * @param statuses     the list of statuses
     * @return a list of all orders by the restaurant with the specified statuses
     */
    List<Order> findAllByRestaurant_IdAndStatusInOrderByCreatedAtDesc(long restaurantId, List<String> statuses);
}