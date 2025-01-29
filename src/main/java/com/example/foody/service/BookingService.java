package com.example.foody.service;

import com.example.foody.dto.request.BookingRequestDTO;
import com.example.foody.dto.response.BookingResponseDTO;

import java.util.List;

public interface BookingService {
    BookingResponseDTO save(BookingRequestDTO bookingDTO);

    List<BookingResponseDTO> findAll();

    BookingResponseDTO findById(long id);

    List<BookingResponseDTO> findAllByCustomer(long customerId);

    List<BookingResponseDTO> findAllByRestaurant(long restaurantId);

    BookingResponseDTO cancelById(long id);

    boolean remove(long id);
}
