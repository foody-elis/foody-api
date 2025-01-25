package com.example.foody.controller;

import com.example.foody.TestDataUtil;
import com.example.foody.dto.request.UserChangePasswordRequestDTO;
import com.example.foody.dto.request.UserLoginRequestDTO;
import com.example.foody.dto.request.UserRequestDTO;
import com.example.foody.dto.response.EmployeeUserResponseDTO;
import com.example.foody.dto.response.TokenResponseDTO;
import com.example.foody.dto.response.UserResponseDTO;
import com.example.foody.service.AuthenticationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthenticationControllerTest {

    @InjectMocks
    private AuthenticationController authenticationController;

    @Mock
    private AuthenticationService authenticationService;

    @Test
    void registerAdminWhenValidRequestReturnsCreatedResponse() {
        // Arrange
        UserRequestDTO requestDTO = TestDataUtil.createTestUserRequestDTO();
        UserResponseDTO responseDTO = TestDataUtil.createTestCustomerUserResponseDTO();

        when(authenticationService.registerAdmin(requestDTO)).thenReturn(responseDTO);

        // Act
        ResponseEntity<UserResponseDTO> response = authenticationController.registerAdmin(requestDTO);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(responseDTO, response.getBody());
    }

    @Test
    void registerModeratorWhenValidRequestReturnsCreatedResponse() {
        // Arrange
        UserRequestDTO requestDTO = TestDataUtil.createTestUserRequestDTO();
        UserResponseDTO responseDTO = TestDataUtil.createTestCustomerUserResponseDTO();

        when(authenticationService.registerModerator(requestDTO)).thenReturn(responseDTO);

        // Act
        ResponseEntity<UserResponseDTO> response = authenticationController.registerModerator(requestDTO);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(responseDTO, response.getBody());
    }

    @Test
    void registerAndAuthenticateRestaurateurWhenValidRequestReturnsCreatedResponse() {
        // Arrange
        UserRequestDTO requestDTO = TestDataUtil.createTestUserRequestDTO();
        TokenResponseDTO responseDTO = TestDataUtil.createTestTokenResponseDTO();

        when(authenticationService.registerAndAuthenticateRestaurateur(requestDTO)).thenReturn(responseDTO);

        // Act
        ResponseEntity<TokenResponseDTO> response = authenticationController.registerAndAuthenticateRestaurateur(requestDTO);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(responseDTO, response.getBody());
    }

    @Test
    void registerCookWhenValidRequestReturnsCreatedResponse() {
        // Arrange
        UserRequestDTO requestDTO = TestDataUtil.createTestUserRequestDTO();
        EmployeeUserResponseDTO responseDTO = TestDataUtil.createTestCookUserResponseDTO();

        when(authenticationService.registerCook(1L, requestDTO)).thenReturn(responseDTO);

        // Act
        ResponseEntity<EmployeeUserResponseDTO> response = authenticationController.registerCook(1L, requestDTO);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(responseDTO, response.getBody());
    }

    @Test
    void registerWaiterWhenValidRequestReturnsCreatedResponse() {
        // Arrange
        UserRequestDTO requestDTO = TestDataUtil.createTestUserRequestDTO();
        EmployeeUserResponseDTO responseDTO = TestDataUtil.createTestCookUserResponseDTO();

        when(authenticationService.registerWaiter(1L, requestDTO)).thenReturn(responseDTO);

        // Act
        ResponseEntity<EmployeeUserResponseDTO> response = authenticationController.registerWaiter(1L, requestDTO);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(responseDTO, response.getBody());
    }

    @Test
    void registerAndAuthenticateCustomerWhenValidRequestReturnsCreatedResponse() {
        // Arrange
        UserRequestDTO requestDTO = TestDataUtil.createTestUserRequestDTO();
        TokenResponseDTO responseDTO = TestDataUtil.createTestTokenResponseDTO();

        when(authenticationService.registerAndAuthenticateCustomer(requestDTO)).thenReturn(responseDTO);

        // Act
        ResponseEntity<TokenResponseDTO> response = authenticationController.registerAndAuthenticateCustomer(requestDTO);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(responseDTO, response.getBody());
    }

    @Test
    void authenticateUserWhenValidRequestReturnsOkResponse() {
        // Arrange
        UserLoginRequestDTO requestDTO = TestDataUtil.createTestUserLoginRequestDTO();
        TokenResponseDTO responseDTO = TestDataUtil.createTestTokenResponseDTO();

        when(authenticationService.authenticate(requestDTO)).thenReturn(responseDTO);

        // Act
        ResponseEntity<TokenResponseDTO> response = authenticationController.authenticateUser(requestDTO);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseDTO, response.getBody());
    }

    @Test
    void getAuthenticatedUserWhenCalledReturnsOkResponse() {
        // Arrange
        UserResponseDTO responseDTO = TestDataUtil.createTestCustomerUserResponseDTO();

        when(authenticationService.getAuthenticatedUser()).thenReturn(responseDTO);

        // Act
        ResponseEntity<UserResponseDTO> response = authenticationController.getAuthenticatedUser();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseDTO, response.getBody());
    }

    @Test
    void changePasswordWhenValidRequestReturnsOkResponse() {
        // Arrange
        UserChangePasswordRequestDTO requestDTO = TestDataUtil.createTestUserChangePasswordRequestDTO();

        doNothing().when(authenticationService).changePassword(requestDTO);

        // Act
        ResponseEntity<Void> response = authenticationController.changePassword(requestDTO);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(authenticationService, times(1)).changePassword(requestDTO);
    }
}