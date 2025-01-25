package com.example.foody.mapper.impl;

import com.example.foody.TestDataUtil;
import com.example.foody.builder.BookingBuilder;
import com.example.foody.dto.request.BookingRequestDTO;
import com.example.foody.dto.response.BookingResponseDTO;
import com.example.foody.dto.response.CustomerUserResponseDTO;
import com.example.foody.dto.response.RestaurantResponseDTO;
import com.example.foody.dto.response.SittingTimeResponseDTO;
import com.example.foody.helper.UserHelper;
import com.example.foody.mapper.RestaurantMapper;
import com.example.foody.mapper.SittingTimeMapper;
import com.example.foody.model.Booking;
import com.example.foody.model.Restaurant;
import com.example.foody.model.SittingTime;
import com.example.foody.model.user.CustomerUser;
import com.example.foody.utils.enums.BookingStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookingMapperImplTest {

    @InjectMocks
    private BookingMapperImpl bookingMapper;

    @Mock
    private BookingBuilder bookingBuilder;

    @Mock
    private SittingTimeMapper sittingTimeMapper;

    @Mock
    private UserHelper userHelper;

    @Mock
    private RestaurantMapper restaurantMapper;

    @Test
    void bookingToBookingResponseDTOWhenBookingIsNullReturnsNull() {
        // Act
        BookingResponseDTO result = bookingMapper.bookingToBookingResponseDTO(null);

        // Assert
        assertNull(result);
    }

    @Test
    void bookingToBookingResponseDTOWhenValidReturnsDTO() {
        // Arrange
        Booking booking = mock(Booking.class);
        SittingTime sittingTime = mock(SittingTime.class);
        CustomerUser customer = mock(CustomerUser.class);
        Restaurant restaurant = mock(Restaurant.class);

        when(booking.getId()).thenReturn(1L);
        when(booking.getSittingTime()).thenReturn(sittingTime);
        when(booking.getCustomer()).thenReturn(customer);
        when(booking.getRestaurant()).thenReturn(restaurant);
        when(booking.getDate()).thenReturn(LocalDate.of(2025, 1, 1));
        when(booking.getSeats()).thenReturn(4);
        when(booking.getStatus()).thenReturn(BookingStatus.ACTIVE);

        when(sittingTimeMapper.sittingTimeToSittingTimeResponseDTO(sittingTime)).thenReturn(mock(SittingTimeResponseDTO.class));
        when(userHelper.buildUserResponseDTO(customer)).thenReturn(mock(CustomerUserResponseDTO.class));
        when(restaurantMapper.restaurantToRestaurantResponseDTO(restaurant)).thenReturn(mock(RestaurantResponseDTO.class));

        // Act
        BookingResponseDTO result = bookingMapper.bookingToBookingResponseDTO(booking);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(LocalDate.of(2025, 1, 1), result.getDate());
        assertEquals(4, result.getSeats());
        assertEquals(BookingStatus.ACTIVE.name(), result.getStatus());
        verify(sittingTimeMapper).sittingTimeToSittingTimeResponseDTO(sittingTime);
        verify(userHelper).buildUserResponseDTO(customer);
        verify(restaurantMapper).restaurantToRestaurantResponseDTO(restaurant);
    }

    @Test
    void bookingToBookingResponseDTOWhenBookingStatusIsNullReturnsDTO() {
        // Arrange
        Booking booking = mock(Booking.class);
        SittingTime sittingTime = mock(SittingTime.class);
        CustomerUser customer = mock(CustomerUser.class);
        Restaurant restaurant = mock(Restaurant.class);

        when(booking.getId()).thenReturn(1L);
        when(booking.getSittingTime()).thenReturn(sittingTime);
        when(booking.getCustomer()).thenReturn(customer);
        when(booking.getRestaurant()).thenReturn(restaurant);
        when(booking.getDate()).thenReturn(LocalDate.of(2025, 1, 1));
        when(booking.getSeats()).thenReturn(4);
        when(booking.getStatus()).thenReturn(null);

        when(sittingTimeMapper.sittingTimeToSittingTimeResponseDTO(sittingTime)).thenReturn(mock(SittingTimeResponseDTO.class));
        when(userHelper.buildUserResponseDTO(customer)).thenReturn(mock(CustomerUserResponseDTO.class));
        when(restaurantMapper.restaurantToRestaurantResponseDTO(restaurant)).thenReturn(mock(RestaurantResponseDTO.class));

        // Act
        BookingResponseDTO result = bookingMapper.bookingToBookingResponseDTO(booking);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(LocalDate.of(2025, 1, 1), result.getDate());
        assertEquals(4, result.getSeats());
        assertNull(result.getStatus());
        verify(sittingTimeMapper).sittingTimeToSittingTimeResponseDTO(sittingTime);
        verify(userHelper).buildUserResponseDTO(customer);
        verify(restaurantMapper).restaurantToRestaurantResponseDTO(restaurant);
    }

    @Test
    void bookingRequestDTOToBookingWhenRequestIsNullReturnsNull() {
        // Act
        Booking result = bookingMapper.bookingRequestDTOToBooking(null);

        // Assert
        assertNull(result);
    }

    @Test
    void bookingRequestDTOToBookingWhenValidReturnsBooking() {
        // Arrange
        BookingRequestDTO requestDTO = mock(BookingRequestDTO.class);
        when(requestDTO.getDate()).thenReturn(LocalDate.of(2025, 1, 1));
        when(requestDTO.getSeats()).thenReturn(4);

        Booking booking = mock(Booking.class);
        when(bookingBuilder.date(LocalDate.of(2025, 1, 1))).thenReturn(bookingBuilder);
        when(bookingBuilder.seats(4)).thenReturn(bookingBuilder);
        when(bookingBuilder.build()).thenReturn(booking);

        // Act
        Booking result = bookingMapper.bookingRequestDTOToBooking(requestDTO);

        // Assert
        assertNotNull(result);
        verify(bookingBuilder).date(LocalDate.of(2025, 1, 1));
        verify(bookingBuilder).seats(4);
        verify(bookingBuilder).build();
    }

    @Test
    void bookingsToBookingResponseDTOsWhenBookingsIsNullReturnsNull() {
        // Act
        List<BookingResponseDTO> result = bookingMapper.bookingsToBookingResponseDTOs(null);

        // Assert
        assertNull(result);
    }

    @Test
    void bookingsToBookingResponseDTOsWhenValidReturnsDTOList() {
        // Arrange
        Booking booking = mock(Booking.class);
        when(booking.getId()).thenReturn(1L);
        when(booking.getDate()).thenReturn(LocalDate.of(2025, 1, 1));
        when(booking.getSeats()).thenReturn(4);
        when(booking.getStatus()).thenReturn(BookingStatus.ACTIVE);

        SittingTime sittingTime = mock(SittingTime.class);
        CustomerUser customer = mock(CustomerUser.class);
        Restaurant restaurant = mock(Restaurant.class);

        when(booking.getSittingTime()).thenReturn(sittingTime);
        when(booking.getCustomer()).thenReturn(customer);
        when(booking.getRestaurant()).thenReturn(restaurant);

        when(sittingTimeMapper.sittingTimeToSittingTimeResponseDTO(sittingTime))
                .thenReturn(mock(SittingTimeResponseDTO.class));
        when(userHelper.buildUserResponseDTO(customer))
                .thenReturn(mock(CustomerUserResponseDTO.class));
        when(restaurantMapper.restaurantToRestaurantResponseDTO(restaurant))
                .thenReturn(mock(RestaurantResponseDTO.class));

        List<Booking> bookings = Collections.singletonList(booking);

        // Act
        List<BookingResponseDTO> result = bookingMapper.bookingsToBookingResponseDTOs(bookings);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(LocalDate.of(2025, 1, 1), result.get(0).getDate());
        assertEquals(4, result.get(0).getSeats());
        assertEquals(BookingStatus.ACTIVE.name(), result.get(0).getStatus());
        verify(sittingTimeMapper).sittingTimeToSittingTimeResponseDTO(sittingTime);
        verify(userHelper).buildUserResponseDTO(customer);
        verify(restaurantMapper).restaurantToRestaurantResponseDTO(restaurant);
    }
}