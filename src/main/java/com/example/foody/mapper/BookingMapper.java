package com.example.foody.mapper;

import com.example.foody.dto.request.BookingRequestDTO;
import com.example.foody.dto.response.BookingResponseDTO;
import com.example.foody.model.Booking;

import java.util.List;

public interface BookingMapper {
    BookingResponseDTO bookingToBookingResponseDTO(Booking booking);
    Booking bookingRequestDTOToBooking(BookingRequestDTO bookingRequestDTO);
    List<BookingResponseDTO> bookingsToBookingResponseDTOs(List<Booking> bookings);
}
