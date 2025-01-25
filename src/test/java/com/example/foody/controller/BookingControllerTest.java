package com.example.foody.controller;

import com.example.foody.TestDataUtil;
import com.example.foody.dto.request.BookingRequestDTO;
import com.example.foody.dto.response.BookingResponseDTO;
import com.example.foody.exceptions.entity.EntityNotFoundException;
import com.example.foody.model.user.CustomerUser;
import com.example.foody.service.BookingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookingControllerTest {

    @InjectMocks
    private BookingController bookingController;

    @Mock
    private BookingService bookingService;

    @Test
    void saveBookingWhenValidRequestReturnsCreatedResponse() {
        // Arrange
        BookingRequestDTO requestDTO = TestDataUtil.createTestBookingRequestDTO();
        BookingResponseDTO responseDTO = TestDataUtil.createTestBookingResponseDTO();

        when(bookingService.save(requestDTO)).thenReturn(responseDTO);

        // Act
        ResponseEntity<BookingResponseDTO> response = bookingController.saveBooking(requestDTO);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(responseDTO, response.getBody());
    }

    @Test
    void getBookingsWhenCalledReturnsOkResponse() {
        // Arrange
        List<BookingResponseDTO> responseDTOs =
                Collections.singletonList(TestDataUtil.createTestBookingResponseDTO());

        when(bookingService.findAll()).thenReturn(responseDTOs);

        // Act
        ResponseEntity<List<BookingResponseDTO>> response = bookingController.getBookings();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseDTOs, response.getBody());
    }

    @Test
    void getBookingByIdWhenValidIdReturnsOkResponse() {
        // Arrange
        BookingResponseDTO responseDTO = TestDataUtil.createTestBookingResponseDTO();

        when(bookingService.findById(1L)).thenReturn(responseDTO);

        // Act
        ResponseEntity<BookingResponseDTO> response = bookingController.getBookingById(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseDTO, response.getBody());
    }

    @Test
    void getBookingByIdWhenInvalidIdThrowsEntityNotFoundException() {
        // Arrange
        when(bookingService.findById(1L)).thenThrow(new EntityNotFoundException("booking", "id", 1L));

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> bookingController.getBookingById(1L));
    }

    @Test
    void getBookingsByCustomerWhenCalledReturnsOkResponse() {
        // Arrange
        List<BookingResponseDTO> responseDTOs =
                Collections.singletonList(TestDataUtil.createTestBookingResponseDTO());
        CustomerUser customer = TestDataUtil.createTestCustomerUser();

        when(bookingService.findAllByCustomer(customer.getId())).thenReturn(responseDTOs);

        // Act
        ResponseEntity<List<BookingResponseDTO>> response = bookingController.getBookingsByCustomer(customer);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseDTOs, response.getBody());
    }

    @Test
    void getBookingsByRestaurantWhenValidIdReturnsOkResponse() {
        // Arrange
        List<BookingResponseDTO> responseDTOs =
                Collections.singletonList(TestDataUtil.createTestBookingResponseDTO());

        when(bookingService.findAllByRestaurant(1L)).thenReturn(responseDTOs);

        // Act
        ResponseEntity<List<BookingResponseDTO>> response = bookingController.getBookingsByRestaurant(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseDTOs, response.getBody());
    }

    @Test
    void cancelBookingWhenValidIdReturnsOkResponse() {
        // Arrange
        BookingResponseDTO responseDTO = TestDataUtil.createTestBookingResponseDTO();

        when(bookingService.cancelById(1L)).thenReturn(responseDTO);

        // Act
        ResponseEntity<BookingResponseDTO> response = bookingController.cancelBooking(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseDTO, response.getBody());
    }

    @Test
    void removeBookingWhenValidIdRemovesBooking() {
        // Arrange
        when(bookingService.remove(1L)).thenReturn(true);

        // Act
        ResponseEntity<Void> response = bookingController.removeBooking(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(bookingService, times(1)).remove(1L);
    }
}