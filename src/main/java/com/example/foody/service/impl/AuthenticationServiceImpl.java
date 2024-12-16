package com.example.foody.service.impl;

import com.example.foody.dto.request.UserLoginRequestDTO;
import com.example.foody.dto.request.UserRequestDTO;
import com.example.foody.dto.response.CustomerUserResponseDTO;
import com.example.foody.dto.response.EmployeeUserResponseDTO;
import com.example.foody.dto.response.TokenResponseDTO;
import com.example.foody.dto.response.UserResponseDTO;
import com.example.foody.exceptions.auth.InvalidCredentialsException;
import com.example.foody.exceptions.entity.EntityCreationException;
import com.example.foody.exceptions.entity.EntityDuplicateException;
import com.example.foody.exceptions.entity.EntityNotFoundException;
import com.example.foody.exceptions.restaurant.ForbiddenRestaurantAccessException;
import com.example.foody.exceptions.user.UserNotActiveException;
import com.example.foody.mapper.UserMapper;
import com.example.foody.model.Restaurant;
import com.example.foody.model.user.*;
import com.example.foody.repository.RestaurantRepository;
import com.example.foody.repository.UserRepository;
import com.example.foody.security.JwtService;
import com.example.foody.service.AuthenticationService;
import com.example.foody.service.GoogleDriveService;
import com.example.foody.utils.UserRoleUtils;
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

import java.util.Optional;

@Service
@Transactional(rollbackOn = Exception.class)
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;
    private final UserMapper<User> userMapper;
    private final UserMapper<AdminUser> adminUserMapper;
    private final UserMapper<ModeratorUser> moderatorUserMapper;
    private final UserMapper<RestaurateurUser> restaurateurUserMapper;
    private final UserMapper<CookUser> cookUserMapper;
    private final UserMapper<WaiterUser> waiterUserMapper;
    private final UserMapper<CustomerUser> customerUserMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final GoogleDriveService googleDriveService;
    private final JwtService jwtService;

    public AuthenticationServiceImpl(UserRepository userRepository, RestaurantRepository restaurantRepository, UserMapper<User> userMapper, UserMapper<AdminUser> adminUserMapper, UserMapper<ModeratorUser> moderatorUserMapper, UserMapper<RestaurateurUser> restaurateurUserMapper, UserMapper<CookUser> cookUserMapper, UserMapper<WaiterUser> waiterUserMapper, UserMapper<CustomerUser> customerUserMapper, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, GoogleDriveService googleDriveService, JwtService jwtService) {
        this.userRepository = userRepository;
        this.restaurantRepository = restaurantRepository;
        this.userMapper = userMapper;
        this.adminUserMapper = adminUserMapper;
        this.moderatorUserMapper = moderatorUserMapper;
        this.restaurateurUserMapper = restaurateurUserMapper;
        this.cookUserMapper = cookUserMapper;
        this.waiterUserMapper = waiterUserMapper;
        this.customerUserMapper = customerUserMapper;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.googleDriveService = googleDriveService;
        this.jwtService = jwtService;
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
        return registerUserWithRole(userRequestDTO, Role.ADMIN, adminUserMapper);
    }

    @Override
    public UserResponseDTO registerModerator(UserRequestDTO userRequestDTO) {
        return registerUserWithRole(userRequestDTO, Role.MODERATOR, moderatorUserMapper);
    }

    @Override
    public UserResponseDTO registerRestaurateur(UserRequestDTO userRequestDTO) {
        return registerUserWithRole(userRequestDTO, Role.RESTAURATEUR, restaurateurUserMapper);
    }

    @Override
    public EmployeeUserResponseDTO registerCook(long restaurantId, UserRequestDTO userRequestDTO) {
        return registerEmployeeUser(restaurantId, userRequestDTO, Role.COOK, cookUserMapper);
    }

    @Override
    public EmployeeUserResponseDTO registerWaiter(long restaurantId, UserRequestDTO userRequestDTO) {

        return registerEmployeeUser(restaurantId, userRequestDTO, Role.WAITER, waiterUserMapper);
    }

    @Override
    public CustomerUserResponseDTO registerCustomer(UserRequestDTO userRequestDTO) {
        return (CustomerUserResponseDTO) registerUserWithRole(userRequestDTO, Role.CUSTOMER, customerUserMapper);
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

        User user = userRepository.findByEmailAndDeletedAtIsNull(email)
                .orElseThrow(() -> new EntityNotFoundException("user", "email", password));

        if (!user.isActive()) throw new UserNotActiveException(user.getEmail());

        String accessToken = jwtService.generateToken(user);

        return new TokenResponseDTO(accessToken);
    }

    @Override
    public UserResponseDTO getAuthenticatedUser() {
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userMapper.userToUserResponseDTO(principal);
    }

    private <T extends User> UserResponseDTO registerUserWithRole(UserRequestDTO userRequestDTO, Role role, UserMapper<T> mapper) {
        userRequestDTO.setRole(role.name());

        T user = mapper.userRequestDTOToUser(userRequestDTO);
        user = saveUser(user, userRequestDTO.getAvatarBase64());

        return mapper.userToUserResponseDTO(user);
    }

    private <E extends EmployeeUser> EmployeeUserResponseDTO registerEmployeeUser(long restaurantId, UserRequestDTO userRequestDTO, Role role, UserMapper<E> mapper) {
        userRequestDTO.setRole(role.name());

        E employee = mapper.userRequestDTOToUser(userRequestDTO);
        employee = saveEmployeeUser(restaurantId, employee, userRequestDTO.getAvatarBase64());

        return (EmployeeUserResponseDTO) mapper.userToUserResponseDTO(employee);
    }

    private <E extends EmployeeUser> E saveEmployeeUser(long restaurantId, E employeeUser, String avatarBase64) {
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Restaurant restaurant = restaurantRepository
                .findByIdAndDeletedAtIsNull(restaurantId)
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

        try {
            user = userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            removeUserAvatar(user);
            throw new EntityDuplicateException("user", "email", user.getEmail());
        } catch (Exception e) {
            removeUserAvatar(user);
            throw new EntityCreationException("user");
        }

        // todo send email confirmation

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

    private void checkRestaurantAccessOrThrow(User user, Restaurant restaurant) {
        if (restaurant.getRestaurateur().getId() == user.getId()) return;
        if (UserRoleUtils.isAdmin(user)) return;

        throw new ForbiddenRestaurantAccessException();
    }

    // todo implement the refresh token mechanism
    // todo implement the logout mechanism
}