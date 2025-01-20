package com.example.foody.service.impl;

import com.example.foody.TestDataUtil;
import com.example.foody.dto.request.WeekDayInfoRequestDTO;
import com.example.foody.dto.request.WeekDayInfoUpdateRequestDTO;
import com.example.foody.dto.response.WeekDayInfoResponseDTO;
import com.example.foody.exceptions.entity.*;
import com.example.foody.exceptions.restaurant.ForbiddenRestaurantAccessException;
import com.example.foody.mapper.WeekDayInfoMapper;
import com.example.foody.model.Restaurant;
import com.example.foody.model.SittingTime;
import com.example.foody.model.WeekDayInfo;
import com.example.foody.model.user.RestaurateurUser;
import com.example.foody.model.user.User;
import com.example.foody.repository.RestaurantRepository;
import com.example.foody.repository.WeekDayInfoRepository;
import com.example.foody.service.SittingTimeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WeekDayInfoServiceImplTest {

    @InjectMocks
    private WeekDayInfoServiceImpl weekDayInfoService;

    @Mock
    private WeekDayInfoRepository weekDayInfoRepository;

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private WeekDayInfoMapper weekDayInfoMapper;

    @Mock
    private SittingTimeService sittingTimeService;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    private void mockSecurityContext(User user) {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getPrincipal()).thenReturn(user);
    }

    @Test
    void saveWhenWeekDayInfoIsValidReturnsResponseDTO() {
        // Arrange
        RestaurateurUser restaurateur = TestDataUtil.createTestRestaurateurUser();
        WeekDayInfoRequestDTO requestDTO = TestDataUtil.createTestWeekDayInfoRequestDTO();
        WeekDayInfo weekDayInfo = TestDataUtil.createTestWeekDayInfo();
        Restaurant restaurant = TestDataUtil.createTestRestaurant();
        mockSecurityContext(restaurateur);

        when(restaurantRepository.findById(requestDTO.getRestaurantId())).thenReturn(Optional.of(restaurant));
        when(weekDayInfoMapper.weekDayInfoRequestDTOToWeekDayInfo(requestDTO)).thenReturn(weekDayInfo);
        when(weekDayInfoRepository.save(any(WeekDayInfo.class))).thenReturn(weekDayInfo);
        when(weekDayInfoMapper.weekDayInfoToWeekDayInfoResponseDTO(any(WeekDayInfo.class)))
                .thenReturn(TestDataUtil.createTestWeekDayInfoResponseDTO());

        // Act
        WeekDayInfoResponseDTO responseDTO = weekDayInfoService.save(requestDTO);

        // Assert
        assertNotNull(responseDTO);
        verify(weekDayInfoRepository, times(1)).save(any(WeekDayInfo.class));
        verify(sittingTimeService, times(1)).createForWeekDayInfo(any(WeekDayInfo.class));
    }

    @Test
    void saveWhenDuplicateThrowsEntityDuplicateException() {
        // Arrange
        RestaurateurUser restaurateur = TestDataUtil.createTestRestaurateurUser();
        WeekDayInfoRequestDTO requestDTO = TestDataUtil.createTestWeekDayInfoRequestDTO();
        WeekDayInfo weekDayInfo = TestDataUtil.createTestWeekDayInfo();
        Restaurant restaurant = TestDataUtil.createTestRestaurant();
        mockSecurityContext(restaurateur);

        when(restaurantRepository.findById(requestDTO.getRestaurantId())).thenReturn(Optional.of(restaurant));
        when(weekDayInfoMapper.weekDayInfoRequestDTOToWeekDayInfo(requestDTO)).thenReturn(weekDayInfo);
        doThrow(DataIntegrityViolationException.class).when(weekDayInfoRepository).save(any(WeekDayInfo.class));

        // Act & Assert
        assertThrows(EntityDuplicateException.class, () -> weekDayInfoService.save(requestDTO));
        verify(weekDayInfoRepository, times(1)).save(any(WeekDayInfo.class));
    }

    @Test
    void saveWhenSaveFailsThrowsEntityCreationException() {
        // Arrange
        RestaurateurUser restaurateur = TestDataUtil.createTestRestaurateurUser();
        WeekDayInfoRequestDTO requestDTO = TestDataUtil.createTestWeekDayInfoRequestDTO();
        WeekDayInfo weekDayInfo = TestDataUtil.createTestWeekDayInfo();
        Restaurant restaurant = TestDataUtil.createTestRestaurant();
        mockSecurityContext(restaurateur);

        when(restaurantRepository.findById(requestDTO.getRestaurantId())).thenReturn(Optional.of(restaurant));
        when(weekDayInfoMapper.weekDayInfoRequestDTOToWeekDayInfo(requestDTO)).thenReturn(weekDayInfo);
        doThrow(RuntimeException.class).when(weekDayInfoRepository).save(any(WeekDayInfo.class));

        // Act & Assert
        assertThrows(EntityCreationException.class, () -> weekDayInfoService.save(requestDTO));
        verify(weekDayInfoRepository, times(1)).save(any(WeekDayInfo.class));
    }

    @Test
    void findAllReturnsListOfResponseDTOs() {
        // Arrange
        List<WeekDayInfo> weekDayInfos = List.of(TestDataUtil.createTestWeekDayInfo());

        when(weekDayInfoRepository.findAll()).thenReturn(weekDayInfos);
        when(weekDayInfoMapper.weekDayInfosToWeekDayInfoResponseDTOs(anyList()))
                .thenReturn(List.of(TestDataUtil.createTestWeekDayInfoResponseDTO()));

        // Act
        List<WeekDayInfoResponseDTO> responseDTOs = weekDayInfoService.findAll();

        // Assert
        assertNotNull(responseDTOs);
        assertEquals(1, responseDTOs.size());
    }

    @Test
    void findAllByRestaurantReturnsListOfResponseDTOs() {
        // Arrange
        List<WeekDayInfo> weekDayInfos = List.of(TestDataUtil.createTestWeekDayInfo());

        when(weekDayInfoRepository.findAllByRestaurantIdOrderByWeekDay(anyLong())).thenReturn(weekDayInfos);
        when(weekDayInfoMapper.weekDayInfosToWeekDayInfoResponseDTOs(anyList()))
                .thenReturn(List.of(TestDataUtil.createTestWeekDayInfoResponseDTO()));

        // Act
        List<WeekDayInfoResponseDTO> responseDTOs = weekDayInfoService.findAllByRestaurant(1L);

        // Assert
        assertNotNull(responseDTOs);
        assertEquals(1, responseDTOs.size());
    }

    @Test
    void updateWhenWeekDayInfoExistsUpdatesAndReturnsResponseDTO() {
        // Arrange
        RestaurateurUser restaurateur = TestDataUtil.createTestRestaurateurUser();
        WeekDayInfoUpdateRequestDTO updateRequestDTO = TestDataUtil.createTestWeekDayInfoUpdateRequestDTO();
        WeekDayInfo weekDayInfo = TestDataUtil.createTestWeekDayInfo();
        SittingTime sittingTime = TestDataUtil.createTestSittingTime();
        weekDayInfo.setSittingTimes(List.of(sittingTime));
        mockSecurityContext(restaurateur);

        when(weekDayInfoRepository.findById(weekDayInfo.getId())).thenReturn(Optional.of(weekDayInfo));
        when(weekDayInfoRepository.save(any(WeekDayInfo.class))).thenReturn(weekDayInfo);
        when(weekDayInfoMapper.weekDayInfoToWeekDayInfoResponseDTO(any(WeekDayInfo.class)))
                .thenReturn(TestDataUtil.createTestWeekDayInfoResponseDTO());

        // Act
        WeekDayInfoResponseDTO responseDTO = weekDayInfoService.update(weekDayInfo.getId(), updateRequestDTO);

        // Assert
        assertNotNull(responseDTO);
        verify(weekDayInfoRepository, times(1)).save(any(WeekDayInfo.class));
    }

    @Test
    void updateWhenWeekDayInfoDoesNotExistThrowsEntityNotFoundException() {
        // Arrange
        RestaurateurUser restaurateur = TestDataUtil.createTestRestaurateurUser();
        WeekDayInfoUpdateRequestDTO updateRequestDTO = TestDataUtil.createTestWeekDayInfoUpdateRequestDTO();
        WeekDayInfo weekDayInfo = TestDataUtil.createTestWeekDayInfo();
        mockSecurityContext(restaurateur);

        when(weekDayInfoRepository.findById(weekDayInfo.getId())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> weekDayInfoService.update(1L, updateRequestDTO));
    }

    @Test
    void updateWhenRestaurateurIsNotOwnerThrowsForbiddenRestaurantAccessException() {
        // Arrange
        RestaurateurUser restaurateur = TestDataUtil.createTestRestaurateurUser();
        RestaurateurUser otherRestaurateur = TestDataUtil.createTestRestaurateurUser();
        WeekDayInfoUpdateRequestDTO updateRequestDTO = TestDataUtil.createTestWeekDayInfoUpdateRequestDTO();
        WeekDayInfo weekDayInfo = TestDataUtil.createTestWeekDayInfo();
        Restaurant restaurant = TestDataUtil.createTestRestaurant();
        otherRestaurateur.setId(2L);
        restaurant.setRestaurateur(otherRestaurateur);
        weekDayInfo.setRestaurant(restaurant);
        mockSecurityContext(restaurateur);

        when(weekDayInfoRepository.findById(weekDayInfo.getId())).thenReturn(Optional.of(weekDayInfo));

        // Act & Assert
        assertThrows(ForbiddenRestaurantAccessException.class, () -> weekDayInfoService.update(weekDayInfo.getId(), updateRequestDTO));
    }

    @Test
    void updateWhenEditFailsThrowsEntityEditException() {
        // Arrange
        RestaurateurUser restaurateur = TestDataUtil.createTestRestaurateurUser();
        WeekDayInfoUpdateRequestDTO updateRequestDTO = TestDataUtil.createTestWeekDayInfoUpdateRequestDTO();
        WeekDayInfo weekDayInfo = TestDataUtil.createTestWeekDayInfo();
        mockSecurityContext(restaurateur);

        when(weekDayInfoRepository.findById(weekDayInfo.getId())).thenReturn(Optional.of(weekDayInfo));
        doThrow(RuntimeException.class).when(weekDayInfoRepository).save(any(WeekDayInfo.class));

        // Act & Assert
        assertThrows(EntityEditException.class, () -> weekDayInfoService.update(weekDayInfo.getId(), updateRequestDTO));
        verify(weekDayInfoRepository, times(1)).save(any(WeekDayInfo.class));
    }
}