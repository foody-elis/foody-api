package com.example.foody.controller;

import com.example.foody.dto.request.OrderRequestDTO;
import com.example.foody.dto.response.OrderResponseDTO;
import com.example.foody.exceptions.entity.EntityCreationException;
import com.example.foody.exceptions.entity.EntityDeletionException;
import com.example.foody.exceptions.entity.EntityEditException;
import com.example.foody.exceptions.entity.EntityNotFoundException;
import com.example.foody.exceptions.order.ForbiddenOrderAccessException;
import com.example.foody.exceptions.order.InvalidOrderStateException;
import com.example.foody.exceptions.order.OrderNotAllowedException;
import com.example.foody.exceptions.restaurant.ForbiddenRestaurantAccessException;
import com.example.foody.model.user.User;
import com.example.foody.service.OrderService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for handling order-related requests.
 */
@RestController
@RequestMapping("/api/v1/orders")
@AllArgsConstructor
public class OrderController {

    private final OrderService orderService;

    /**
     * Saves a new order.
     *
     * @param orderRequestDTO the order request data transfer object
     * @return the response entity containing the order response data transfer object
     * @throws EntityNotFoundException       if the entity is not found
     * @throws ForbiddenOrderAccessException if access to the order is forbidden
     * @throws OrderNotAllowedException      if the order is not allowed
     * @throws EntityCreationException       if there is an error creating the entity
     */
    @PostMapping
    public ResponseEntity<OrderResponseDTO> saveOrder(@Valid @RequestBody OrderRequestDTO orderRequestDTO)
            throws EntityNotFoundException, ForbiddenOrderAccessException, OrderNotAllowedException, EntityCreationException {
        OrderResponseDTO orderResponseDTO = orderService.save(orderRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(orderResponseDTO);
    }

    /**
     * Retrieves all orders.
     *
     * @return the response entity containing the list of order response data transfer objects
     */
    @GetMapping
    public ResponseEntity<List<OrderResponseDTO>> getOrders() {
        List<OrderResponseDTO> orders = orderService.findAll();
        return ResponseEntity.ok(orders);
    }

    /**
     * Retrieves an order by its ID.
     *
     * @param id the order ID
     * @return the response entity containing the order response data transfer object
     * @throws EntityNotFoundException       if the entity is not found
     * @throws ForbiddenOrderAccessException if access to the order is forbidden
     */
    @GetMapping(path = "/{id}")
    public ResponseEntity<OrderResponseDTO> getOrderById(@PathVariable long id)
            throws EntityNotFoundException, ForbiddenOrderAccessException {
        OrderResponseDTO orderResponseDTO = orderService.findById(id);
        return ResponseEntity.ok(orderResponseDTO);
    }

    /**
     * Retrieves all orders for a specific buyer.
     *
     * @param buyer the authenticated buyer
     * @return the response entity containing the list of order response data transfer objects
     * @throws EntityNotFoundException if the entity is not found
     */
    @GetMapping(path = "/buyer")
    public ResponseEntity<List<OrderResponseDTO>> getOrdersByBuyer(@AuthenticationPrincipal User buyer)
            throws EntityNotFoundException {
        List<OrderResponseDTO> orders = orderService.findAllByBuyer(buyer.getId());
        return ResponseEntity.ok(orders);
    }

    /**
     * Retrieves all orders for a specific restaurant.
     *
     * @param restaurantId the restaurant ID
     * @return the response entity containing the list of order response data transfer objects
     * @throws EntityNotFoundException            if the entity is not found
     * @throws ForbiddenRestaurantAccessException if access to the restaurant is forbidden
     */
    @GetMapping(path = "/restaurant/{restaurant-id}")
    public ResponseEntity<List<OrderResponseDTO>> getOrdersByRestaurant(
            @PathVariable("restaurant-id") long restaurantId
    ) throws EntityNotFoundException, ForbiddenRestaurantAccessException {
        List<OrderResponseDTO> orders = orderService.findAllByRestaurant(restaurantId);
        return ResponseEntity.ok(orders);
    }

    /**
     * Retrieves all in-progress orders for a specific restaurant.
     *
     * @param restaurantId the restaurant ID
     * @return the response entity containing the list of in-progress order response data transfer objects
     * @throws EntityNotFoundException            if the entity is not found
     * @throws ForbiddenRestaurantAccessException if access to the restaurant is forbidden
     */
    @GetMapping(path = "/restaurant/{restaurant-id}/in-progress")
    public ResponseEntity<List<OrderResponseDTO>> getOrdersByRestaurantAndInProgress(
            @PathVariable("restaurant-id") long restaurantId
    ) throws EntityNotFoundException, ForbiddenRestaurantAccessException {
        List<OrderResponseDTO> orders = orderService.findAllByRestaurantAndInProgress(restaurantId);
        return ResponseEntity.ok(orders);
    }

    /**
     * Pays for an order.
     *
     * @param id the order ID
     * @return the response entity containing the updated order response data transfer object
     * @throws EntityNotFoundException       if the entity is not found
     * @throws ForbiddenOrderAccessException if access to the order is forbidden
     * @throws InvalidOrderStateException    if the order state is invalid
     * @throws EntityEditException           if there is an error editing the entity
     */
    @PatchMapping(path = "/pay/{id}")
    public ResponseEntity<OrderResponseDTO> payOrder(@PathVariable long id)
            throws EntityNotFoundException, ForbiddenOrderAccessException, InvalidOrderStateException, EntityEditException {
        OrderResponseDTO orderResponseDTO = orderService.payById(id);
        return ResponseEntity.ok(orderResponseDTO);
    }

    /**
     * Prepares an order.
     *
     * @param id the order ID
     * @return the response entity containing the updated order response data transfer object
     * @throws EntityNotFoundException            if the entity is not found
     * @throws ForbiddenRestaurantAccessException if access to the restaurant is forbidden
     * @throws ForbiddenOrderAccessException      if access to the order is forbidden
     * @throws InvalidOrderStateException         if the order state is invalid
     * @throws EntityEditException                if there is an error editing the entity
     */
    @PatchMapping(path = "/prepare/{id}")
    public ResponseEntity<OrderResponseDTO> prepareOrder(@PathVariable long id)
            throws EntityNotFoundException, ForbiddenRestaurantAccessException, ForbiddenOrderAccessException,
            InvalidOrderStateException, EntityEditException {
        OrderResponseDTO orderResponseDTO = orderService.prepareById(id);
        return ResponseEntity.ok(orderResponseDTO);
    }

    /**
     * Completes an order.
     *
     * @param id the order ID
     * @return the response entity containing the updated order response data transfer object
     * @throws EntityNotFoundException            if the entity is not found
     * @throws ForbiddenRestaurantAccessException if access to the restaurant is forbidden
     * @throws ForbiddenOrderAccessException      if access to the order is forbidden
     * @throws InvalidOrderStateException         if the order state is invalid
     * @throws EntityEditException                if there is an error editing the entity
     */
    @PatchMapping(path = "/complete/{id}")
    public ResponseEntity<OrderResponseDTO> completeOrder(@PathVariable long id)
            throws EntityNotFoundException, ForbiddenRestaurantAccessException, ForbiddenOrderAccessException,
            InvalidOrderStateException, EntityEditException {
        OrderResponseDTO orderResponseDTO = orderService.completeById(id);
        return ResponseEntity.ok(orderResponseDTO);
    }

    /**
     * Removes an order.
     *
     * @param id the order ID
     * @return the response entity with no content
     * @throws EntityNotFoundException if the entity is not found
     * @throws EntityDeletionException if there is an error deleting the entity
     */
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> removeOrder(@PathVariable long id) throws EntityNotFoundException, EntityDeletionException {
        orderService.remove(id);
        return ResponseEntity.ok().build();
    }
}