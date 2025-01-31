package com.example.foody.service.impl;

import com.example.foody.TestDataUtil;
import com.example.foody.dto.request.UserChangePasswordRequestDTO;
import com.example.foody.dto.request.UserLoginRequestDTO;
import com.example.foody.dto.request.UserRequestDTO;
import com.example.foody.dto.response.EmployeeUserResponseDTO;
import com.example.foody.dto.response.TokenResponseDTO;
import com.example.foody.dto.response.UserResponseDTO;
import com.example.foody.exceptions.auth.InvalidCredentialsException;
import com.example.foody.exceptions.entity.EntityDuplicateException;
import com.example.foody.exceptions.restaurant.ForbiddenRestaurantAccessException;
import com.example.foody.exceptions.user.InvalidPasswordException;
import com.example.foody.exceptions.user.UserNotActiveException;
import com.example.foody.helper.UserHelper;
import com.example.foody.mapper.UserMapper;
import com.example.foody.model.Restaurant;
import com.example.foody.model.user.*;
import com.example.foody.repository.RestaurantRepository;
import com.example.foody.repository.UserRepository;
import com.example.foody.security.JwtService;
import com.example.foody.service.EmailService;
import com.example.foody.service.FirebaseService;
import com.example.foody.service.GoogleDriveService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link AuthenticationServiceImpl} class using mock services.
 */
@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceImplTest {

    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private UserHelper userHelper;

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    @Mock
    private GoogleDriveService googleDriveService;

    @Mock
    private FirebaseService firebaseService;

    @Mock
    private EmailService emailService;

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
    void registerAndAuthenticateRestaurateurWhenRestaurateurIsValidReturnsTokenResponseDTO() {
        // Arrange
        UserRequestDTO userRequestDTO = TestDataUtil.createTestUserRequestDTO();
        RestaurateurUser restaurateur = TestDataUtil.createTestRestaurateurUser();
        restaurateur.setEmail(userRequestDTO.getEmail());

        when(userMapper.userRequestDTOToUser(userRequestDTO)).thenReturn(restaurateur);
        when(firebaseService.createCustomToken(restaurateur.getEmail())).thenReturn("firebase-token");
        when(passwordEncoder.encode(userRequestDTO.getPassword())).thenReturn("encoded-password");
        when(userRepository.save(any(User.class))).thenReturn(restaurateur);
        when(userRepository.findByEmail(userRequestDTO.getEmail())).thenReturn(Optional.of(restaurateur));
        when(jwtService.generateToken(restaurateur)).thenReturn("jwt-token");

        // Act
        TokenResponseDTO tokenResponseDTO = authenticationService.registerAndAuthenticateRestaurateur(userRequestDTO);

        // Assert
        assertNotNull(tokenResponseDTO);
        assertEquals("jwt-token", tokenResponseDTO.getAccessToken());
        verify(firebaseService, times(1)).createCustomToken(restaurateur.getEmail());
        verify(passwordEncoder, times(1)).encode(userRequestDTO.getPassword());
        verify(userRepository, times(1)).save(restaurateur);
        verify(userRepository, times(1)).findByEmail(userRequestDTO.getEmail());
        verify(jwtService, times(1)).generateToken(restaurateur);
    }

    @Test
    void registerAndAuthenticateCustomerWhenCustomerIsValidReturnsTokenResponseDTO() {
        // Arrange
        UserRequestDTO userRequestDTO = TestDataUtil.createTestUserRequestDTO();
        CustomerUser customer = TestDataUtil.createTestCustomerUser();
        customer.setEmail(userRequestDTO.getEmail());

        when(userMapper.userRequestDTOToUser(userRequestDTO)).thenReturn(customer);
        when(passwordEncoder.encode(userRequestDTO.getPassword())).thenReturn("encoded-password");
        when(userRepository.save(any(User.class))).thenReturn(customer);
        when(userRepository.findByEmail(userRequestDTO.getEmail())).thenReturn(Optional.of(customer));
        when(jwtService.generateToken(customer)).thenReturn("jwt-token");

        // Act
        TokenResponseDTO tokenResponseDTO = authenticationService.registerAndAuthenticateCustomer(userRequestDTO);

        // Assert
        assertNotNull(tokenResponseDTO);
        assertEquals("jwt-token", tokenResponseDTO.getAccessToken());
        verify(passwordEncoder, times(1)).encode(userRequestDTO.getPassword());
        verify(userRepository, times(1)).save(customer);
        verify(userRepository, times(1)).findByEmail(userRequestDTO.getEmail());
        verify(jwtService, times(1)).generateToken(customer);
    }

    @Test
    void registerAdminWhenAdminIsValidReturnsTokenResponseDTO() {
        // Arrange
        UserRequestDTO userRequestDTO = TestDataUtil.createTestUserRequestDTO();
        AdminUser admin = TestDataUtil.createTestAdminUser();
        UserResponseDTO adminUserResponseDTO = TestDataUtil.createTestAdminUserResponseDTO();

        when(userMapper.userRequestDTOToUser(userRequestDTO)).thenReturn(admin);
        when(firebaseService.createCustomToken(admin.getEmail())).thenReturn("firebase-token");
        when(passwordEncoder.encode(userRequestDTO.getPassword())).thenReturn("encoded-password");
        when(userRepository.save(any(User.class))).thenReturn(admin);
        when(userHelper.buildUserResponseDTO(admin)).thenReturn(adminUserResponseDTO);

        // Act
        UserResponseDTO userResponseDTO = authenticationService.registerAdmin(userRequestDTO);

        // Assert
        assertNotNull(userResponseDTO);
        assertEquals(admin.getEmail(), userResponseDTO.getEmail());
        verify(firebaseService, times(1)).createCustomToken(admin.getEmail());
        verify(passwordEncoder, times(1)).encode(userRequestDTO.getPassword());
        verify(userRepository, times(1)).save(admin);
        verify(userHelper, times(1)).buildUserResponseDTO(admin);
    }

    @Test
    void registerModeratorWhenModeratorIsValidReturnsTokenResponseDTO() {
        // Arrange
        UserRequestDTO userRequestDTO = TestDataUtil.createTestUserRequestDTO();
        ModeratorUser moderator = TestDataUtil.createTestModeratorUser();
        UserResponseDTO moderatorUserResponseDTO = TestDataUtil.createTestModeratorUserResponseDTO();

        when(userMapper.userRequestDTOToUser(userRequestDTO)).thenReturn(moderator);
        when(firebaseService.createCustomToken(moderator.getEmail())).thenReturn("firebase-token");
        when(passwordEncoder.encode(userRequestDTO.getPassword())).thenReturn("encoded-password");
        when(userRepository.save(any(User.class))).thenReturn(moderator);
        when(userHelper.buildUserResponseDTO(moderator)).thenReturn(moderatorUserResponseDTO);

        // Act
        UserResponseDTO userResponseDTO = authenticationService.registerModerator(userRequestDTO);

        // Assert
        assertNotNull(userResponseDTO);
        assertEquals(moderator.getEmail(), userResponseDTO.getEmail());
        verify(firebaseService, times(1)).createCustomToken(moderator.getEmail());
        verify(passwordEncoder, times(1)).encode(userRequestDTO.getPassword());
        verify(userRepository, times(1)).save(moderator);
        verify(userHelper, times(1)).buildUserResponseDTO(moderator);
    }

    @Test
    void registerCookWhenCookIsValidReturnsEmployeeUserResponseDTO() {
        // Arrange
        UserRequestDTO userRequestDTO = TestDataUtil.createTestUserRequestDTO();
        RestaurateurUser restaurateur = TestDataUtil.createTestRestaurateurUser();
        CookUser cookUser = TestDataUtil.createTestCookUser();
        Restaurant restaurant = TestDataUtil.createTestRestaurant();
        mockSecurityContext(restaurateur);

        when(userMapper.userRequestDTOToUser(userRequestDTO)).thenReturn(cookUser);
        when(restaurantRepository.findById(restaurant.getId())).thenReturn(Optional.of(restaurant));
        when(userRepository.save(any(CookUser.class))).thenReturn(cookUser);
        when(userHelper.buildUserResponseDTO(cookUser)).thenReturn(TestDataUtil.createTestCookUserResponseDTO());

        // Act
        EmployeeUserResponseDTO employeeUserResponseDTO = authenticationService.registerCook(restaurant.getId(), userRequestDTO);

        // Assert
        assertNotNull(employeeUserResponseDTO);
        assertEquals(cookUser.getEmail(), employeeUserResponseDTO.getEmail());
        verify(restaurantRepository, times(1)).findById(restaurant.getId());
        verify(userRepository, times(1)).save(cookUser);
        verify(userHelper, times(1)).buildUserResponseDTO(cookUser);
    }

    @Test
    void registerCookWhenUserIsAdminReturnsEmployeeUserResponseDTO() {
        // Arrange
        UserRequestDTO userRequestDTO = TestDataUtil.createTestUserRequestDTO();
        AdminUser admin = TestDataUtil.createTestAdminUser();
        CookUser cookUser = TestDataUtil.createTestCookUser();
        Restaurant restaurant = TestDataUtil.createTestRestaurant();
        mockSecurityContext(admin);

        when(userMapper.userRequestDTOToUser(userRequestDTO)).thenReturn(cookUser);
        when(restaurantRepository.findById(restaurant.getId())).thenReturn(Optional.of(restaurant));
        when(userRepository.save(any(CookUser.class))).thenReturn(cookUser);
        when(userHelper.buildUserResponseDTO(cookUser)).thenReturn(TestDataUtil.createTestCookUserResponseDTO());

        // Act
        EmployeeUserResponseDTO employeeUserResponseDTO = authenticationService.registerCook(restaurant.getId(), userRequestDTO);

        // Assert
        assertNotNull(employeeUserResponseDTO);
        assertEquals(cookUser.getEmail(), employeeUserResponseDTO.getEmail());
        verify(restaurantRepository, times(1)).findById(restaurant.getId());
        verify(userRepository, times(1)).save(cookUser);
        verify(userHelper, times(1)).buildUserResponseDTO(cookUser);
    }

    @Test
    void registerCookWhenRestaurateurIsNotRestaurantOwnerThrowsForbiddenRestaurantAccessException() {
        // Arrange
        UserRequestDTO userRequestDTO = TestDataUtil.createTestUserRequestDTO();
        CookUser cookUser = TestDataUtil.createTestCookUser();
        RestaurateurUser restaurateur = TestDataUtil.createTestRestaurateurUser();
        RestaurateurUser otherRestaurateur = TestDataUtil.createTestRestaurateurUser();
        Restaurant restaurant = TestDataUtil.createTestRestaurant();
        otherRestaurateur.setId(2L);
        restaurant.setRestaurateur(otherRestaurateur);
        cookUser.setEmployerRestaurant(restaurant);
        mockSecurityContext(restaurateur);

        when(userMapper.userRequestDTOToUser(userRequestDTO)).thenReturn(cookUser);
        when(restaurantRepository.findById(restaurant.getId())).thenReturn(Optional.of(restaurant));

        // Act & Assert
        assertThrows(ForbiddenRestaurantAccessException.class, () -> authenticationService.registerCook(restaurant.getId(), userRequestDTO));
        verify(userRepository, never()).save(cookUser);
    }

    @Test
    void registerWaiterWhenWaiterIsValidReturnsEmployeeUserResponseDTO() {
        // Arrange
        UserRequestDTO userRequestDTO = TestDataUtil.createTestUserRequestDTO();
        RestaurateurUser restaurateur = TestDataUtil.createTestRestaurateurUser();
        WaiterUser waiterUser = TestDataUtil.createTestWaiterUser();
        Restaurant restaurant = TestDataUtil.createTestRestaurant();
        mockSecurityContext(restaurateur);

        when(userMapper.userRequestDTOToUser(userRequestDTO)).thenReturn(waiterUser);
        when(restaurantRepository.findById(restaurant.getId())).thenReturn(Optional.of(restaurant));
        when(userRepository.save(any(WaiterUser.class))).thenReturn(waiterUser);
        when(userHelper.buildUserResponseDTO(waiterUser)).thenReturn(TestDataUtil.createTestWaiterUserResponseDTO());

        // Act
        EmployeeUserResponseDTO employeeUserResponseDTO = authenticationService.registerWaiter(restaurant.getId(), userRequestDTO);

        // Assert
        assertNotNull(employeeUserResponseDTO);
        assertEquals(waiterUser.getEmail(), employeeUserResponseDTO.getEmail());
        verify(restaurantRepository, times(1)).findById(restaurant.getId());
        verify(userRepository, times(1)).save(waiterUser);
        verify(userHelper, times(1)).buildUserResponseDTO(waiterUser);
    }

    @Test
    void registerCustomerWhenCustomerIsValidReturnsCustomerUserResponseDTO() {
        // Arrange
        UserRequestDTO userRequestDTO = TestDataUtil.createTestUserRequestDTO();
        CustomerUser customer = TestDataUtil.createTestCustomerUser();
        UserResponseDTO customerUserResponseDTO = TestDataUtil.createTestCustomerUserResponseDTO();

        when(userMapper.userRequestDTOToUser(userRequestDTO)).thenReturn(customer);
        when(passwordEncoder.encode(userRequestDTO.getPassword())).thenReturn("encoded-password");
        when(userRepository.save(any(User.class))).thenReturn(customer);
        when(userHelper.buildUserResponseDTO(customer)).thenReturn(customerUserResponseDTO);

        // Act
        UserResponseDTO userResponseDTO = authenticationService.registerCustomer(userRequestDTO);

        // Assert
        assertNotNull(userResponseDTO);
        assertEquals(customer.getEmail(), userResponseDTO.getEmail());
        verify(passwordEncoder, times(1)).encode(userRequestDTO.getPassword());
        verify(userRepository, times(1)).save(customer);
        verify(userHelper, times(1)).buildUserResponseDTO(customer);
    }

    @Test
    void registerCustomerWhenCustomerEmailExistsThrowsEntityExistsException() {
        // Arrange
        UserRequestDTO userRequest = TestDataUtil.createTestUserRequestDTO();
        User user = TestDataUtil.createTestCustomerUser();

        when(userMapper.userRequestDTOToUser(userRequest)).thenReturn(user);
        doThrow(new DataIntegrityViolationException("Duplicate entry"))
                .when(userRepository).save(user);

        // Act & Assert
        assertThrows(EntityDuplicateException.class, () -> authenticationService.registerAndAuthenticateCustomer(userRequest));
    }

    @Test
    void registerCustomerWhenSaveFailsThrowsEntityCreationException() {
        // Arrange
        UserRequestDTO userRequestDTO = TestDataUtil.createTestUserRequestDTO();
        CustomerUser customer = TestDataUtil.createTestCustomerUser();

        when(userMapper.userRequestDTOToUser(userRequestDTO)).thenReturn(customer);
        when(passwordEncoder.encode(userRequestDTO.getPassword())).thenReturn("encoded-password");
        when(userRepository.save(any(User.class))).thenThrow(new RuntimeException());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> authenticationService.registerCustomer(userRequestDTO));
    }

    @Test
    void authenticateWhenCredentialsAreValidReturnsTokenResponseDTO() {
        // Arrange
        User user = TestDataUtil.createTestCustomerUser();
        UserLoginRequestDTO userLoginRequestDTO = TestDataUtil.createTestUserLoginRequestDTO();
        userLoginRequestDTO.setEmail(user.getEmail());

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(jwtService.generateToken(user)).thenReturn("test-token");

        // Act
        TokenResponseDTO tokenResponseDTO = authenticationService.authenticate(userLoginRequestDTO);

        // Assert
        assertNotNull(tokenResponseDTO);
        assertEquals("test-token", tokenResponseDTO.getAccessToken());
    }

    @Test
    void authenticateWhenAuthenticationManagerFailsThrowsInvalidCredentialsException() {
        // Arrange
        UserLoginRequestDTO userLoginRequestDTO = new UserLoginRequestDTO();
        userLoginRequestDTO.setEmail("user@test.com");
        userLoginRequestDTO.setPassword("wrong-password");

        doThrow(new BadCredentialsException("Bad credentials")).when(authenticationManager)
                .authenticate(any(UsernamePasswordAuthenticationToken.class));

        // Act & Assert
        assertThrows(InvalidCredentialsException.class, () -> authenticationService.authenticate(userLoginRequestDTO));
    }

    @Test
    void authenticateWhenUserIsNotActiveThrowsUserNotActiveException() {
        // Arrange
        User user = TestDataUtil.createTestCustomerUser();
        user.setActive(false);

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        // Act & Assert
        assertThrows(UserNotActiveException.class, () -> authenticationService.authenticate(user.getEmail(), user.getPassword()));
    }

    @Test
    void getAuthenticatedUserReturnsUserResponseDTO() {
        // Arrange
        User user = TestDataUtil.createTestCustomerUser();
        UserResponseDTO userResponseDTO = TestDataUtil.createTestCustomerUserResponseDTO();
        mockSecurityContext(user);

        when(userHelper.buildUserResponseDTO(user)).thenReturn(userResponseDTO);

        // Act
        UserResponseDTO result = authenticationService.getAuthenticatedUser();

        // Assert
        assertEquals(userResponseDTO, result);
    }

    @Test
    void changePasswordWhenCurrentPasswordIsValidUpdatesPassword() {
        // Arrange
        User user = TestDataUtil.createTestCustomerUser();
        UserChangePasswordRequestDTO changePasswordRequest = new UserChangePasswordRequestDTO();
        changePasswordRequest.setCurrentPassword(user.getPassword());
        changePasswordRequest.setNewPassword("newPassword123");
        mockSecurityContext(user);

        when(passwordEncoder.matches(user.getPassword(), user.getPassword())).thenReturn(true);
        when(passwordEncoder.encode("newPassword123")).thenReturn("encodedNewPassword");

        // Act
        authenticationService.changePassword(changePasswordRequest);

        // Assert
        verify(userRepository, times(1)).save(user);
        assertEquals("encodedNewPassword", user.getPassword());
    }

    @Test
    void changePasswordWhenCurrentPasswordIsInvalidThrowsInvalidPasswordException() {
        // Arrange
        User user = TestDataUtil.createTestCustomerUser();
        UserChangePasswordRequestDTO changePasswordRequest = new UserChangePasswordRequestDTO();
        changePasswordRequest.setCurrentPassword("wrong-password");
        changePasswordRequest.setNewPassword("newPassword123");
        mockSecurityContext(user);

        when(passwordEncoder.matches("wrong-password", user.getPassword())).thenReturn(false);

        // Act & Assert
        assertThrows(InvalidPasswordException.class, () -> authenticationService.changePassword(changePasswordRequest));
    }

    @Test
    void changePasswordWhenSaveFailsThrowsEntityEditException() {
        // Arrange
        User user = TestDataUtil.createTestCustomerUser();
        UserChangePasswordRequestDTO changePasswordRequest = new UserChangePasswordRequestDTO();
        changePasswordRequest.setCurrentPassword(user.getPassword());
        changePasswordRequest.setNewPassword("newPassword123");

        when(passwordEncoder.matches(user.getPassword(), user.getPassword())).thenReturn(true);
        when(passwordEncoder.encode("newPassword123")).thenReturn("encodedNewPassword");
        mockSecurityContext(user);
        doThrow(new RuntimeException()).when(userRepository).save(user);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> authenticationService.changePassword(changePasswordRequest));
    }
}