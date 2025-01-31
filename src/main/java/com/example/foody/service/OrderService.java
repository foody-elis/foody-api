package com.example.foody.service;

import com.example.foody.dto.request.OrderRequestDTO;
import com.example.foody.dto.response.OrderResponseDTO;

import java.util.List;

/**
 * Service interface for managing orders.
 */
public interface OrderService {

    /**
     * Saves a new order.
     *
     * @param orderDTO the order data transfer object containing order details
     * @return the saved order response data transfer object
     */
    OrderResponseDTO save(OrderRequestDTO orderDTO);

    /**
     * Retrieves all orders.
     *
     * @return a list of all order response data transfer objects
     */
    List<OrderResponseDTO> findAll();

    /**
     * Finds an order by its ID.
     *
     * @param id the ID of the order to find
     * @return the found order response data transfer object
     */
    OrderResponseDTO findById(long id);

    /**
     * Finds all orders by buyer ID.
     *
     * @param buyerId the ID of the buyer
     * @return a list of order response data transfer objects for the specified buyer
     */
    List<OrderResponseDTO> findAllByBuyer(long buyerId);

    /**
     * Finds all orders by restaurant ID.
     *
     * @param restaurantId the ID of the restaurant
     * @return a list of order response data transfer objects for the specified restaurant
     */
    List<OrderResponseDTO> findAllByRestaurant(long restaurantId);

    /**
     * Finds all in-progress orders by restaurant ID.
     *
     * @param restaurantId the ID of the restaurant
     * @return a list of in-progress order response data transfer objects for the specified restaurant
     */
    List<OrderResponseDTO> findAllByRestaurantAndInProgress(long restaurantId);

    /**
     * Marks an order as paid by its ID.
     *
     * @param id the ID of the order to mark as paid
     * @return the updated order response data transfer object
     */
    OrderResponseDTO payById(long id);

    /**
     * Marks an order as prepared by its ID.
     *
     * @param id the ID of the order to mark as prepared
     * @return the updated order response data transfer object
     */
    OrderResponseDTO prepareById(long id);

    /**
     * Marks an order as completed by its ID.
     *
     * @param id the ID of the order to mark as completed
     * @return the updated order response data transfer object
     */
    OrderResponseDTO completeById(long id);

    /**
     * Removes an order by its ID.
     *
     * @param id the ID of the order to remove
     * @return true if the order was successfully removed, false otherwise
     */
    boolean remove(long id);
}