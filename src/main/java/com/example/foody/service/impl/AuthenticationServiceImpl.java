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
import com.example.foody.utils.enums.Role;
import jakarta.transaction.Transactional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional(rollbackOn = Exception.class)
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final UserMapper<User> userMapper;
    private final UserMapper<AdminUser> adminUserMapper;
    private final UserMapper<ModeratorUser> moderatorUserMapper;
    private final UserMapper<RestaurateurUser> restaurateurUserMapper;
    private final UserMapper<CookUser> cookUserMapper;
    private final UserMapper<WaiterUser> waiterUserMapper;
    private final UserMapper<CustomerUser> customerUserMapper;
    private final RestaurantRepository restaurantRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthenticationServiceImpl(UserRepository userRepository, UserMapper<User> userMapper, UserMapper<AdminUser> adminUserMapper, UserMapper<ModeratorUser> moderatorUserMapper, UserMapper<RestaurateurUser> restaurateurUserMapper, UserMapper<CookUser> cookUserMapper, UserMapper<WaiterUser> waiterUserMapper, UserMapper<CustomerUser> customerUserMapper, RestaurantRepository restaurantRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtService jwtService) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.adminUserMapper = adminUserMapper;
        this.moderatorUserMapper = moderatorUserMapper;
        this.restaurateurUserMapper = restaurateurUserMapper;
        this.cookUserMapper = cookUserMapper;
        this.waiterUserMapper = waiterUserMapper;
        this.customerUserMapper = customerUserMapper;
        this.restaurantRepository = restaurantRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
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
        userRequestDTO.setRole(Role.ADMIN.name());

        AdminUser admin = adminUserMapper.userRequestDTOToUser(userRequestDTO);
        admin = (AdminUser) register(admin);

        return adminUserMapper.userToUserResponseDTO(admin);
    }

    @Override
    public UserResponseDTO registerModerator(UserRequestDTO userRequestDTO) {
        userRequestDTO.setRole(Role.MODERATOR.name());

        ModeratorUser moderator = moderatorUserMapper.userRequestDTOToUser(userRequestDTO);
        moderator = (ModeratorUser) register(moderator);

        return moderatorUserMapper.userToUserResponseDTO(moderator);
    }

    @Override
    public UserResponseDTO registerRestaurateur(UserRequestDTO userRequestDTO) {
        userRequestDTO.setRole(Role.RESTAURATEUR.name());

        RestaurateurUser restaurateur = restaurateurUserMapper.userRequestDTOToUser(userRequestDTO);
        restaurateur = (RestaurateurUser) register(restaurateur);

        return restaurateurUserMapper.userToUserResponseDTO(restaurateur);
    }

    @Override
    public EmployeeUserResponseDTO registerCook(long restaurantId, UserRequestDTO userRequestDTO) {
        userRequestDTO.setRole(Role.COOK.name());

        CookUser cookUser = cookUserMapper.userRequestDTOToUser(userRequestDTO);
        cookUser = (CookUser) registerEmployee(restaurantId, cookUser);

        return (EmployeeUserResponseDTO) cookUserMapper.userToUserResponseDTO(cookUser);
    }

    @Override
    public EmployeeUserResponseDTO registerWaiter(long restaurantId, UserRequestDTO userRequestDTO) {
        userRequestDTO.setRole(Role.WAITER.name());

        WaiterUser waiterUser = waiterUserMapper.userRequestDTOToUser(userRequestDTO);
        waiterUser = (WaiterUser) registerEmployee(restaurantId, waiterUser);

        return (EmployeeUserResponseDTO) waiterUserMapper.userToUserResponseDTO(waiterUser);
    }

    private <E extends EmployeeUser> EmployeeUser registerEmployee(long restaurantId, E employeeUser) {
        // Check if the principal is the owner of the restaurant or an admin
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Restaurant restaurant = restaurantRepository
                .findByIdAndDeletedAtIsNull(restaurantId)
                .orElseThrow(() -> new EntityNotFoundException("restaurant", "id", restaurantId));

        if (restaurant.getRestaurateur().getId() != principal.getId() && !principal.getRole().equals(Role.ADMIN)) {
            throw new ForbiddenRestaurantAccessException();
        }

        employeeUser.setEmployerRestaurant(restaurant);
        employeeUser = (E) register(employeeUser);

        return employeeUser;
    }

    public CustomerUserResponseDTO registerCustomer(UserRequestDTO userRequestDTO) {
        userRequestDTO.setRole(Role.CUSTOMER.name());

        CustomerUser customer = customerUserMapper.userRequestDTOToUser(userRequestDTO);
        customer = (CustomerUser) register(customer);

        return (CustomerUserResponseDTO) customerUserMapper.userToUserResponseDTO(customer);
    }

    private <T extends User> User register(T user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        try {
            user = userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new EntityDuplicateException("user", "email", user.getEmail());
        } catch (Exception e) {
            throw new EntityCreationException("user");
        }

        // todo send email confirmation

        return user;
    }

    public TokenResponseDTO authenticate(UserLoginRequestDTO userLoginRequestDTO) {
        return authenticate(userLoginRequestDTO.getEmail(), userLoginRequestDTO.getPassword());
    }

    public TokenResponseDTO authenticate(String email, String password) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
            );
        } catch (AuthenticationException e) {
            throw new InvalidCredentialsException();
        }

        User user = userRepository.findByEmailAndDeletedAtIsNull(email)
                .orElseThrow(() -> new EntityNotFoundException("user", "email", password));

        if (!user.isActive()) {
            throw new UserNotActiveException(user.getEmail());
        }

        String accessToken = jwtService.generateToken(user);

        return new TokenResponseDTO(accessToken);
    }

    @Override
    public UserResponseDTO getAuthenticatedUser() {
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userMapper.userToUserResponseDTO(principal);
    }

    // todo implement the refresh token mechanism
    // todo implement the logout mechanism
}