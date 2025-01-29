package com.example.foody.mapper;

import com.example.foody.dto.request.BookingRequestDTO;
import com.example.foody.dto.response.BookingResponseDTO;
import com.example.foody.model.Booking;

import java.util.List;

/**
 * Mapper interface for converting between Booking entities and DTOs.
 */
public interface BookingMapper {

    /**
     * Converts a Booking entity to a BookingResponseDTO.
     *
     * @param booking the Booking entity to convert
     * @return the converted BookingResponseDTO
     */
    BookingResponseDTO bookingToBookingResponseDTO(Booking booking);

    /**
     * Converts a BookingRequestDTO to a Booking entity.
     *
     * @param bookingRequestDTO the BookingRequestDTO to convert
     * @return the converted Booking entity
     */
    Booking bookingRequestDTOToBooking(BookingRequestDTO bookingRequestDTO);

    /**
     * Converts a list of Booking entities to a list of BookingResponseDTOs.
     *
     * @param bookings the list of Booking entities to convert
     * @return the list of converted BookingResponseDTOs
     */
    List<BookingResponseDTO> bookingsToBookingResponseDTOs(List<Booking> bookings);
}