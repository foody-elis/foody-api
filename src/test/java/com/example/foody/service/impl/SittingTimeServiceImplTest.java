package com.example.foody.service.impl;

import com.example.foody.TestDataUtil;
import com.example.foody.builder.impl.SittingTimeBuilderImpl;
import com.example.foody.dto.response.SittingTimeResponseDTO;
import com.example.foody.exceptions.entity.EntityCreationException;
import com.example.foody.exceptions.entity.EntityDeletionException;
import com.example.foody.exceptions.entity.EntityNotFoundException;
import com.example.foody.exceptions.sitting_time.InvalidWeekDayException;
import com.example.foody.mapper.SittingTimeMapper;
import com.example.foody.model.Booking;
import com.example.foody.model.SittingTime;
import com.example.foody.model.WeekDayInfo;
import com.example.foody.repository.SittingTimeRepository;
import com.example.foody.service.BookingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link SittingTimeServiceImpl} class using mock services.
 */
@ExtendWith(MockitoExtension.class)
public class SittingTimeServiceImplTest {

    @InjectMocks
    private SittingTimeServiceImpl sittingTimeService;

    @Mock
    private SittingTimeRepository sittingTimeRepository;

    @Mock
    private SittingTimeMapper sittingTimeMapper;

    @Mock
    private BookingService bookingService;

    private void mockSittingTimeService() {
        SittingTimeBuilderImpl sittingTimeBuilder = new SittingTimeBuilderImpl();
        sittingTimeService = new SittingTimeServiceImpl(
                sittingTimeRepository,
                sittingTimeMapper,
                sittingTimeBuilder,
                bookingService
        );
    }

    @Test
    void createForWeekDayInfoWhenWeekDayInfoIsValidReturnsSittingTimes() {
        // Arrange
        WeekDayInfo weekDayInfo = TestDataUtil.createTestWeekDayInfo();
        SittingTime sittingTime = TestDataUtil.createTestSittingTime();
        mockSittingTimeService();

        when(sittingTimeRepository.save(any(SittingTime.class))).thenReturn(sittingTime);

        // Act
        List<SittingTime> sittingTimes = sittingTimeService.createForWeekDayInfo(weekDayInfo);

        // Assert
        assertNotNull(sittingTimes);
        assertFalse(sittingTimes.isEmpty());
        verify(sittingTimeRepository, atLeast(1)).save(any(SittingTime.class));
    }

    @Test
    void createForWeekDayInfoWhenWeekDayInfoStartLaunchIsNullReturnsOnlyDinnerSittingTimes() {
        // Arrange
        WeekDayInfo weekDayInfo = TestDataUtil.createTestWeekDayInfo();
        SittingTime sittingTime = TestDataUtil.createTestSittingTime();
        weekDayInfo.setStartLaunch(null);
        mockSittingTimeService();

        when(sittingTimeRepository.save(any(SittingTime.class))).thenReturn(sittingTime);

        // Act
        List<SittingTime> sittingTimes = sittingTimeService.createForWeekDayInfo(weekDayInfo);

        // Assert
        assertNotNull(sittingTimes);
        assertFalse(sittingTimes.isEmpty());
        verify(sittingTimeRepository, atLeast(1)).save(any(SittingTime.class));
    }

    @Test
    void createForWeekDayInfoWhenWeekDayInfoEndLaunchIsNullReturnsOnlyDinnerSittingTimes() {
        // Arrange
        WeekDayInfo weekDayInfo = TestDataUtil.createTestWeekDayInfo();
        SittingTime sittingTime = TestDataUtil.createTestSittingTime();
        weekDayInfo.setEndLaunch(null);
        mockSittingTimeService();

        when(sittingTimeRepository.save(any(SittingTime.class))).thenReturn(sittingTime);

        // Act
        List<SittingTime> sittingTimes = sittingTimeService.createForWeekDayInfo(weekDayInfo);

        // Assert
        assertNotNull(sittingTimes);
        assertFalse(sittingTimes.isEmpty());
        verify(sittingTimeRepository, atLeast(1)).save(any(SittingTime.class));
    }

    @Test
    void createForWeekDayInfoWhenSaveFailsThrowsEntityCreationException() {
        // Arrange
        WeekDayInfo weekDayInfo = TestDataUtil.createTestWeekDayInfo();
        mockSittingTimeService();

        when(sittingTimeRepository.save(any(SittingTime.class))).thenThrow(RuntimeException.class);

        // Act & Assert
        assertThrows(EntityCreationException.class, () -> sittingTimeService.createForWeekDayInfo(weekDayInfo));
        verify(sittingTimeRepository, atLeast(1)).save(any(SittingTime.class));
    }

    @Test
    void findAllReturnsSittingTimeResponseDTOs() {
        // Arrange
        List<SittingTime> sittingTimes = List.of(TestDataUtil.createTestSittingTime());
        when(sittingTimeRepository.findAll()).thenReturn(sittingTimes);
        when(sittingTimeMapper.sittingTimesToSittingTimeResponseDTOs(sittingTimes))
                .thenReturn(List.of(TestDataUtil.createTestSittingTimeResponseDTO()));

        // Act
        List<SittingTimeResponseDTO> responseDTOs = sittingTimeService.findAll();

        // Assert
        assertNotNull(responseDTOs);
        assertEquals(1, responseDTOs.size());
        verify(sittingTimeRepository, times(1)).findAll();
    }

    @Test
    void findAllByRestaurantReturnsSittingTimeResponseDTOs() {
        // Arrange
        long restaurantId = 1L;
        List<SittingTime> sittingTimes = List.of(TestDataUtil.createTestSittingTime());
        when(sittingTimeRepository.findAllByWeekDayInfoRestaurantId(restaurantId))
                .thenReturn(sittingTimes);
        when(sittingTimeMapper.sittingTimesToSittingTimeResponseDTOs(sittingTimes))
                .thenReturn(List.of(TestDataUtil.createTestSittingTimeResponseDTO()));

        // Act
        List<SittingTimeResponseDTO> responseDTOs = sittingTimeService.findAllByRestaurant(restaurantId);

        // Assert
        assertNotNull(responseDTOs);
        assertEquals(1, responseDTOs.size());
        verify(sittingTimeRepository, times(1)).findAllByWeekDayInfoRestaurantId(restaurantId);
    }

    @Test
    void findAllByRestaurantAndWeekDayReturnsSittingTimeResponseDTOs() {
        // Arrange
        long restaurantId = 1L;
        int weekDay = 1;
        List<SittingTime> sittingTimes = List.of(TestDataUtil.createTestSittingTime());
        when(sittingTimeRepository.findAllByWeekDayInfo_Restaurant_IdAndWeekDayInfo_WeekDayOrderByStart(restaurantId, weekDay))
                .thenReturn(sittingTimes);
        when(sittingTimeMapper.sittingTimesToSittingTimeResponseDTOs(sittingTimes))
                .thenReturn(List.of(TestDataUtil.createTestSittingTimeResponseDTO()));

        // Act
        List<SittingTimeResponseDTO> responseDTOs = sittingTimeService.findAllByRestaurantAndWeekDay(restaurantId, weekDay);

        // Assert
        assertNotNull(responseDTOs);
        assertEquals(1, responseDTOs.size());
        verify(sittingTimeRepository, times(1))
                .findAllByWeekDayInfo_Restaurant_IdAndWeekDayInfo_WeekDayOrderByStart(restaurantId, weekDay);
    }

    @Test
    void findAllByRestaurantAndWeekDayWhenWeekDayIsGreaterThan7ThrowsInvalidWeekDayException() {
        // Arrange
        int weekDay = 8;

        // Act & Assert
        assertThrows(InvalidWeekDayException.class, () -> sittingTimeService.findAllByRestaurantAndWeekDay(0L, weekDay));
        verify(sittingTimeRepository, never()).findAllByWeekDayInfo_Restaurant_IdAndWeekDayInfo_WeekDayOrderByStart(anyLong(), anyInt());
    }

    @Test
    void findAllByRestaurantAndWeekDayWhenWeekDayIsLessThan1ThrowsInvalidWeekDayException() {
        // Arrange
        int weekDay = 0;

        // Act & Assert
        assertThrows(InvalidWeekDayException.class, () -> sittingTimeService.findAllByRestaurantAndWeekDay(0L, weekDay));
        verify(sittingTimeRepository, never()).findAllByWeekDayInfo_Restaurant_IdAndWeekDayInfo_WeekDayOrderByStart(anyLong(), anyInt());
    }

    @Test
    void findAllByRestaurantAndWeekDayAndStartAfterNowReturnsSittingTimeResponseDTOs() {
        // Arrange
        long restaurantId = 1L;
        int weekDay = 1;
        List<SittingTime> sittingTimes = List.of(TestDataUtil.createTestSittingTime());
        when(sittingTimeRepository.findAllByRestaurantIdAndWeekDayAndStartAfterNowOrderByStart(restaurantId, weekDay))
                .thenReturn(sittingTimes);
        when(sittingTimeMapper.sittingTimesToSittingTimeResponseDTOs(sittingTimes))
                .thenReturn(List.of(TestDataUtil.createTestSittingTimeResponseDTO()));

        // Act
        List<SittingTimeResponseDTO> responseDTOs = sittingTimeService.findAllByRestaurantAndWeekDayAndStartAfterNow(restaurantId, weekDay);

        // Assert
        assertNotNull(responseDTOs);
        assertEquals(1, responseDTOs.size());
        verify(sittingTimeRepository, times(1))
                .findAllByRestaurantIdAndWeekDayAndStartAfterNowOrderByStart(restaurantId, weekDay);
    }

    @Test
    void removeWhenSittingTimeExistsDeletesSittingTime() {
        // Arrange
        SittingTime sittingTime = TestDataUtil.createTestSittingTime();
        Booking booking = TestDataUtil.createTestBooking();
        sittingTime.setBookings(List.of(booking));

        when(sittingTimeRepository.findById(sittingTime.getId())).thenReturn(Optional.of(sittingTime));

        // Act
        boolean result = sittingTimeService.remove(sittingTime.getId());

        // Assert
        assertTrue(result);
        verify(sittingTimeRepository, times(1)).findById(sittingTime.getId());
        verify(sittingTimeRepository, times(1)).save(sittingTime);
    }

    @Test
    void removeWhenSittingTimeDoesNotExistThrowsEntityNotFoundException() {
        // Arrange
        when(sittingTimeRepository.findById(0L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> sittingTimeService.remove(0L));
        verify(sittingTimeRepository, times(1)).findById(0L);
        verify(sittingTimeRepository, never()).save(any(SittingTime.class));
    }

    @Test
    void removeWhenSaveFailsThrowsEntityDeletionException() {
        // Arrange
        SittingTime sittingTime = TestDataUtil.createTestSittingTime();
        Booking booking = TestDataUtil.createTestBooking();
        sittingTime.setBookings(List.of(booking));

        when(sittingTimeRepository.findById(sittingTime.getId())).thenReturn(Optional.of(sittingTime));
        doThrow(RuntimeException.class).when(sittingTimeRepository).save(any(SittingTime.class));

        // Act & Assert
        assertThrows(EntityDeletionException.class, () -> sittingTimeService.remove(sittingTime.getId()));
        verify(sittingTimeRepository, times(1)).findById(sittingTime.getId());
        verify(sittingTimeRepository, times(1)).save(sittingTime);
    }
}