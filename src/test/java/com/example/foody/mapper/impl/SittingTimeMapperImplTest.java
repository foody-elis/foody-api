package com.example.foody.mapper.impl;

import com.example.foody.dto.response.SittingTimeResponseDTO;
import com.example.foody.model.SittingTime;
import com.example.foody.model.WeekDayInfo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test class for {@link SittingTimeMapperImpl} class using mock services.
 */
@ExtendWith(MockitoExtension.class)
public class SittingTimeMapperImplTest {

    @InjectMocks
    private SittingTimeMapperImpl sittingTimeMapper;

    @Test
    void sittingTimeToSittingTimeResponseDTOWhenSittingTimeIsNullReturnsNull() {
        // Act
        SittingTimeResponseDTO result = sittingTimeMapper.sittingTimeToSittingTimeResponseDTO(null);

        // Assert
        assertNull(result);
    }

    @Test
    void sittingTimeToSittingTimeResponseDTOWhenValidReturnsDTO() {
        // Arrange
        SittingTime sittingTime = mock(SittingTime.class);
        when(sittingTime.getId()).thenReturn(1L);
        when(sittingTime.getStart()).thenReturn(LocalTime.of(12, 0));
        when(sittingTime.getEnd()).thenReturn(LocalTime.of(14, 0));

        WeekDayInfo weekDayInfo = mock(WeekDayInfo.class);
        when(weekDayInfo.getId()).thenReturn(2L);
        when(sittingTime.getWeekDayInfo()).thenReturn(weekDayInfo);

        // Act
        SittingTimeResponseDTO result = sittingTimeMapper.sittingTimeToSittingTimeResponseDTO(sittingTime);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(LocalTime.of(12, 0), result.getStart());
        assertEquals(LocalTime.of(14, 0), result.getEnd());
        assertEquals(2L, result.getWeekDayInfoId());
    }

    @Test
    void sittingTimeToSittingTimeResponseDTOWhenSittingTimeWeekDayInfoIsNullReturnsDTO() {
        // Arrange
        SittingTime sittingTime = mock(SittingTime.class);
        when(sittingTime.getId()).thenReturn(1L);
        when(sittingTime.getStart()).thenReturn(LocalTime.of(12, 0));
        when(sittingTime.getEnd()).thenReturn(LocalTime.of(14, 0));
        when(sittingTime.getWeekDayInfo()).thenReturn(null);

        // Act
        SittingTimeResponseDTO result = sittingTimeMapper.sittingTimeToSittingTimeResponseDTO(sittingTime);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(LocalTime.of(12, 0), result.getStart());
        assertEquals(LocalTime.of(14, 0), result.getEnd());
        assertNull(result.getWeekDayInfoId());
    }

    @Test
    void sittingTimesToSittingTimeResponseDTOsWhenSittingTimesIsNullReturnsNull() {
        // Act
        List<SittingTimeResponseDTO> result = sittingTimeMapper.sittingTimesToSittingTimeResponseDTOs(null);

        // Assert
        assertNull(result);
    }

    @Test
    void sittingTimesToSittingTimeResponseDTOsWhenValidReturnsDTOList() {
        // Arrange
        SittingTime sittingTime = mock(SittingTime.class);
        when(sittingTime.getId()).thenReturn(1L);
        when(sittingTime.getStart()).thenReturn(LocalTime.of(12, 0));
        when(sittingTime.getEnd()).thenReturn(LocalTime.of(14, 0));

        WeekDayInfo weekDayInfo = mock(WeekDayInfo.class);
        when(weekDayInfo.getId()).thenReturn(2L);
        when(sittingTime.getWeekDayInfo()).thenReturn(weekDayInfo);

        List<SittingTime> sittingTimes = Collections.singletonList(sittingTime);

        // Act
        List<SittingTimeResponseDTO> result = sittingTimeMapper.sittingTimesToSittingTimeResponseDTOs(sittingTimes);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals(LocalTime.of(12, 0), result.get(0).getStart());
        assertEquals(LocalTime.of(14, 0), result.get(0).getEnd());
        assertEquals(2L, result.get(0).getWeekDayInfoId());
    }
}