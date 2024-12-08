package com.example.foody.mapper.impl;

import com.example.foody.builder.BookingBuilder;
import com.example.foody.dto.request.BookingRequestDTO;
import com.example.foody.dto.response.BookingResponseDTO;
import com.example.foody.dto.response.CustomerUserResponseDTO;
import com.example.foody.mapper.BookingMapper;
import com.example.foody.mapper.RestaurantMapper;
import com.example.foody.mapper.SittingTimeMapper;
import com.example.foody.mapper.UserMapper;
import com.example.foody.model.Booking;
import com.example.foody.model.user.CustomerUser;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class BookingMapperImpl implements BookingMapper {
    private final BookingBuilder bookingBuilder;
    private final SittingTimeMapper sittingTimeMapper;
    private final UserMapper<CustomerUser> userMapper;
    private final RestaurantMapper restaurantMapper;

    public BookingMapperImpl(BookingBuilder bookingBuilder, SittingTimeMapper sittingTimeMapper, UserMapper<CustomerUser> userMapper, RestaurantMapper restaurantMapper) {
        this.bookingBuilder = bookingBuilder;
        this.sittingTimeMapper = sittingTimeMapper;
        this.userMapper = userMapper;
        this.restaurantMapper = restaurantMapper;
    }

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
                (CustomerUserResponseDTO) userMapper.userToUserResponseDTO(booking.getCustomer())
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