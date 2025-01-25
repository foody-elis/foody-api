package com.example.foody.mapper.impl;

import com.example.foody.TestDataUtil;
import com.example.foody.builder.CustomerUserBuilder;
import com.example.foody.builder.UserBuilder;
import com.example.foody.builder.impl.*;
import com.example.foody.dto.request.UserRequestDTO;
import com.example.foody.dto.request.UserUpdateRequestDTO;
import com.example.foody.dto.response.CustomerUserResponseDTO;
import com.example.foody.dto.response.EmployeeUserResponseDTO;
import com.example.foody.dto.response.UserResponseDTO;
import com.example.foody.model.CreditCard;
import com.example.foody.model.Restaurant;
import com.example.foody.model.user.*;
import com.example.foody.utils.enums.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserMapperImplTest {

    @InjectMocks
    private UserMapperImpl userMapper;

    @Mock
    private UserBuilder<AdminUser> adminUserBuilder;

    @Mock
    private UserBuilder<ModeratorUser> moderatorUserBuilder;

    @Mock
    private UserBuilder<RestaurateurUser> restaurateurUserBuilder;

    @Mock
    private UserBuilder<CookUser> cookUserBuilder;

    @Mock
    private UserBuilder<WaiterUser> waiterUserBuilder;

    @Mock
    private UserBuilder<CustomerUser> customerUserBuilder;

    @Test
    void userToUserResponseDTOWhenUserIsNullReturnsNull() {
        // Act
        UserResponseDTO result = userMapper.userToUserResponseDTO(null, null);

        // Assert
        assertNull(result);
    }

    @Test
    void userToUserResponseDTOWhenEmployeeUserReturnsEmployeeResponseDTO() {
        // Arrange
        EmployeeUser employeeUser = mock(EmployeeUser.class);
        Restaurant restaurant = mock(Restaurant.class);

        when(employeeUser.getId()).thenReturn(1L);
        when(employeeUser.getEmployerRestaurant()).thenReturn(restaurant);
        when(restaurant.getId()).thenReturn(100L);

        // Act
        EmployeeUserResponseDTO result = (EmployeeUserResponseDTO) userMapper.userToUserResponseDTO(employeeUser, "token");

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(100L, result.getEmployerRestaurantId());
        assertEquals("token", result.getFirebaseCustomToken());
    }

    @Test
    void userToUserResponseDTOWhenCustomerUserReturnsCustomerResponseDTO() {
        // Arrange
        CustomerUser customerUser = mock(CustomerUser.class);
        CreditCard creditCard = mock(CreditCard.class);

        when(customerUser.getId()).thenReturn(1L);
        when(customerUser.getCreditCard()).thenReturn(creditCard);
        when(creditCard.getId()).thenReturn(200L);

        // Act
        CustomerUserResponseDTO result = (CustomerUserResponseDTO) userMapper.userToUserResponseDTO(customerUser, "token");

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(200L, result.getCreditCardId());
        assertEquals("token", result.getFirebaseCustomToken());
    }

    @Test
    void userToUserResponseDTOWhenAdminUserReturnsUserResponseDTO() {
        // Arrange
        AdminUser adminUser = mock(AdminUser.class);

        when(adminUser.getId()).thenReturn(1L);

        // Act
        UserResponseDTO result = userMapper.userToUserResponseDTO(adminUser, "token");

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("token", result.getFirebaseCustomToken());
    }

    @Test
    void userToUserResponseDTOWhenEmployerRestaurantIsNullReturnsReturnsUserResponseDTO() {
        // Arrange
        EmployeeUser employeeUser = mock(EmployeeUser.class);

        when(employeeUser.getId()).thenReturn(1L);
        when(employeeUser.getEmployerRestaurant()).thenReturn(null);

        // Act
        UserResponseDTO result = userMapper.userToUserResponseDTO(employeeUser, "token");

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("token", result.getFirebaseCustomToken());
    }

    @Test
    void userToUserResponseDTOWhenCreditCardIsNullReturnsReturnsUserResponseDTO() {
        // Arrange
        CustomerUser customerUser = mock(CustomerUser.class);

        when(customerUser.getId()).thenReturn(1L);
        when(customerUser.getCreditCard()).thenReturn(null);

        // Act
        UserResponseDTO result = userMapper.userToUserResponseDTO(customerUser, "token");

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("token", result.getFirebaseCustomToken());
    }

    @Test
    void userRequestDTOToUserWhenRequestIsNullReturnsNull() {
        // Act
        User result = userMapper.userRequestDTOToUser(null);

        // Assert
        assertNull(result);
    }

    @Test
    void userRequestDTOToUserWhenUserIsAdminReturnsUser() {
        // Arrange
        UserRequestDTO requestDTO = mock(UserRequestDTO.class);
        when(requestDTO.getRole()).thenReturn(Role.Constants.ADMIN_VALUE);
        when(requestDTO.getEmail()).thenReturn("admin@example.com");
        when(requestDTO.getPassword()).thenReturn("adminpassword");
        when(requestDTO.getName()).thenReturn("Admin");
        when(requestDTO.getSurname()).thenReturn("User");
        when(requestDTO.getBirthDate()).thenReturn(null);
        when(requestDTO.getPhoneNumber()).thenReturn("0987654321");

        UserBuilder<AdminUser> adminUserBuilderSpy = spy(new AdminUserBuilderImpl());
        AdminUser adminUser = mock(AdminUser.class);

        doReturn(adminUserBuilderSpy).when(adminUserBuilderSpy).email("admin@example.com");
        doReturn(adminUserBuilderSpy).when(adminUserBuilderSpy).password("adminpassword");
        doReturn(adminUserBuilderSpy).when(adminUserBuilderSpy).name("Admin");
        doReturn(adminUserBuilderSpy).when(adminUserBuilderSpy).surname("User");
        doReturn(adminUserBuilderSpy).when(adminUserBuilderSpy).phoneNumber("0987654321");
        doReturn(adminUser).when(adminUserBuilderSpy).build();

        UserMapperImpl userMapperWithSpy = new UserMapperImpl(
                adminUserBuilderSpy,
                moderatorUserBuilder,
                restaurateurUserBuilder,
                cookUserBuilder,
                waiterUserBuilder,
                customerUserBuilder
        );

        // Act
        User result = userMapperWithSpy.userRequestDTOToUser(requestDTO);

        // Assert
        assertNotNull(result);
        assertEquals(adminUser, result);
        verify(adminUserBuilderSpy).email("admin@example.com");
        verify(adminUserBuilderSpy).password("adminpassword");
        verify(adminUserBuilderSpy).name("Admin");
        verify(adminUserBuilderSpy).surname("User");
        verify(adminUserBuilderSpy).phoneNumber("0987654321");
        verify(adminUserBuilderSpy).build();
    }

    @Test
    void userRequestDTOToUserWhenUserIsModeratorReturnsUser() {
        // Arrange
        UserRequestDTO requestDTO = mock(UserRequestDTO.class);
        when(requestDTO.getRole()).thenReturn(Role.Constants.MODERATOR_VALUE);
        when(requestDTO.getEmail()).thenReturn("moderator@example.com");
        when(requestDTO.getPassword()).thenReturn("moderatorpassword");
        when(requestDTO.getName()).thenReturn("Moderator");
        when(requestDTO.getSurname()).thenReturn("User");
        when(requestDTO.getBirthDate()).thenReturn(null);
        when(requestDTO.getPhoneNumber()).thenReturn("1122334455");

        UserBuilder<ModeratorUser> moderatorUserBuilderSpy = spy(new ModeratorUserBuilderImpl());
        ModeratorUser moderatorUser = mock(ModeratorUser.class);

        doReturn(moderatorUserBuilderSpy).when(moderatorUserBuilderSpy).email("moderator@example.com");
        doReturn(moderatorUserBuilderSpy).when(moderatorUserBuilderSpy).password("moderatorpassword");
        doReturn(moderatorUserBuilderSpy).when(moderatorUserBuilderSpy).name("Moderator");
        doReturn(moderatorUserBuilderSpy).when(moderatorUserBuilderSpy).surname("User");
        doReturn(moderatorUserBuilderSpy).when(moderatorUserBuilderSpy).phoneNumber("1122334455");
        doReturn(moderatorUser).when(moderatorUserBuilderSpy).build();

        UserMapperImpl userMapperWithSpy = new UserMapperImpl(
                adminUserBuilder,
                moderatorUserBuilderSpy,
                restaurateurUserBuilder,
                cookUserBuilder,
                waiterUserBuilder,
                customerUserBuilder
        );

        // Act
        User result = userMapperWithSpy.userRequestDTOToUser(requestDTO);

        // Assert
        assertNotNull(result);
        assertEquals(moderatorUser, result);
        verify(moderatorUserBuilderSpy).email("moderator@example.com");
        verify(moderatorUserBuilderSpy).password("moderatorpassword");
        verify(moderatorUserBuilderSpy).name("Moderator");
        verify(moderatorUserBuilderSpy).surname("User");
        verify(moderatorUserBuilderSpy).phoneNumber("1122334455");
        verify(moderatorUserBuilderSpy).build();
    }

    @Test
    void userRequestDTOToUserWhenUserIsRestaurateurReturnsUser() {
        // Arrange
        UserRequestDTO requestDTO = mock(UserRequestDTO.class);
        when(requestDTO.getRole()).thenReturn(Role.Constants.RESTAURATEUR_VALUE);
        when(requestDTO.getEmail()).thenReturn("restaurateur@example.com");
        when(requestDTO.getPassword()).thenReturn("restaurateurpassword");
        when(requestDTO.getName()).thenReturn("Restaurateur");
        when(requestDTO.getSurname()).thenReturn("User");
        when(requestDTO.getBirthDate()).thenReturn(null);
        when(requestDTO.getPhoneNumber()).thenReturn("2233445566");

        UserBuilder<RestaurateurUser> restaurateurUserBuilderSpy = spy(new RestaurateurUserBuilderImpl());
        RestaurateurUser restaurateurUser = mock(RestaurateurUser.class);

        doReturn(restaurateurUserBuilderSpy).when(restaurateurUserBuilderSpy).email("restaurateur@example.com");
        doReturn(restaurateurUserBuilderSpy).when(restaurateurUserBuilderSpy).password("restaurateurpassword");
        doReturn(restaurateurUserBuilderSpy).when(restaurateurUserBuilderSpy).name("Restaurateur");
        doReturn(restaurateurUserBuilderSpy).when(restaurateurUserBuilderSpy).surname("User");
        doReturn(restaurateurUserBuilderSpy).when(restaurateurUserBuilderSpy).phoneNumber("2233445566");
        doReturn(restaurateurUser).when(restaurateurUserBuilderSpy).build();

        UserMapperImpl userMapperWithSpy = new UserMapperImpl(
                adminUserBuilder,
                moderatorUserBuilder,
                restaurateurUserBuilderSpy,
                cookUserBuilder,
                waiterUserBuilder,
                customerUserBuilder
        );

        // Act
        User result = userMapperWithSpy.userRequestDTOToUser(requestDTO);

        // Assert
        assertNotNull(result);
        assertEquals(restaurateurUser, result);
        verify(restaurateurUserBuilderSpy).email("restaurateur@example.com");
        verify(restaurateurUserBuilderSpy).password("restaurateurpassword");
        verify(restaurateurUserBuilderSpy).name("Restaurateur");
        verify(restaurateurUserBuilderSpy).surname("User");
        verify(restaurateurUserBuilderSpy).phoneNumber("2233445566");
        verify(restaurateurUserBuilderSpy).build();
    }

    @Test
    void userRequestDTOToUserWhenUserIsCookReturnsUser() {
        // Arrange
        UserRequestDTO requestDTO = mock(UserRequestDTO.class);
        when(requestDTO.getRole()).thenReturn(Role.Constants.COOK_VALUE);
        when(requestDTO.getEmail()).thenReturn("cook@example.com");
        when(requestDTO.getPassword()).thenReturn("cookpassword");
        when(requestDTO.getName()).thenReturn("Cook");
        when(requestDTO.getSurname()).thenReturn("User");
        when(requestDTO.getBirthDate()).thenReturn(null);
        when(requestDTO.getPhoneNumber()).thenReturn("3344556677");

        UserBuilder<CookUser> cookUserBuilderSpy = spy(new CookUserBuilderImpl());
        CookUser cookUser = mock(CookUser.class);

        doReturn(cookUserBuilderSpy).when(cookUserBuilderSpy).email("cook@example.com");
        doReturn(cookUserBuilderSpy).when(cookUserBuilderSpy).password("cookpassword");
        doReturn(cookUserBuilderSpy).when(cookUserBuilderSpy).name("Cook");
        doReturn(cookUserBuilderSpy).when(cookUserBuilderSpy).surname("User");
        doReturn(cookUserBuilderSpy).when(cookUserBuilderSpy).phoneNumber("3344556677");
        doReturn(cookUser).when(cookUserBuilderSpy).build();

        UserMapperImpl userMapperWithSpy = new UserMapperImpl(
                adminUserBuilder,
                moderatorUserBuilder,
                restaurateurUserBuilder,
                cookUserBuilderSpy,
                waiterUserBuilder,
                customerUserBuilder
        );

        // Act
        User result = userMapperWithSpy.userRequestDTOToUser(requestDTO);

        // Assert
        assertNotNull(result);
        assertEquals(cookUser, result);
        verify(cookUserBuilderSpy).email("cook@example.com");
        verify(cookUserBuilderSpy).password("cookpassword");
        verify(cookUserBuilderSpy).name("Cook");
        verify(cookUserBuilderSpy).surname("User");
        verify(cookUserBuilderSpy).phoneNumber("3344556677");
        verify(cookUserBuilderSpy).build();
    }

    @Test
    void userRequestDTOToUserWhenUserIsWaiterReturnsUser() {
        // Arrange
        UserRequestDTO requestDTO = mock(UserRequestDTO.class);
        when(requestDTO.getRole()).thenReturn(Role.Constants.WAITER_VALUE);
        when(requestDTO.getEmail()).thenReturn("waiter@example.com");
        when(requestDTO.getPassword()).thenReturn("waiterpassword");
        when(requestDTO.getName()).thenReturn("Waiter");
        when(requestDTO.getSurname()).thenReturn("User");
        when(requestDTO.getBirthDate()).thenReturn(null);
        when(requestDTO.getPhoneNumber()).thenReturn("4455667788");

        UserBuilder<WaiterUser> waiterUserBuilderSpy = spy(new WaiterUserBuilderImpl());
        WaiterUser waiterUser = mock(WaiterUser.class);

        doReturn(waiterUserBuilderSpy).when(waiterUserBuilderSpy).email("waiter@example.com");
        doReturn(waiterUserBuilderSpy).when(waiterUserBuilderSpy).password("waiterpassword");
        doReturn(waiterUserBuilderSpy).when(waiterUserBuilderSpy).name("Waiter");
        doReturn(waiterUserBuilderSpy).when(waiterUserBuilderSpy).surname("User");
        doReturn(waiterUserBuilderSpy).when(waiterUserBuilderSpy).phoneNumber("4455667788");
        doReturn(waiterUser).when(waiterUserBuilderSpy).build();

        UserMapperImpl userMapperWithSpy = new UserMapperImpl(
                adminUserBuilder,
                moderatorUserBuilder,
                restaurateurUserBuilder,
                cookUserBuilder,
                waiterUserBuilderSpy,
                customerUserBuilder
        );

        // Act
        User result = userMapperWithSpy.userRequestDTOToUser(requestDTO);

        // Assert
        assertNotNull(result);
        assertEquals(waiterUser, result);
        verify(waiterUserBuilderSpy).email("waiter@example.com");
        verify(waiterUserBuilderSpy).password("waiterpassword");
        verify(waiterUserBuilderSpy).name("Waiter");
        verify(waiterUserBuilderSpy).surname("User");
        verify(waiterUserBuilderSpy).phoneNumber("4455667788");
        verify(waiterUserBuilderSpy).build();
    }

    @Test
    void userRequestDTOToUserWhenUserIsCustomerReturnsUser() {
        // Arrange
        UserRequestDTO requestDTO = mock(UserRequestDTO.class);
        when(requestDTO.getRole()).thenReturn(Role.Constants.CUSTOMER_VALUE);
        when(requestDTO.getEmail()).thenReturn("test@example.com");
        when(requestDTO.getPassword()).thenReturn("password");
        when(requestDTO.getName()).thenReturn("John");
        when(requestDTO.getSurname()).thenReturn("Doe");
        when(requestDTO.getBirthDate()).thenReturn(null);
        when(requestDTO.getPhoneNumber()).thenReturn("1234567890");

        CustomerUserBuilder customerUserBuilderSpy = spy(new CustomerUserBuilderImpl());
        CustomerUser customerUser = mock(CustomerUser.class);

        doReturn(customerUserBuilderSpy).when(customerUserBuilderSpy).email("test@example.com");
        doReturn(customerUserBuilderSpy).when(customerUserBuilderSpy).password("password");
        doReturn(customerUserBuilderSpy).when(customerUserBuilderSpy).name("John");
        doReturn(customerUserBuilderSpy).when(customerUserBuilderSpy).surname("Doe");
        doReturn(customerUserBuilderSpy).when(customerUserBuilderSpy).phoneNumber("1234567890");
        doReturn(customerUser).when(customerUserBuilderSpy).build();

        UserMapperImpl userMapperWithSpy = new UserMapperImpl(
                adminUserBuilder,
                moderatorUserBuilder,
                restaurateurUserBuilder,
                cookUserBuilder,
                waiterUserBuilder,
                customerUserBuilderSpy
        );

        // Act
        User result = userMapperWithSpy.userRequestDTOToUser(requestDTO);

        // Assert
        assertNotNull(result);
        assertEquals(customerUser, result);
        verify(customerUserBuilderSpy).email("test@example.com");
        verify(customerUserBuilderSpy).password("password");
        verify(customerUserBuilderSpy).name("John");
        verify(customerUserBuilderSpy).surname("Doe");
        verify(customerUserBuilderSpy).phoneNumber("1234567890");
        verify(customerUserBuilderSpy).build();
    }

    @Test
    void userRequestDTOToUserWhenInvalidRoleThrowsIllegalArgumentException() {
        // Arrange
        UserRequestDTO requestDTO = mock(UserRequestDTO.class);
        when(requestDTO.getRole()).thenReturn("INVALID_ROLE");

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> userMapper.userRequestDTOToUser(requestDTO));
    }

    @Test
    void updateUserFromUserUpdateRequestDTOWhenUserIsNullDoesNothing() {
        // Act
        userMapper.updateUserFromUserUpdateRequestDTO(null, null);

        // Assert
        // No exceptions expected
    }

    @Test
    void updateUserFromUserUpdateRequestDTOWhenUserUpdateRequestDTOIsNullDoesNothing() {
        // Arrange
        User user = mock(User.class);

        // Act
        userMapper.updateUserFromUserUpdateRequestDTO(user, null);

        // Assert
        // No exceptions expected
    }

    @Test
    void updateUserFromUserUpdateRequestDTOWhenValidUpdatesUser() {
        // Arrange
        User user = mock(User.class);
        UserUpdateRequestDTO updateRequestDTO = mock(UserUpdateRequestDTO.class);

        when(updateRequestDTO.getName()).thenReturn("Updated Name");
        when(updateRequestDTO.getSurname()).thenReturn("Updated Surname");

        // Act
        userMapper.updateUserFromUserUpdateRequestDTO(user, updateRequestDTO);

        // Assert
        verify(user).setName("Updated Name");
        verify(user).setSurname("Updated Surname");
    }
}