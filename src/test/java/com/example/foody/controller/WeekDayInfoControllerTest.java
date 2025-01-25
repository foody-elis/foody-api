package com.example.foody.controller;

import com.example.foody.TestDataUtil;
import com.example.foody.dto.request.WeekDayInfoRequestDTO;
import com.example.foody.dto.request.WeekDayInfoUpdateRequestDTO;
import com.example.foody.dto.response.WeekDayInfoResponseDTO;
import com.example.foody.exceptions.entity.EntityNotFoundException;
import com.example.foody.service.WeekDayInfoService;
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

@ExtendWith(MockitoExtension.class)
public class WeekDayInfoControllerTest {

    @InjectMocks
    private WeekDayInfoController weekDayInfoController;

    @Mock
    private WeekDayInfoService weekDayInfoService;

    @Test
    void saveWeekDayInfoWhenValidRequestReturnsCreatedResponse() {
        // Arrange
        WeekDayInfoRequestDTO requestDTO = TestDataUtil.createTestWeekDayInfoRequestDTO();
        WeekDayInfoResponseDTO responseDTO = TestDataUtil.createTestWeekDayInfoResponseDTO();

        when(weekDayInfoService.save(requestDTO)).thenReturn(responseDTO);

        // Act
        ResponseEntity<WeekDayInfoResponseDTO> response = weekDayInfoController.saveWeekDayInfo(requestDTO);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(responseDTO, response.getBody());
    }

    @Test
    void getWeekDayInfosWhenCalledReturnsOkResponse() {
        // Arrange
        List<WeekDayInfoResponseDTO> responseDTOs =
                Collections.singletonList(TestDataUtil.createTestWeekDayInfoResponseDTO());

        when(weekDayInfoService.findAll()).thenReturn(responseDTOs);

        // Act
        ResponseEntity<List<WeekDayInfoResponseDTO>> response = weekDayInfoController.getWeekDayInfos();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseDTOs, response.getBody());
    }

    @Test
    void getWeekDayInfosByRestaurantWhenValidIdReturnsOkResponse() {
        // Arrange
        List<WeekDayInfoResponseDTO> responseDTOs =
                Collections.singletonList(TestDataUtil.createTestWeekDayInfoResponseDTO());

        when(weekDayInfoService.findAllByRestaurant(1L)).thenReturn(responseDTOs);

        // Act
        ResponseEntity<List<WeekDayInfoResponseDTO>> response =
                weekDayInfoController.getWeekDayInfosByRestaurant(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseDTOs, response.getBody());
    }

    @Test
    void getWeekDayInfosByRestaurantWhenInvalidIdThrowsEntityNotFoundException() {
        // Arrange
        when(weekDayInfoService.findAllByRestaurant(1L)).thenThrow(new EntityNotFoundException("restaurant", "id", 1L));

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> weekDayInfoController.getWeekDayInfosByRestaurant(1L));
    }

    @Test
    void updateWeekDayInfoWhenValidRequestReturnsOkResponse() {
        // Arrange
        WeekDayInfoUpdateRequestDTO requestDTO = TestDataUtil.createTestWeekDayInfoUpdateRequestDTO();
        WeekDayInfoResponseDTO responseDTO = TestDataUtil.createTestWeekDayInfoResponseDTO();

        when(weekDayInfoService.update(1L, requestDTO)).thenReturn(responseDTO);

        // Act
        ResponseEntity<WeekDayInfoResponseDTO> response =
                weekDayInfoController.updateWeekDayInfo(1L, requestDTO);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseDTO, response.getBody());
    }

    @Test
    void updateWeekDayInfoWhenInvalidIdThrowsEntityNotFoundException() {
        // Arrange
        WeekDayInfoUpdateRequestDTO requestDTO = TestDataUtil.createTestWeekDayInfoUpdateRequestDTO();

        when(weekDayInfoService.update(1L, requestDTO)).thenThrow(new EntityNotFoundException("weekdayinfo", "id", 1L));

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> weekDayInfoController.updateWeekDayInfo(1L, requestDTO));
    }
}