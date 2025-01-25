package com.example.foody.mapper.impl;

import com.example.foody.TestDataUtil;
import com.example.foody.builder.WeekDayInfoBuilder;
import com.example.foody.dto.request.WeekDayInfoRequestDTO;
import com.example.foody.dto.request.WeekDayInfoUpdateRequestDTO;
import com.example.foody.dto.response.WeekDayInfoResponseDTO;
import com.example.foody.model.WeekDayInfo;
import com.example.foody.utils.enums.SittingTimeStep;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WeekDayInfoMapperImplTest {

    @InjectMocks
    private WeekDayInfoMapperImpl weekDayInfoMapper;

    @Mock
    private WeekDayInfoBuilder weekDayInfoBuilder;

    @Test
    void weekDayInfoToWeekDayInfoResponseDTOWhenWeekDayInfoIsNullReturnsNull() {
        // Act
        WeekDayInfoResponseDTO result = weekDayInfoMapper.weekDayInfoToWeekDayInfoResponseDTO(null);

        // Assert
        assertNull(result);
    }

    @Test
    void weekDayInfoToWeekDayInfoResponseDTOWhenValidReturnsDTO() {
        // Arrange
        WeekDayInfo weekDayInfo = TestDataUtil.createTestWeekDayInfo();

        // Act
        WeekDayInfoResponseDTO result = weekDayInfoMapper.weekDayInfoToWeekDayInfoResponseDTO(weekDayInfo);

        // Assert
        assertNotNull(result);
        assertEquals(weekDayInfo.getId(), result.getId());
        assertEquals(weekDayInfo.getWeekDay(), result.getWeekDay());
    }

    @Test
    void weekDayInfoToWeekDayInfoResponseDTOWhenWeekDayInfoSittingTimeStepIsNullReturnsDTO() {
        // Arrange
        WeekDayInfo weekDayInfo = TestDataUtil.createTestWeekDayInfo();
        weekDayInfo.setSittingTimeStep(null);

        // Act
        WeekDayInfoResponseDTO result = weekDayInfoMapper.weekDayInfoToWeekDayInfoResponseDTO(weekDayInfo);

        // Assert
        assertNotNull(result);
        assertEquals(weekDayInfo.getId(), result.getId());
        assertEquals(weekDayInfo.getWeekDay(), result.getWeekDay());
        assertNull(result.getSittingTimeStep());
    }

    @Test
    void weekDayInfoToWeekDayInfoResponseDTOWhenWeekDayInfoRestaurantIsNullReturnsDTO() {
        // Arrange
        WeekDayInfo weekDayInfo = TestDataUtil.createTestWeekDayInfo();
        weekDayInfo.setRestaurant(null);

        // Act
        WeekDayInfoResponseDTO result = weekDayInfoMapper.weekDayInfoToWeekDayInfoResponseDTO(weekDayInfo);

        // Assert
        assertNotNull(result);
        assertEquals(weekDayInfo.getId(), result.getId());
        assertEquals(weekDayInfo.getWeekDay(), result.getWeekDay());
    }

    @Test
    void weekDayInfoRequestDTOToWeekDayInfoWhenRequestIsNullReturnsNull() {
        // Act
        WeekDayInfo result = weekDayInfoMapper.weekDayInfoRequestDTOToWeekDayInfo(null);

        // Assert
        assertNull(result);
    }

    @Test
    void weekDayInfoRequestDTOToWeekDayInfoWhenValidReturnsWeekDayInfo() {
        // Arrange
        WeekDayInfoRequestDTO requestDTO = TestDataUtil.createTestWeekDayInfoRequestDTO();

        when(weekDayInfoBuilder.startLaunch(requestDTO.getStartLaunch())).thenReturn(weekDayInfoBuilder);
        when(weekDayInfoBuilder.endLaunch(requestDTO.getEndLaunch())).thenReturn(weekDayInfoBuilder);
        when(weekDayInfoBuilder.startDinner(requestDTO.getStartDinner())).thenReturn(weekDayInfoBuilder);
        when(weekDayInfoBuilder.endDinner(requestDTO.getEndDinner())).thenReturn(weekDayInfoBuilder);
        when(weekDayInfoBuilder.weekDay(requestDTO.getWeekDay())).thenReturn(weekDayInfoBuilder);
        when(weekDayInfoBuilder.sittingTimeStep(SittingTimeStep.SIXTY)).thenReturn(weekDayInfoBuilder);
        when(weekDayInfoBuilder.build()).thenReturn(mock(WeekDayInfo.class));

        // Act
        WeekDayInfo result = weekDayInfoMapper.weekDayInfoRequestDTOToWeekDayInfo(requestDTO);

        // Assert
        assertNotNull(result);
    }

    @Test
    void weekDayInfoRequestDTOToWeekDayInfoWhenWeekDayInfoRequestDTOWeekDayIsNullReturnsWeekDayInfo() {
        // Arrange
        WeekDayInfoRequestDTO requestDTO = TestDataUtil.createTestWeekDayInfoRequestDTO();
        requestDTO.setWeekDay(null);

        when(weekDayInfoBuilder.startLaunch(requestDTO.getStartLaunch())).thenReturn(weekDayInfoBuilder);
        when(weekDayInfoBuilder.endLaunch(requestDTO.getEndLaunch())).thenReturn(weekDayInfoBuilder);
        when(weekDayInfoBuilder.startDinner(requestDTO.getStartDinner())).thenReturn(weekDayInfoBuilder);
        when(weekDayInfoBuilder.endDinner(requestDTO.getEndDinner())).thenReturn(weekDayInfoBuilder);
        when(weekDayInfoBuilder.sittingTimeStep(SittingTimeStep.SIXTY)).thenReturn(weekDayInfoBuilder);
        when(weekDayInfoBuilder.build()).thenReturn(mock(WeekDayInfo.class));

        // Act
        WeekDayInfo result = weekDayInfoMapper.weekDayInfoRequestDTOToWeekDayInfo(requestDTO);

        // Assert
        assertNotNull(result);
    }

    @Test
    void weekDayInfoRequestDTOToWeekDayInfoWhenWeekDayInfoRequestDTOSittingTimeStepIsNullReturnsWeekDayInfo() {
        // Arrange
        WeekDayInfoRequestDTO requestDTO = TestDataUtil.createTestWeekDayInfoRequestDTO();
        requestDTO.setSittingTimeStep(null);

        when(weekDayInfoBuilder.startLaunch(requestDTO.getStartLaunch())).thenReturn(weekDayInfoBuilder);
        when(weekDayInfoBuilder.endLaunch(requestDTO.getEndLaunch())).thenReturn(weekDayInfoBuilder);
        when(weekDayInfoBuilder.startDinner(requestDTO.getStartDinner())).thenReturn(weekDayInfoBuilder);
        when(weekDayInfoBuilder.endDinner(requestDTO.getEndDinner())).thenReturn(weekDayInfoBuilder);
        when(weekDayInfoBuilder.weekDay(requestDTO.getWeekDay())).thenReturn(weekDayInfoBuilder);
        when(weekDayInfoBuilder.build()).thenReturn(mock(WeekDayInfo.class));

        // Act
        WeekDayInfo result = weekDayInfoMapper.weekDayInfoRequestDTOToWeekDayInfo(requestDTO);

        // Assert
        assertNotNull(result);
    }

    @Test
    void updateWeekDayInfoFromWeekDayInfoUpdateRequestDTOWhenWeekDayInfoIsNullDoesNotUpdateWeekDayInfo() {
        // Arrange
        WeekDayInfo weekDayInfo = null;
        WeekDayInfoUpdateRequestDTO updateRequestDTO = mock(WeekDayInfoUpdateRequestDTO.class);

        // Act
        weekDayInfoMapper.updateWeekDayInfoFromWeekDayInfoUpdateRequestDTO(weekDayInfo, updateRequestDTO);

        // Assert
        verify(updateRequestDTO, never()).getStartLaunch();
        verify(updateRequestDTO, never()).getEndLaunch();
        verify(updateRequestDTO, never()).getStartDinner();
        verify(updateRequestDTO, never()).getEndDinner();
        verify(updateRequestDTO, never()).getSittingTimeStep();
    }

    @Test
    void updateWeekDayInfoFromWeekDayInfoUpdateRequestDTOWhenWeekDayInfoUpdateRequestDTOIsNullDoesNotUpdateWeekDayInfo() {
        // Arrange
        WeekDayInfo weekDayInfo = mock(WeekDayInfo.class);
        WeekDayInfoUpdateRequestDTO updateRequestDTO = null;

        // Act
        weekDayInfoMapper.updateWeekDayInfoFromWeekDayInfoUpdateRequestDTO(weekDayInfo, updateRequestDTO);

        // Assert
        verify(weekDayInfo, never()).setStartLaunch(any());
        verify(weekDayInfo, never()).setEndLaunch(any());
        verify(weekDayInfo, never()).setStartDinner(any());
        verify(weekDayInfo, never()).setEndDinner(any());
        verify(weekDayInfo, never()).setSittingTimeStep(any());
    }

    @Test
    void updateWeekDayInfoFromWeekDayInfoUpdateRequestDTOWhenValidUpdatesWeekDayInfo() {
        // Arrange
        WeekDayInfo weekDayInfo = mock(WeekDayInfo.class);
        WeekDayInfoUpdateRequestDTO updateRequestDTO = mock(WeekDayInfoUpdateRequestDTO.class);

        when(updateRequestDTO.getStartLaunch()).thenReturn(LocalTime.of(12, 0));
        when(updateRequestDTO.getEndLaunch()).thenReturn(LocalTime.of(14, 0));
        when(updateRequestDTO.getStartDinner()).thenReturn(LocalTime.of(18, 0));
        when(updateRequestDTO.getEndDinner()).thenReturn(LocalTime.of(22, 0));
        when(updateRequestDTO.getSittingTimeStep()).thenReturn(SittingTimeStep.SIXTY.name());

        // Act
        weekDayInfoMapper.updateWeekDayInfoFromWeekDayInfoUpdateRequestDTO(weekDayInfo, updateRequestDTO);

        // Assert
        verify(weekDayInfo).setStartLaunch(updateRequestDTO.getStartLaunch());
        verify(weekDayInfo).setEndLaunch(updateRequestDTO.getEndLaunch());
        verify(weekDayInfo).setStartDinner(updateRequestDTO.getStartDinner());
        verify(weekDayInfo).setEndDinner(updateRequestDTO.getEndDinner());
        verify(weekDayInfo).setSittingTimeStep(SittingTimeStep.SIXTY);
    }

    @Test
    void updateWeekDayInfoFromWeekDayInfoUpdateRequestDTOWhenSittingTimeStepIsNullUpdatesWeekDayInfo() {
        // Arrange
        WeekDayInfo weekDayInfo = mock(WeekDayInfo.class);
        WeekDayInfoUpdateRequestDTO updateRequestDTO = mock(WeekDayInfoUpdateRequestDTO.class);

        when(updateRequestDTO.getStartLaunch()).thenReturn(LocalTime.of(12, 0));
        when(updateRequestDTO.getEndLaunch()).thenReturn(LocalTime.of(14, 0));
        when(updateRequestDTO.getStartDinner()).thenReturn(LocalTime.of(18, 0));
        when(updateRequestDTO.getEndDinner()).thenReturn(LocalTime.of(22, 0));
        when(updateRequestDTO.getSittingTimeStep()).thenReturn(null);

        // Act
        weekDayInfoMapper.updateWeekDayInfoFromWeekDayInfoUpdateRequestDTO(weekDayInfo, updateRequestDTO);

        // Assert
        verify(weekDayInfo).setStartLaunch(updateRequestDTO.getStartLaunch());
        verify(weekDayInfo).setEndLaunch(updateRequestDTO.getEndLaunch());
        verify(weekDayInfo).setStartDinner(updateRequestDTO.getStartDinner());
        verify(weekDayInfo).setEndDinner(updateRequestDTO.getEndDinner());
        verify(weekDayInfo, never()).setSittingTimeStep(any());
    }

    @Test
    void weekDayInfosToWeekDayInfoResponseDTOsWhenInfosIsNullReturnsNull() {
        // Act
        List<WeekDayInfoResponseDTO> result = weekDayInfoMapper.weekDayInfosToWeekDayInfoResponseDTOs(null);

        // Assert
        assertNull(result);
    }

    @Test
    void weekDayInfosToWeekDayInfoResponseDTOsWhenValidReturnsDTOList() {
        // Arrange
        WeekDayInfo weekDayInfo = mock(WeekDayInfo.class);

        when(weekDayInfo.getId()).thenReturn(1L);
        when(weekDayInfo.getWeekDay()).thenReturn(1);
        when(weekDayInfo.getStartLaunch()).thenReturn(LocalTime.of(12, 0));
        when(weekDayInfo.getEndLaunch()).thenReturn(LocalTime.of(14, 0));
        when(weekDayInfo.getStartDinner()).thenReturn(LocalTime.of(18, 0));
        when(weekDayInfo.getEndDinner()).thenReturn(LocalTime.of(22, 0));
        when(weekDayInfo.getSittingTimeStep()).thenReturn(SittingTimeStep.SIXTY);

        // Act
        List<WeekDayInfoResponseDTO> result = weekDayInfoMapper.weekDayInfosToWeekDayInfoResponseDTOs(Collections.singletonList(weekDayInfo));

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(weekDayInfo.getId(), result.get(0).getId());
        assertEquals(weekDayInfo.getWeekDay(), result.get(0).getWeekDay());
        assertEquals(weekDayInfo.getStartLaunch(), result.get(0).getStartLaunch());
        assertEquals(weekDayInfo.getEndLaunch(), result.get(0).getEndLaunch());
        assertEquals(weekDayInfo.getStartDinner(), result.get(0).getStartDinner());
        assertEquals(weekDayInfo.getEndDinner(), result.get(0).getEndDinner());
        assertEquals(SittingTimeStep.SIXTY.name(), result.get(0).getSittingTimeStep());
    }
}