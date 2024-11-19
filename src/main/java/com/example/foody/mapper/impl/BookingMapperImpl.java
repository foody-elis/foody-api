package com.example.foody.mapper.impl;

import com.example.foody.builder.BookingBuilder;
import com.example.foody.dto.request.BookingRequestDTO;
import com.example.foody.dto.response.BookingResponseDTO;
import com.example.foody.mapper.BookingMapper;
import com.example.foody.model.Booking;
import com.example.foody.model.Restaurant;
import com.example.foody.model.SittingTime;
import com.example.foody.model.user.CustomerUser;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class BookingMapperImpl implements BookingMapper {
    private final BookingBuilder bookingBuilder;

    public BookingMapperImpl(BookingBuilder bookingBuilder) {
        this.bookingBuilder = bookingBuilder;
    }

    @Override
    public BookingResponseDTO bookingToBookingResponseDTO(Booking booking) {
        if (booking == null) {
            return null;
        }

        BookingResponseDTO bookingResponseDTO = new BookingResponseDTO();

        bookingResponseDTO.setSittingTimeId(bookingSittingTimeId(booking));
        bookingResponseDTO.setCustomerId(bookingCustomerId(booking));
        bookingResponseDTO.setRestaurantId(bookingRestaurantId(booking));
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

    private long bookingSittingTimeId(Booking booking) {
        if (booking == null) {
            return 0L;
        }
        SittingTime sittingTime = booking.getSittingTime();
        if (sittingTime == null) {
            return 0L;
        }
        return sittingTime.getId();
    }

    private long bookingCustomerId(Booking booking) {
        if (booking == null) {
            return 0L;
        }
        CustomerUser customer = booking.getCustomer();
        if (customer == null) {
            return 0L;
        }
        return customer.getId();
    }

    private long bookingRestaurantId(Booking booking) {
        if (booking == null) {
            return 0L;
        }
        Restaurant restaurant = booking.getRestaurant();
        if (restaurant == null) {
            return 0L;
        }
        return restaurant.getId();
    }
}
