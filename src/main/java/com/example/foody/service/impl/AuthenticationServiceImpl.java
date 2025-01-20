package com.example.foody.service.impl;

import com.example.foody.dto.request.UserChangePasswordRequestDTO;
import com.example.foody.dto.request.UserLoginRequestDTO;
import com.example.foody.dto.request.UserRequestDTO;
import com.example.foody.dto.response.CustomerUserResponseDTO;
import com.example.foody.dto.response.EmployeeUserResponseDTO;
import com.example.foody.dto.response.TokenResponseDTO;
import com.example.foody.dto.response.UserResponseDTO;
import com.example.foody.exceptions.auth.InvalidCredentialsException;
import com.example.foody.exceptions.entity.EntityCreationException;
import com.example.foody.exceptions.entity.EntityDuplicateException;
import com.example.foody.exceptions.entity.EntityEditException;
import com.example.foody.exceptions.entity.EntityNotFoundException;
import com.example.foody.exceptions.restaurant.ForbiddenRestaurantAccessException;
import com.example.foody.exceptions.user.InvalidPasswordException;
import com.example.foody.exceptions.user.UserNotActiveException;
import com.example.foody.helper.UserHelper;
import com.example.foody.mapper.UserMapper;
import com.example.foody.model.Restaurant;
import com.example.foody.model.user.EmployeeUser;
import com.example.foody.model.user.User;
import com.example.foody.repository.RestaurantRepository;
import com.example.foody.repository.UserRepository;
import com.example.foody.security.JwtService;
import com.example.foody.service.AuthenticationService;
import com.example.foody.service.EmailService;
import com.example.foody.service.FirebaseService;
import com.example.foody.service.GoogleDriveService;
import com.example.foody.utils.UserRoleUtils;
import com.example.foody.utils.enums.EmailPlaceholder;
import com.example.foody.utils.enums.EmailTemplateType;
import com.example.foody.utils.enums.GoogleDriveFileType;
import com.example.foody.utils.enums.Role;
import jakarta.transaction.Transactional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
@Transactional(rollbackOn = Exception.class)
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;
    private final UserMapper userMapper;
    private final UserHelper userHelper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final GoogleDriveService googleDriveService;
    private final FirebaseService firebaseService;
    private final JwtService jwtService;
    private final EmailService emailService;

    public AuthenticationServiceImpl(UserRepository userRepository, RestaurantRepository restaurantRepository, UserMapper userMapper, UserHelper userHelper, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, GoogleDriveService googleDriveService, FirebaseService firebaseService, JwtService jwtService, EmailService emailService) {
        this.userRepository = userRepository;
        this.restaurantRepository = restaurantRepository;
        this.userMapper = userMapper;
        this.userHelper = userHelper;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.googleDriveService = googleDriveService;
        this.firebaseService = firebaseService;
        this.jwtService = jwtService;
        this.emailService = emailService;
    }

    @Override
    public TokenResponseDTO registerAndAuthenticateRestaurateur(UserRequestDTO userRequestDTO) {
        registerRestaurateur(userRequestDTO);
        return authenticate(userRequestDTO.getEmail(), userRequestDTO.getPassword());
    }

    @Override
    public TokenResponseDTO registerAndAuthenticateCustomer(UserRequestDTO userRequestDTO) {
        registerCustomer(userRequestDTO);
        return authenticate(userRequestDTO.getEmail(), userRequestDTO.getPassword());
    }

    @Override
    public UserResponseDTO registerAdmin(UserRequestDTO userRequestDTO) {
        return registerUserWithRole(userRequestDTO, Role.ADMIN);
    }

    @Override
    public UserResponseDTO registerModerator(UserRequestDTO userRequestDTO) {
        return registerUserWithRole(userRequestDTO, Role.MODERATOR);
    }

    @Override
    public UserResponseDTO registerRestaurateur(UserRequestDTO userRequestDTO) {
        return registerUserWithRole(userRequestDTO, Role.RESTAURATEUR);
    }

    @Override
    public EmployeeUserResponseDTO registerCook(long restaurantId, UserRequestDTO userRequestDTO) {
        return registerEmployeeUser(restaurantId, userRequestDTO, Role.COOK);
    }

    @Override
    public EmployeeUserResponseDTO registerWaiter(long restaurantId, UserRequestDTO userRequestDTO) {
        return registerEmployeeUser(restaurantId, userRequestDTO, Role.WAITER);
    }

    @Override
    public CustomerUserResponseDTO registerCustomer(UserRequestDTO userRequestDTO) {
        return (CustomerUserResponseDTO) registerUserWithRole(userRequestDTO, Role.CUSTOMER);
    }

    @Override
    public TokenResponseDTO authenticate(UserLoginRequestDTO userLoginRequestDTO) {
        return authenticate(userLoginRequestDTO.getEmail(), userLoginRequestDTO.getPassword());
    }

    @Override
    public TokenResponseDTO authenticate(String email, String password) {
        try {
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(email, password);
            authenticationManager.authenticate(authentication);
        } catch (AuthenticationException e) {
            throw new InvalidCredentialsException();
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("user", "email", email));

        if (!user.isActive()) throw new UserNotActiveException(user.getEmail());

        String accessToken = jwtService.generateToken(user);

        return new TokenResponseDTO(accessToken);
    }

    @Override
    public UserResponseDTO getAuthenticatedUser() {
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userHelper.buildUserResponseDTO(principal);
    }

    @Override
    public void changePassword(UserChangePasswordRequestDTO userChangePasswordRequestDTO) {
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!passwordEncoder.matches(userChangePasswordRequestDTO.getCurrentPassword(), principal.getPassword())) {
            throw new InvalidPasswordException();
        }

        principal.setPassword(passwordEncoder.encode(userChangePasswordRequestDTO.getNewPassword()));

        try {
            userRepository.save(principal);
        } catch (Exception e) {
            throw new EntityEditException("user", "id", principal.getId());
        }

        sendChangePasswordEmail(principal);
    }

    private <T extends User> UserResponseDTO registerUserWithRole(UserRequestDTO userRequestDTO, Role role) {
        userRequestDTO.setRole(role.name());

        T user = (T) userMapper.userRequestDTOToUser(userRequestDTO);
        user = saveUser(user, userRequestDTO.getAvatarBase64());

        return userHelper.buildUserResponseDTO(user);
    }

    private <E extends EmployeeUser> EmployeeUserResponseDTO registerEmployeeUser(long restaurantId, UserRequestDTO userRequestDTO, Role role) {
        userRequestDTO.setRole(role.name());

        E employee = (E) userMapper.userRequestDTOToUser(userRequestDTO);
        employee = saveEmployeeUser(restaurantId, employee, userRequestDTO.getAvatarBase64());

        return (EmployeeUserResponseDTO) userHelper.buildUserResponseDTO(employee);
    }

    private <E extends EmployeeUser> E saveEmployeeUser(long restaurantId, E employeeUser, String avatarBase64) {
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Restaurant restaurant = restaurantRepository
                .findById(restaurantId)
                .orElseThrow(() -> new EntityNotFoundException("restaurant", "id", restaurantId));

        checkRestaurantAccessOrThrow(principal, restaurant);

        employeeUser.setEmployerRestaurant(restaurant);
        employeeUser = saveUser(employeeUser, avatarBase64);

        return employeeUser;
    }

    private <T extends User> T saveUser(T user, String avatarBase64) {
        String avatarUrl = saveUserAvatar(avatarBase64);

        user.setAvatarUrl(avatarUrl);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setFirebaseCustomToken(firebaseService.createCustomToken(user.getEmail()));

        try {
            user = userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            removeUserAvatar(user);
            throw new EntityDuplicateException("user", "email", user.getEmail());
        } catch (Exception e) {
            removeUserAvatar(user);
            throw new EntityCreationException("user");
        }

        sendRegistrationEmail(user);

        return user;
    }

    private String saveUserAvatar(String userAvatarBase64) {
        return Optional.ofNullable(userAvatarBase64)
                .map(avatarBase64 -> googleDriveService.uploadBase64Image(avatarBase64, GoogleDriveFileType.USER_AVATAR))
                .orElse(null);
    }

    private void removeUserAvatar(User user) {
        Optional.ofNullable(user.getAvatarUrl())
                .ifPresent(googleDriveService::deleteImage);
    }

    private void sendRegistrationEmail(User user) {
        Map<EmailPlaceholder, Object> variables = Map.of(
                EmailPlaceholder.NAME, user.getName(),
                EmailPlaceholder.SURNAME, user.getSurname()
        );
        emailService.sendTemplatedEmail(
                user.getEmail(),
                EmailTemplateType.USER_REGISTRATION,
                variables
        );
    }

    private void checkRestaurantAccessOrThrow(User user, Restaurant restaurant) {
        if (UserRoleUtils.isAdmin(user)) return;
        if (restaurant.getRestaurateur().getId() == user.getId()) return;

        throw new ForbiddenRestaurantAccessException();
    }

    private void sendChangePasswordEmail(User user) {
        Map<EmailPlaceholder, Object> variables = Map.of(
                EmailPlaceholder.NAME, user.getName(),
                EmailPlaceholder.SURNAME, user.getSurname()
        );
        emailService.sendTemplatedEmail(
                user.getEmail(),
                EmailTemplateType.CHANGE_PASSWORD,
                variables
        );
    }

    // todo implement the refresh token mechanism
    // todo implement the logout mechanism
}