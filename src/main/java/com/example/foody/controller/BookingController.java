package com.example.foody.controller;

import com.example.foody.dto.request.BookingRequestDTO;
import com.example.foody.dto.response.BookingResponseDTO;
import com.example.foody.exceptions.booking.BookingNotAllowedException;
import com.example.foody.exceptions.booking.ForbiddenBookingAccessException;
import com.example.foody.exceptions.entity.EntityCreationException;
import com.example.foody.exceptions.entity.EntityDeletionException;
import com.example.foody.exceptions.entity.EntityEditException;
import com.example.foody.exceptions.entity.EntityNotFoundException;
import com.example.foody.exceptions.restaurant.ForbiddenRestaurantAccessException;
import com.example.foody.model.user.CustomerUser;
import com.example.foody.service.BookingService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for handling booking-related requests.
 */
@RestController
@RequestMapping("/api/v1/bookings")
@AllArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    /**
     * Saves a new booking.
     *
     * @param bookingRequestDTO the booking request data transfer object
     * @return the response entity containing the booking response data transfer object
     * @throws EntityNotFoundException    if the entity is not found
     * @throws BookingNotAllowedException if the booking is not allowed
     * @throws EntityCreationException    if there is an error creating the entity
     */
    @PostMapping
    public ResponseEntity<BookingResponseDTO> saveBooking(@Valid @RequestBody BookingRequestDTO bookingRequestDTO)
            throws EntityNotFoundException, BookingNotAllowedException, EntityCreationException {
        BookingResponseDTO responseDTO = bookingService.save(bookingRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    /**
     * Retrieves all bookings.
     *
     * @return the response entity containing the list of booking response data transfer objects
     */
    @GetMapping
    public ResponseEntity<List<BookingResponseDTO>> getBookings() {
        List<BookingResponseDTO> responseDTOs = bookingService.findAll();
        return ResponseEntity.ok(responseDTOs);
    }

    /**
     * Retrieves a booking by its ID.
     *
     * @param id the booking ID
     * @return the response entity containing the booking response data transfer object
     * @throws EntityNotFoundException         if the entity is not found
     * @throws ForbiddenBookingAccessException if access to the booking is forbidden
     */
    @GetMapping(path = "/{id}")
    public ResponseEntity<BookingResponseDTO> getBookingById(@PathVariable long id)
            throws EntityNotFoundException, ForbiddenBookingAccessException {
        BookingResponseDTO responseDTO = bookingService.findById(id);
        return ResponseEntity.ok(responseDTO);
    }

    /**
     * Retrieves all bookings for a specific customer.
     *
     * @param customer the authenticated customer user
     * @return the response entity containing the list of booking response data transfer objects
     * @throws EntityNotFoundException if the entity is not found
     */
    @GetMapping(path = "/customer")
    public ResponseEntity<List<BookingResponseDTO>> getBookingsByCustomer(
            @AuthenticationPrincipal CustomerUser customer
    ) throws EntityNotFoundException {
        List<BookingResponseDTO> responseDTOs = bookingService.findAllByCustomer(customer.getId());
        return ResponseEntity.ok(responseDTOs);
    }

    /**
     * Retrieves all bookings for a specific restaurant.
     *
     * @param restaurantId the restaurant ID
     * @return the response entity containing the list of booking response data transfer objects
     * @throws EntityNotFoundException            if the entity is not found
     * @throws ForbiddenRestaurantAccessException if access to the restaurant is forbidden
     */
    @GetMapping(path = "/restaurant/{restaurant-id}")
    public ResponseEntity<List<BookingResponseDTO>> getBookingsByRestaurant(
            @PathVariable("restaurant-id") long restaurantId
    ) throws EntityNotFoundException, ForbiddenRestaurantAccessException {
        List<BookingResponseDTO> responseDTOs = bookingService.findAllByRestaurant(restaurantId);
        return ResponseEntity.ok(responseDTOs);
    }

    /**
     * Cancels a booking by its ID.
     *
     * @param id the booking ID
     * @return the response entity containing the updated booking response data transfer object
     * @throws EntityNotFoundException         if the entity is not found
     * @throws ForbiddenBookingAccessException if access to the booking is forbidden
     * @throws IllegalStateException           if the booking cannot be canceled
     * @throws EntityEditException             if there is an error editing the entity
     */
    @PatchMapping(path = "/cancel/{id}")
    public ResponseEntity<BookingResponseDTO> cancelBooking(@PathVariable long id)
            throws EntityNotFoundException, ForbiddenBookingAccessException, IllegalStateException, EntityEditException {
        BookingResponseDTO responseDTO = bookingService.cancelById(id);
        return ResponseEntity.ok(responseDTO);
    }

    /**
     * Removes a booking by its ID.
     *
     * @param id the booking ID
     * @return the response entity with no content
     * @throws EntityNotFoundException if the entity is not found
     * @throws EntityDeletionException if there is an error deleting the entity
     */
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> removeBooking(@PathVariable long id)
            throws EntityNotFoundException, EntityDeletionException {
        bookingService.remove(id);
        return ResponseEntity.ok().build();
    }
}