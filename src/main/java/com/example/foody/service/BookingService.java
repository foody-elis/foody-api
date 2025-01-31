package com.example.foody.service;

import com.example.foody.dto.request.BookingRequestDTO;
import com.example.foody.dto.response.BookingResponseDTO;

import java.util.List;

/**
 * Service interface for managing bookings.
 */
public interface BookingService {

    /**
     * Saves a new booking.
     *
     * @param bookingDTO the booking request data transfer object
     * @return the saved booking response data transfer object
     */
    BookingResponseDTO save(BookingRequestDTO bookingDTO);

    /**
     * Retrieves all bookings.
     *
     * @return the list of booking response data transfer objects
     */
    List<BookingResponseDTO> findAll();

    /**
     * Retrieves a booking by its ID.
     *
     * @param id the ID of the booking to retrieve
     * @return the booking response data transfer object
     */
    BookingResponseDTO findById(long id);

    /**
     * Retrieves all bookings made by a specific customer.
     *
     * @param customerId the ID of the customer
     * @return the list of booking response data transfer objects
     */
    List<BookingResponseDTO> findAllByCustomer(long customerId);

    /**
     * Retrieves all bookings for a specific restaurant.
     *
     * @param restaurantId the ID of the restaurant
     * @return the list of booking response data transfer objects
     */
    List<BookingResponseDTO> findAllByRestaurant(long restaurantId);

    /**
     * Cancels a booking by its ID.
     *
     * @param id the ID of the booking to cancel
     * @return the canceled booking response data transfer object
     */
    BookingResponseDTO cancelById(long id);

    /**
     * Removes a booking by its ID.
     *
     * @param id the ID of the booking to remove
     * @return true if the booking was removed, false otherwise
     */
    boolean remove(long id);
}