package com.example.foody.service.impl;

import com.example.foody.TestDataUtil;
import com.example.foody.dto.request.UserUpdateRequestDTO;
import com.example.foody.dto.response.UserResponseDTO;
import com.example.foody.exceptions.entity.EntityDeletionException;
import com.example.foody.exceptions.entity.EntityEditException;
import com.example.foody.exceptions.entity.EntityNotFoundException;
import com.example.foody.helper.UserHelper;
import com.example.foody.mapper.UserMapper;
import com.example.foody.model.user.User;
import com.example.foody.repository.UserRepository;
import com.example.foody.service.GoogleDriveService;
import com.example.foody.utils.enums.Role;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private UserHelper userHelper;

    @Mock
    private GoogleDriveService googleDriveService;

    @Test
    void findAllReturnsListOfUserResponseDTO() {
        // Arrange
        List<User> users = List.of(TestDataUtil.createTestCustomerUser());

        when(userRepository.findAll()).thenReturn(users);
        when(userHelper.buildUserResponseDTOs(users))
                .thenReturn(List.of(TestDataUtil.createTestCustomerUserResponseDTO()));

        // Act
        List<UserResponseDTO> responseDTOs = userService.findAll();

        // Assert
        assertNotNull(responseDTOs);
        assertEquals(1, responseDTOs.size());
    }

    @Test
    void findByIdWhenUserExistsReturnsUserResponseDTO() {
        // Arrange
        User user = TestDataUtil.createTestCustomerUser();

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(userHelper.buildUserResponseDTO(user))
                .thenReturn(TestDataUtil.createTestCustomerUserResponseDTO());

        // Act
        UserResponseDTO responseDTO = userService.findById(user.getId());

        // Assert
        assertNotNull(responseDTO);
        verify(userRepository, times(1)).findById(user.getId());
    }

    @Test
    void findByIdWhenUserDoesNotExistThrowsEntityNotFoundException() {
        // Arrange
        when(userRepository.findById(0L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> userService.findById(0L));
    }

    @Test
    void findByEmailWhenUserExistsReturnsUserResponseDTO() {
        // Arrange
        User user = TestDataUtil.createTestCustomerUser();
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(userHelper.buildUserResponseDTO(user))
                .thenReturn(TestDataUtil.createTestCustomerUserResponseDTO());

        // Act
        UserResponseDTO responseDTO = userService.findByEmail(user.getEmail());

        // Assert
        assertNotNull(responseDTO);
        verify(userRepository, times(1)).findByEmail(user.getEmail());
    }

    @Test
    void findByEmailWhenUserDoesNotExistThrowsEntityNotFoundException() {
        // Arrange
        when(userRepository.findByEmail("nonexistent@test.com")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> userService.findByEmail("nonexistent@test.com"));
    }

    @Test
    void findByRoleReturnsListOfUserResponseDTO() {
        // Arrange
        List<User> users = List.of(TestDataUtil.createTestCustomerUser());

        when(userRepository.findByRole(Role.CUSTOMER)).thenReturn(users);
        when(userHelper.buildUserResponseDTOs(users)).thenReturn(List.of(TestDataUtil.createTestCustomerUserResponseDTO()));

        // Act
        List<UserResponseDTO> responseDTOs = userService.findByRole(Role.CUSTOMER);

        // Assert
        assertNotNull(responseDTOs);
        assertEquals(1, responseDTOs.size());
    }

    @Test
    void updateWhenUserExistsUpdatesUserAndReturnsResponseDTO() {
        // Arrange
        User user = TestDataUtil.createTestCustomerUser();
        UserUpdateRequestDTO updateRequestDTO = new UserUpdateRequestDTO();
        updateRequestDTO.setAvatarBase64(null);

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);
        when(userHelper.buildUserResponseDTO(user)).thenReturn(TestDataUtil.createTestCustomerUserResponseDTO());

        // Act
        UserResponseDTO responseDTO = userService.update(user.getId(), updateRequestDTO);

        // Assert
        assertNotNull(responseDTO);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void updateWhenUserDoesNotExistThrowsEntityNotFoundException() {
        // Arrange
        UserUpdateRequestDTO updateRequestDTO = new UserUpdateRequestDTO();

        when(userRepository.findById(0L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> userService.update(0L, updateRequestDTO));
    }

    @Test
    void updateWhenSaveFailsThrowsEntityEditException() {
        // Arrange
        User user = TestDataUtil.createTestCustomerUser();
        UserUpdateRequestDTO updateRequestDTO = new UserUpdateRequestDTO();
        updateRequestDTO.setAvatarBase64(null);

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        doThrow(new RuntimeException()).when(userRepository).save(user);

        // Act & Assert
        assertThrows(EntityEditException.class, () -> userService.update(user.getId(), updateRequestDTO));
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void removeWhenUserExistsDeletesUser() {
        // Arrange
        User user = TestDataUtil.createTestCustomerUser();

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        // Act
        boolean result = userService.remove(user.getId());

        // Assert
        assertTrue(result);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void removeWhenUserDoesNotExistThrowsEntityNotFoundException() {
        // Arrange
        when(userRepository.findById(0L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> userService.remove(0L));
    }

    @Test
    void removeWhenSaveFailsThrowsEntityDeletionException() {
        // Arrange
        User user = TestDataUtil.createTestCustomerUser();

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        doThrow(new RuntimeException()).when(userRepository).save(user);

        // Act & Assert
        assertThrows(EntityDeletionException.class, () -> userService.remove(user.getId()));
        verify(userRepository, times(1)).save(user);
    }
}