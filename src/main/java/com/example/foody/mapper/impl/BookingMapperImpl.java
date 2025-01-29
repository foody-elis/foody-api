package com.example.foody.mapper.impl;

import com.example.foody.builder.BookingBuilder;
import com.example.foody.dto.request.BookingRequestDTO;
import com.example.foody.dto.response.BookingResponseDTO;
import com.example.foody.dto.response.CustomerUserResponseDTO;
import com.example.foody.helper.UserHelper;
import com.example.foody.mapper.BookingMapper;
import com.example.foody.mapper.RestaurantMapper;
import com.example.foody.mapper.SittingTimeMapper;
import com.example.foody.model.Booking;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the {@link BookingMapper} interface.
 * <p>
 * Provides methods to convert between {@link Booking} entities and DTOs.
 */
@Component
@AllArgsConstructor
public class BookingMapperImpl implements BookingMapper {

    private final BookingBuilder bookingBuilder;
    private final SittingTimeMapper sittingTimeMapper;
    private final UserHelper userHelper;
    private final RestaurantMapper restaurantMapper;

    /**
     * {@inheritDoc}
     * <p>
     * Converts a {@link Booking} entity to a {@link BookingResponseDTO}.
     *
     * @param booking the Booking entity to convert
     * @return the converted BookingResponseDTO
     */
    @Override
    public BookingResponseDTO bookingToBookingResponseDTO(Booking booking) {
        if (booking == null) {
            return null;
        }

        BookingResponseDTO bookingResponseDTO = new BookingResponseDTO();

        bookingResponseDTO.setSittingTime(
                sittingTimeMapper.sittingTimeToSittingTimeResponseDTO(booking.getSittingTime())
        );
        bookingResponseDTO.setCustomer(
                (CustomerUserResponseDTO) userHelper.buildUserResponseDTO(booking.getCustomer())
        );
        bookingResponseDTO.setRestaurant(
                restaurantMapper.restaurantToRestaurantResponseDTO(booking.getRestaurant())
        );
        bookingResponseDTO.setId(booking.getId());
        bookingResponseDTO.setDate(booking.getDate());
        bookingResponseDTO.setSeats(booking.getSeats());
        if (booking.getStatus() != null) {
            bookingResponseDTO.setStatus(booking.getStatus().name());
        }

        return bookingResponseDTO;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Converts a {@link BookingRequestDTO} to a {@link Booking} entity.
     *
     * @param bookingRequestDTO the BookingRequestDTO to convert
     * @return the converted Booking entity
     */
    @Override
    public Booking bookingRequestDTOToBooking(BookingRequestDTO bookingRequestDTO) {
        if (bookingRequestDTO == null) {
            return null;
        }

        return bookingBuilder
                .date(bookingRequestDTO.getDate())
                .seats(bookingRequestDTO.getSeats())
                .build();
    }

    /**
     * {@inheritDoc}
     * <p>
     * Converts a list of {@link Booking} entities to a list of {@link BookingResponseDTO} objects.
     *
     * @param bookings the list of Booking entities to convert
     * @return the list of converted BookingResponseDTO objects
     */
    @Override
    public List<BookingResponseDTO> bookingsToBookingResponseDTOs(List<Booking> bookings) {
        if (bookings == null) {
            return null;
        }

        List<BookingResponseDTO> list = new ArrayList<>(bookings.size());
        bookings.forEach(booking -> list.add(bookingToBookingResponseDTO(booking)));

        return list;
    }
}