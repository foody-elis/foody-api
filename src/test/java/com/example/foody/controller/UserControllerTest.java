package com.example.foody.controller;

import com.example.foody.TestDataUtil;
import com.example.foody.dto.request.UserUpdateRequestDTO;
import com.example.foody.dto.response.UserResponseDTO;
import com.example.foody.exceptions.entity.EntityNotFoundException;
import com.example.foody.model.user.CustomerUser;
import com.example.foody.service.UserService;
import com.example.foody.utils.enums.Role;
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
public class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @Test
    void getUsersWhenCalledReturnsOkResponse() {
        // Arrange
        List<UserResponseDTO> responseDTOs =
                Collections.singletonList(TestDataUtil.createTestCustomerUserResponseDTO());

        when(userService.findAll()).thenReturn(responseDTOs);

        // Act
        ResponseEntity<List<UserResponseDTO>> response = userController.getUsers();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseDTOs, response.getBody());
    }

    @Test
    void getUserByIdWhenValidIdReturnsOkResponse() {
        // Arrange
        UserResponseDTO responseDTO = TestDataUtil.createTestCustomerUserResponseDTO();

        when(userService.findById(1L)).thenReturn(responseDTO);

        // Act
        ResponseEntity<UserResponseDTO> response = userController.getUserById(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseDTO, response.getBody());
    }

    @Test
    void getUserByIdWhenInvalidIdThrowsEntityNotFoundException() {
        // Arrange
        when(userService.findById(1L)).thenThrow(new EntityNotFoundException("user", "id", 1L));

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> userController.getUserById(1L));
    }

    @Test
    void getUserByEmailWhenValidEmailReturnsOkResponse() {
        // Arrange
        UserResponseDTO responseDTO = TestDataUtil.createTestCustomerUserResponseDTO();

        when(userService.findByEmail("test@example.com")).thenReturn(responseDTO);

        // Act
        ResponseEntity<UserResponseDTO> response = userController.getUserByEmail("test@example.com");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseDTO, response.getBody());
    }

    @Test
    void getUserByEmailWhenInvalidEmailThrowsEntityNotFoundException() {
        // Arrange
        when(userService.findByEmail("invalid@example.com"))
                .thenThrow(new EntityNotFoundException("user", "email", "invalid@example.com"));

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> userController.getUserByEmail("invalid@example.com"));
    }

    @Test
    void getUsersByRoleWhenValidRoleReturnsOkResponse() {
        // Arrange
        List<UserResponseDTO> responseDTOs =
                Collections.singletonList(TestDataUtil.createTestCustomerUserResponseDTO());

        when(userService.findByRole(Role.ADMIN)).thenReturn(responseDTOs);

        // Act
        ResponseEntity<List<UserResponseDTO>> response = userController.getUsersByRole("ADMIN");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseDTOs, response.getBody());
    }

    @Test
    void updateUserWhenValidRequestReturnsOkResponse() {
        // Arrange
        UserUpdateRequestDTO requestDTO = TestDataUtil.createTestUserUpdateRequestDTO();
        UserResponseDTO responseDTO = TestDataUtil.createTestCustomerUserResponseDTO();
        CustomerUser authenticatedUser = TestDataUtil.createTestCustomerUser();

        when(userService.update(authenticatedUser.getId(), requestDTO)).thenReturn(responseDTO);

        // Act
        ResponseEntity<UserResponseDTO> response =
                userController.updateUser(authenticatedUser, requestDTO);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseDTO, response.getBody());
    }

    @Test
    void removeUserWhenValidIdRemovesUser() {
        // Arrange
        when(userService.remove(1L)).thenReturn(true);

        // Act
        ResponseEntity<Void> response = userController.removeUser(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(userService, times(1)).remove(1L);
    }
}