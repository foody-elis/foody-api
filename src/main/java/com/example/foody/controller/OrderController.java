package com.example.foody.controller;

import com.example.foody.dto.request.OrderRequestDTO;
import com.example.foody.dto.response.OrderResponseDTO;
import com.example.foody.exceptions.entity.EntityCreationException;
import com.example.foody.exceptions.entity.EntityDeletionException;
import com.example.foody.exceptions.entity.EntityEditException;
import com.example.foody.exceptions.entity.EntityNotFoundException;
import com.example.foody.exceptions.order.ForbiddenOrderAccessException;
import com.example.foody.exceptions.order.InvalidOrderStateException;
import com.example.foody.model.user.CustomerUser;
import com.example.foody.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderResponseDTO> saveOrder(@Valid @RequestBody OrderRequestDTO orderRequestDTO)
            throws EntityNotFoundException, EntityCreationException {
        return new ResponseEntity<>(orderService.save(orderRequestDTO), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<OrderResponseDTO>> getOrders() {
        return new ResponseEntity<>(orderService.findAll(), HttpStatus.OK);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<OrderResponseDTO> getOrderById(@PathVariable long id)
            throws EntityNotFoundException, ForbiddenOrderAccessException {
        return new ResponseEntity<>(orderService.findById(id), HttpStatus.OK);
    }

    @GetMapping(path = "/customer")
    public ResponseEntity<List<OrderResponseDTO>> getOrdersByCustomer(@AuthenticationPrincipal CustomerUser customer)
            throws EntityNotFoundException {
        return new ResponseEntity<>(orderService.findAllByCustomer(customer.getId()), HttpStatus.OK);
    }

    @GetMapping(path = "/restaurant/{restaurant-id}")
    public ResponseEntity<List<OrderResponseDTO>> getOrdersByRestaurant(@PathVariable("restaurant-id") long restaurantId)
            throws EntityNotFoundException, ForbiddenOrderAccessException {
        return new ResponseEntity<>(orderService.findAllByRestaurant(restaurantId), HttpStatus.OK);
    }

    @PatchMapping(path = "/await-payment/{id}")
    public ResponseEntity<OrderResponseDTO> awaitPayment(@PathVariable long id)
            throws EntityNotFoundException, ForbiddenOrderAccessException, InvalidOrderStateException, EntityEditException {
        return new ResponseEntity<>(orderService.awaitPaymentById(id), HttpStatus.OK);
    }

    @PatchMapping(path = "/complete/{id}")
    public ResponseEntity<OrderResponseDTO> completeOrder(@PathVariable long id)
            throws EntityNotFoundException, ForbiddenOrderAccessException, InvalidOrderStateException, EntityEditException {
        return new ResponseEntity<>(orderService.completeById(id), HttpStatus.OK);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> removeOrder(@PathVariable long id)
            throws EntityNotFoundException, EntityDeletionException {
        orderService.remove(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
