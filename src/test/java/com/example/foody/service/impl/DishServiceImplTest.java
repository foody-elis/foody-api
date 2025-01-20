package com.example.foody.service.impl;

import com.example.foody.TestDataUtil;
import com.example.foody.dto.request.DishRequestDTO;
import com.example.foody.dto.request.DishUpdateRequestDTO;
import com.example.foody.dto.response.DishResponseDTO;
import com.example.foody.exceptions.entity.EntityCreationException;
import com.example.foody.exceptions.entity.EntityDeletionException;
import com.example.foody.exceptions.entity.EntityEditException;
import com.example.foody.exceptions.entity.EntityNotFoundException;
import com.example.foody.exceptions.restaurant.ForbiddenRestaurantAccessException;
import com.example.foody.helper.DishHelper;
import com.example.foody.mapper.DishMapper;
import com.example.foody.model.Dish;
import com.example.foody.model.Restaurant;
import com.example.foody.model.user.AdminUser;
import com.example.foody.model.user.RestaurateurUser;
import com.example.foody.model.user.User;
import com.example.foody.repository.DishRepository;
import com.example.foody.repository.RestaurantRepository;
import com.example.foody.service.GoogleDriveService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DishServiceImplTest {

    @InjectMocks
    private DishServiceImpl dishService;

    @Mock
    private DishRepository dishRepository;

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private DishMapper dishMapper;

    @Mock
    private DishHelper dishHelper;

    @Mock
    private GoogleDriveService googleDriveService;

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
    void saveWhenDishIsValidReturnsDishResponseDTO() {
        // Arrange
        RestaurateurUser restaurateur = TestDataUtil.createTestRestaurateurUser();
        DishRequestDTO dishRequestDTO = TestDataUtil.createTestDishRequestDTO();
        Dish dish = TestDataUtil.createTestDish();
        Restaurant restaurant = TestDataUtil.createTestRestaurant();
        mockSecurityContext(restaurateur);

        when(restaurantRepository.findById(dishRequestDTO.getRestaurantId())).thenReturn(Optional.of(restaurant));
        when(dishMapper.dishRequestDTOToDish(dishRequestDTO)).thenReturn(dish);
        when(dishRepository.save(dish)).thenReturn(dish);
        when(dishHelper.buildDishResponseDTO(dish)).thenReturn(TestDataUtil.createTestDishResponseDTO());

        // Act
        DishResponseDTO responseDTO = dishService.save(dishRequestDTO);

        // Assert
        assertNotNull(responseDTO);
        verify(dishRepository, times(1)).save(dish);
    }

    @Test
    void saveWhenDishPhotoBase64IsNullSetsPhotoUrlToNull() {
        // Arrange
        RestaurateurUser restaurateur = TestDataUtil.createTestRestaurateurUser();
        DishRequestDTO dishRequestDTO = TestDataUtil.createTestDishRequestDTO();
        dishRequestDTO.setPhotoBase64(null);
        Dish dish = TestDataUtil.createTestDish();
        Restaurant restaurant = TestDataUtil.createTestRestaurant();
        mockSecurityContext(restaurateur);

        when(restaurantRepository.findById(dishRequestDTO.getRestaurantId())).thenReturn(Optional.of(restaurant));
        when(dishMapper.dishRequestDTOToDish(dishRequestDTO)).thenReturn(dish);
        when(dishRepository.save(dish)).thenReturn(dish);
        when(dishHelper.buildDishResponseDTO(dish)).thenReturn(TestDataUtil.createTestDishResponseDTO());

        // Act
        DishResponseDTO responseDTO = dishService.save(dishRequestDTO);

        // Assert
        assertNotNull(responseDTO);
        assertNull(dish.getPhotoUrl());
        verify(dishRepository, times(1)).save(dish);
    }

    @Test
    void saveWhenSaveFailsThrowsEntityCreationException() {
        // Arrange
        RestaurateurUser restaurateur = TestDataUtil.createTestRestaurateurUser();
        DishRequestDTO dishRequestDTO = TestDataUtil.createTestDishRequestDTO();
        Dish dish = TestDataUtil.createTestDish();
        Restaurant restaurant = TestDataUtil.createTestRestaurant();
        mockSecurityContext(restaurateur);

        when(restaurantRepository.findById(dishRequestDTO.getRestaurantId())).thenReturn(Optional.of(restaurant));
        when(dishMapper.dishRequestDTOToDish(dishRequestDTO)).thenReturn(dish);
        doThrow(new RuntimeException()).when(dishRepository).save(dish);

        // Act & Assert
        assertThrows(EntityCreationException.class, () -> dishService.save(dishRequestDTO));
        verify(dishRepository, times(1)).save(dish);
    }

    @Test
    void saveWhenRestaurantDoesNotExistThrowsEntityNotFoundException() {
        // Arrange
        DishRequestDTO dishRequestDTO = TestDataUtil.createTestDishRequestDTO();
        dishRequestDTO.setRestaurantId(0L);

        when(restaurantRepository.findById(dishRequestDTO.getRestaurantId())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> dishService.save(dishRequestDTO));
    }

    @Test
    void saveWhenUserIsAdminReturnsDishResponseDTO() {
        // Arrange
        AdminUser admin = TestDataUtil.createTestAdminUser();
        DishRequestDTO dishRequestDTO = TestDataUtil.createTestDishRequestDTO();
        Dish dish = TestDataUtil.createTestDish();
        Restaurant restaurant = TestDataUtil.createTestRestaurant();
        mockSecurityContext(admin);

        when(restaurantRepository.findById(restaurant.getId())).thenReturn(Optional.of(restaurant));
        when(dishMapper.dishRequestDTOToDish(dishRequestDTO)).thenReturn(dish);
        when(dishRepository.save(dish)).thenReturn(dish);
        when(dishHelper.buildDishResponseDTO(dish)).thenReturn(TestDataUtil.createTestDishResponseDTO());

        // Act
        DishResponseDTO responseDTO = dishService.save(dishRequestDTO);

        // Assert
        assertNotNull(responseDTO);
        verify(dishRepository, times(1)).save(dish);
    }

    @Test
    void saveWhenRestaurateurIsNotRestaurantOwnerThrowsForbiddenRestaurantAccessException() {
        // Arrange
        Dish dish = TestDataUtil.createTestDish();
        DishRequestDTO dishRequestDTO = TestDataUtil.createTestDishRequestDTO();
        RestaurateurUser restaurateur = TestDataUtil.createTestRestaurateurUser();
        RestaurateurUser otherRestaurateur = TestDataUtil.createTestRestaurateurUser();
        Restaurant restaurant = TestDataUtil.createTestRestaurant();
        otherRestaurateur.setId(2L);
        restaurant.setRestaurateur(otherRestaurateur);
        dish.setRestaurant(restaurant);
        mockSecurityContext(restaurateur);

        when(restaurantRepository.findById(dishRequestDTO.getRestaurantId())).thenReturn(Optional.of(restaurant));

        // Act & Assert
        assertThrows(ForbiddenRestaurantAccessException.class, () -> dishService.save(dishRequestDTO));
        verify(dishRepository, never()).save(any(Dish.class));
    }

    @Test
    void findAllWhenDishesExistReturnsDishResponseDTOs() {
        // Arrange
        List<Dish> dishes = List.of(TestDataUtil.createTestDish());
        when(dishRepository.findAll()).thenReturn(dishes);
        when(dishHelper.buildDishResponseDTOs(dishes)).thenReturn(List.of(TestDataUtil.createTestDishResponseDTO()));

        // Act
        List<DishResponseDTO> responseDTOs = dishService.findAll();

        // Assert
        assertNotNull(responseDTOs);
        assertEquals(1, responseDTOs.size());
    }

    @Test
    void findByIdWhenDishExistsReturnsDishResponseDTO() {
        // Arrange
        Dish dish = TestDataUtil.createTestDish();

        when(dishRepository.findById(dish.getId())).thenReturn(Optional.of(dish));
        when(dishHelper.buildDishResponseDTO(dish)).thenReturn(TestDataUtil.createTestDishResponseDTO());

        // Act
        DishResponseDTO responseDTO = dishService.findById(dish.getId());

        // Assert
        assertNotNull(responseDTO);
        verify(dishRepository, times(1)).findById(dish.getId());
    }

    @Test
    void findByIdWhenDishDoesNotExistThrowsEntityNotFoundException() {
        // Arrange
        when(dishRepository.findById(0L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> dishService.findById(0L));
    }

    @Test
    void findAllByRestaurantWhenDishesExistReturnsDishResponseDTOs() {
        // Arrange
        List<Dish> dishes = List.of(TestDataUtil.createTestDish());
        when(dishRepository.findAllByRestaurant_Id(0L)).thenReturn(dishes);
        when(dishHelper.buildDishResponseDTOs(dishes)).thenReturn(List.of(TestDataUtil.createTestDishResponseDTO()));

        // Act
        List<DishResponseDTO> responseDTOs = dishService.findAllByRestaurant(0L);

        // Assert
        assertNotNull(responseDTOs);
        assertEquals(1, responseDTOs.size());
    }

    @Test
    void updateWhenDishExistsUpdatesDishAndReturnsResponseDTO() {
        // Arrange
        DishUpdateRequestDTO updateRequestDTO = TestDataUtil.createTestDishUpdateRequestDTO();
        Dish dish = TestDataUtil.createTestDish();

        when(dishRepository.findById(dish.getId())).thenReturn(Optional.of(dish));
        when(dishRepository.save(dish)).thenReturn(dish);
        when(dishHelper.buildDishResponseDTO(dish)).thenReturn(TestDataUtil.createTestDishResponseDTO());

        // Act
        DishResponseDTO responseDTO = dishService.update(dish.getId(), updateRequestDTO);

        // Assert
        assertNotNull(responseDTO);
        verify(dishRepository, times(1)).save(dish);
    }

    @Test
    void updateWhenDishDoesNotExistThrowsEntityNotFoundException() {
        // Arrange
        DishUpdateRequestDTO updateRequestDTO = TestDataUtil.createTestDishUpdateRequestDTO();

        when(dishRepository.findById(0L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> dishService.update(0L, updateRequestDTO));
    }

    @Test
    void updateWhenSaveFailsThrowsEntityEditException() {
        // Arrange
        DishUpdateRequestDTO updateRequestDTO = TestDataUtil.createTestDishUpdateRequestDTO();
        Dish dish = TestDataUtil.createTestDish();

        when(dishRepository.findById(dish.getId())).thenReturn(Optional.of(dish));
        doThrow(new RuntimeException()).when(dishRepository).save(dish);

        // Act & Assert
        assertThrows(EntityEditException.class, () -> dishService.update(dish.getId(), updateRequestDTO));
        verify(dishRepository, times(1)).save(dish);
    }

    @Test
    void updateWhenUserIsAdminReturnsDishResponseDTO() {
        // Arrange
        AdminUser admin = TestDataUtil.createTestAdminUser();
        DishUpdateRequestDTO updateRequestDTO = TestDataUtil.createTestDishUpdateRequestDTO();
        Dish dish = TestDataUtil.createTestDish();

        mockSecurityContext(admin);

        when(dishRepository.findById(dish.getId())).thenReturn(Optional.of(dish));
        when(dishRepository.save(dish)).thenReturn(dish);
        when(dishHelper.buildDishResponseDTO(dish)).thenReturn(TestDataUtil.createTestDishResponseDTO());

        // Act
        DishResponseDTO responseDTO = dishService.update(dish.getId(), updateRequestDTO);

        // Assert
        assertNotNull(responseDTO);
        verify(dishRepository, times(1)).save(dish);
    }

    @Test
    void updateWhenRestaurateurIsNotRestaurantOwnerThrowsForbiddenRestaurantAccessException() {
        // Arrange
        DishUpdateRequestDTO updateRequestDTO = TestDataUtil.createTestDishUpdateRequestDTO();
        Dish dish = TestDataUtil.createTestDish();
        RestaurateurUser restaurateur = TestDataUtil.createTestRestaurateurUser();
        RestaurateurUser otherRestaurateur = TestDataUtil.createTestRestaurateurUser();
        Restaurant restaurant = TestDataUtil.createTestRestaurant();
        otherRestaurateur.setId(2L);
        restaurant.setRestaurateur(otherRestaurateur);
        dish.setRestaurant(restaurant);
        mockSecurityContext(restaurateur);

        when(dishRepository.findById(dish.getId())).thenReturn(Optional.of(dish));

        // Act & Assert
        assertThrows(ForbiddenRestaurantAccessException.class, () -> dishService.update(dish.getId(), updateRequestDTO));
        verify(dishRepository, never()).save(any(Dish.class));
    }

    @Test
    void removeWhenDishExistsDeletesDish() {
        // Arrange
        RestaurateurUser restaurateur = TestDataUtil.createTestRestaurateurUser();
        Dish dish = TestDataUtil.createTestDish();
        mockSecurityContext(restaurateur);

        when(dishRepository.findById(dish.getId())).thenReturn(Optional.of(dish));

        // Act
        boolean result = dishService.remove(dish.getId());

        // Assert
        assertTrue(result);
        verify(dishRepository, times(1)).save(dish);
    }

    @Test
    void removeWhenDishDoesNotExistThrowsEntityNotFoundException() {
        // Arrange
        when(dishRepository.findById(0L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> dishService.remove(0L));
    }

    @Test
    void removeWhenSaveFailsThrowsEntityDeletionException() {
        // Arrange
        Dish dish = TestDataUtil.createTestDish();
        when(dishRepository.findById(dish.getId())).thenReturn(Optional.of(dish));
        doThrow(new RuntimeException()).when(dishRepository).save(dish);

        // Act & Assert
        assertThrows(EntityDeletionException.class, () -> dishService.remove(dish.getId()));
        verify(dishRepository, times(1)).save(dish);
    }
}