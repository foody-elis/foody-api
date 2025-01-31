package com.example.foody.helper.impl;

import com.example.foody.TestDataUtil;
import com.example.foody.dto.response.CustomerUserResponseDTO;
import com.example.foody.dto.response.UserResponseDTO;
import com.example.foody.exceptions.entity.EntityEditException;
import com.example.foody.mapper.UserMapper;
import com.example.foody.model.user.CustomerUser;
import com.example.foody.repository.UserRepository;
import com.example.foody.service.FirebaseService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link UserHelperImpl} class using mock services.
 */
@ExtendWith(MockitoExtension.class)
public class UserHelperImplTest {

    @InjectMocks
    private UserHelperImpl userHelperImpl;

    @Mock
    private FirebaseService firebaseService;

    @Mock
    private UserMapper userMapper;

    @Mock
    private UserRepository userRepository;

    @Test
    void buildUserResponseDTOWhenValidReturnsUserResponseDTO() {
        // Arrange
        CustomerUser customer = TestDataUtil.createTestCustomerUser();
        String firebaseCustomToken = customer.getFirebaseCustomToken();
        CustomerUserResponseDTO customerUserResponseDTO = TestDataUtil.createTestCustomerUserResponseDTO();

        when(firebaseService.verifyToken(firebaseCustomToken)).thenReturn(true);
        when(userMapper.userToUserResponseDTO(customer, firebaseCustomToken)).thenReturn(customerUserResponseDTO);

        // Act
        UserResponseDTO result = userHelperImpl.buildUserResponseDTO(customer);

        // Assert
        assertNotNull(result);
        assertEquals(customerUserResponseDTO, result);
        verify(userMapper).userToUserResponseDTO(customer, firebaseCustomToken);
        verify(firebaseService).verifyToken(firebaseCustomToken);
    }

    @Test
    void buildUserResponseDTOWhenUserIsNullReturnsNull() {
        // Arrange
        CustomerUser customer = null;

        // Act
        UserResponseDTO result = userHelperImpl.buildUserResponseDTO(customer);

        // Assert
        assertNull(result);
        verifyNoInteractions(firebaseService);
    }

    @Test
    void buildUserResponseDTOWhenFirebaseCustomTokenIsNullReturnsUserResponseDTO() {
        // Arrange
        CustomerUser customer = TestDataUtil.createTestCustomerUser();
        CustomerUserResponseDTO customerUserResponseDTO = TestDataUtil.createTestCustomerUserResponseDTO();
        customer.setFirebaseCustomToken(null);

        when(userMapper.userToUserResponseDTO(customer, null)).thenReturn(customerUserResponseDTO);

        // Act
        UserResponseDTO result = userHelperImpl.buildUserResponseDTO(customer);

        // Assert
        assertNotNull(result);
        assertEquals(customerUserResponseDTO, result);
        verify(userMapper).userToUserResponseDTO(customer, null);
    }

    @Test
    void buildUserResponseDTOWhenFirebaseCustomTokenIsInvalidReturnsUserResponseDTO() {
        // Arrange
        CustomerUser customer = TestDataUtil.createTestCustomerUser();
        CustomerUserResponseDTO customerUserResponseDTO = TestDataUtil.createTestCustomerUserResponseDTO();
        String invalidToken = "invalid-token";

        when(firebaseService.verifyToken(customer.getFirebaseCustomToken())).thenReturn(false);
        when(firebaseService.createCustomToken(customer.getEmail())).thenReturn(invalidToken);
        when(userMapper.userToUserResponseDTO(customer, invalidToken)).thenReturn(customerUserResponseDTO);

        // Act
        UserResponseDTO result = userHelperImpl.buildUserResponseDTO(customer);

        // Assert
        assertNotNull(result);
        assertEquals(customerUserResponseDTO, result);
        verify(userMapper).userToUserResponseDTO(customer, invalidToken);
//        verify(firebaseService).verifyToken(customer.getFirebaseCustomToken());
        verify(firebaseService).createCustomToken(customer.getEmail());
    }

    @Test
    void buildUserResponseDTOWhenUserSaveFailsThrowsEntityEditException() {
        // Arrange
        CustomerUser customer = TestDataUtil.createTestCustomerUser();
        String firebaseCustomToken = customer.getFirebaseCustomToken();

        when(firebaseService.verifyToken(firebaseCustomToken)).thenReturn(false);
        when(firebaseService.createCustomToken(customer.getEmail())).thenReturn(firebaseCustomToken);
        doThrow(new RuntimeException()).when(userRepository).save(customer);

        // Act & Assert
        assertThrows(EntityEditException.class, () -> userHelperImpl.buildUserResponseDTO(customer));
        verifyNoInteractions(userMapper);
        verify(firebaseService).verifyToken(firebaseCustomToken);
        verify(firebaseService).createCustomToken(customer.getEmail());
        verify(userRepository).save(customer);
    }

    @Test
    void buildUserResponseDTOsWhenValidReturnsListOfUserResponseDTO() {
        // Arrange
        CustomerUser customer = TestDataUtil.createTestCustomerUser();
        List<CustomerUser> customers = List.of(customer);
        CustomerUserResponseDTO customerUserResponseDTO = TestDataUtil.createTestCustomerUserResponseDTO();

        when(firebaseService.verifyToken(customer.getFirebaseCustomToken())).thenReturn(true);
        when(userMapper.userToUserResponseDTO(customer, customer.getFirebaseCustomToken())).thenReturn(customerUserResponseDTO);

        // Act
        List<UserResponseDTO> result = userHelperImpl.buildUserResponseDTOs(customers);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(customerUserResponseDTO, result.getFirst());
        verify(userMapper).userToUserResponseDTO(customer, customer.getFirebaseCustomToken());
        verify(firebaseService).verifyToken(customer.getFirebaseCustomToken());
    }
}