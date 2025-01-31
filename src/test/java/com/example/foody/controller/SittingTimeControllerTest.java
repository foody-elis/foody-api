package com.example.foody.controller;

import com.example.foody.TestDataUtil;
import com.example.foody.dto.response.SittingTimeResponseDTO;
import com.example.foody.exceptions.entity.EntityNotFoundException;
import com.example.foody.exceptions.sitting_time.InvalidWeekDayException;
import com.example.foody.service.SittingTimeService;
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
import static org.mockito.Mockito.when;

/**
 * Test class for the endpoints in the {@link SittingTimeController} class using mock services.
 */
@ExtendWith(MockitoExtension.class)
public class SittingTimeControllerTest {

    @InjectMocks
    private SittingTimeController sittingTimeController;

    @Mock
    private SittingTimeService sittingTimeService;

    @Test
    void getSittingTimesWhenCalledReturnsOkResponse() {
        // Arrange
        List<SittingTimeResponseDTO> responseDTOs =
                Collections.singletonList(TestDataUtil.createTestSittingTimeResponseDTO());

        when(sittingTimeService.findAll()).thenReturn(responseDTOs);

        // Act
        ResponseEntity<List<SittingTimeResponseDTO>> response = sittingTimeController.getSittingTimes();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseDTOs, response.getBody());
    }

    @Test
    void getSittingTimesByRestaurantWhenValidIdReturnsOkResponse() {
        // Arrange
        List<SittingTimeResponseDTO> responseDTOs =
                Collections.singletonList(TestDataUtil.createTestSittingTimeResponseDTO());

        when(sittingTimeService.findAllByRestaurant(1L)).thenReturn(responseDTOs);

        // Act
        ResponseEntity<List<SittingTimeResponseDTO>> response = sittingTimeController.getSittingTimesByRestaurant(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseDTOs, response.getBody());
    }

    @Test
    void getSittingTimesByRestaurantWhenInvalidIdThrowsEntityNotFoundException() {
        // Arrange
        when(sittingTimeService.findAllByRestaurant(1L)).thenThrow(new EntityNotFoundException("restaurant", "id", 1L));

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> sittingTimeController.getSittingTimesByRestaurant(1L));
    }

    @Test
    void getSittingTimesByRestaurantAndWeekDayWhenValidRequestReturnsOkResponse() {
        // Arrange
        List<SittingTimeResponseDTO> responseDTOs =
                Collections.singletonList(TestDataUtil.createTestSittingTimeResponseDTO());

        when(sittingTimeService.findAllByRestaurantAndWeekDay(1L, 2)).thenReturn(responseDTOs);

        // Act
        ResponseEntity<List<SittingTimeResponseDTO>> response =
                sittingTimeController.getSittingTimesByRestaurantAndWeekDay(1L, 2);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseDTOs, response.getBody());
    }

    @Test
    void getSittingTimesByRestaurantAndWeekDayWhenInvalidWeekDayThrowsInvalidWeekDayException() {
        // Arrange
        when(sittingTimeService.findAllByRestaurantAndWeekDay(1L, 8))
                .thenThrow(new InvalidWeekDayException(8));

        // Act & Assert
        assertThrows(InvalidWeekDayException.class, () ->
                sittingTimeController.getSittingTimesByRestaurantAndWeekDay(1L, 8));
    }

    @Test
    void getSittingTimesByRestaurantAndWeekDayAndStartAfterNowWhenValidRequestReturnsOkResponse() {
        // Arrange
        List<SittingTimeResponseDTO> responseDTOs =
                Collections.singletonList(TestDataUtil.createTestSittingTimeResponseDTO());

        when(sittingTimeService.findAllByRestaurantAndWeekDayAndStartAfterNow(1L, 2))
                .thenReturn(responseDTOs);

        // Act
        ResponseEntity<List<SittingTimeResponseDTO>> response =
                sittingTimeController.getSittingTimesByRestaurantAndWeekDayAndStartAfterNow(1L, 2);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseDTOs, response.getBody());
    }

    @Test
    void getSittingTimesByRestaurantAndWeekDayAndStartAfterNowWhenInvalidWeekDayThrowsInvalidWeekDayException() {
        // Arrange
        when(sittingTimeService.findAllByRestaurantAndWeekDayAndStartAfterNow(1L, 8))
                .thenThrow(new InvalidWeekDayException(8));

        // Act & Assert
        assertThrows(InvalidWeekDayException.class, () ->
                sittingTimeController.getSittingTimesByRestaurantAndWeekDayAndStartAfterNow(1L, 8));
    }
}