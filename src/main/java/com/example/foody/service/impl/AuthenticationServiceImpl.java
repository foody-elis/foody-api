package com.example.foody.auth.impl;

import com.example.foody.auth.AuthenticationService;
import com.example.foody.auth.TokenDTO;
import com.example.foody.auth.UserLoginDTO;
import com.example.foody.dto.request.UserRequestDTO;
import com.example.foody.dto.response.CustomerUserResponseDTO;
import com.example.foody.dto.response.EmployeeUserResponseDTO;
import com.example.foody.dto.response.UserResponseDTO;
import com.example.foody.exceptions.auth.InvalidCredentialsException;
import com.example.foody.exceptions.entity.EntityCreationException;
import com.example.foody.exceptions.entity.EntityDuplicateException;
import com.example.foody.exceptions.entity.EntityNotFoundException;
import com.example.foody.exceptions.restaurant.ForbiddenRestaurantAccessException;
import com.example.foody.exceptions.user.UserNotActiveException;
import com.example.foody.mapper.CustomerUserMapper;
import com.example.foody.mapper.EmployeeUserMapper;
import com.example.foody.mapper.UserMapper;
import com.example.foody.model.Restaurant;
import com.example.foody.model.user.*;
import com.example.foody.repository.RestaurantRepository;
import com.example.foody.repository.UserRepository;
import com.example.foody.security.JwtService;
import com.example.foody.utils.Role;
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
    private final RestaurantRepository restaurantRepository;
    private final UserMapper userMapper;
    private final EmployeeUserMapper employeeUserMapper;
    private final CustomerUserMapper customerUserMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthenticationServiceImpl(UserRepository userRepository, RestaurantRepository restaurantRepository, UserMapper userMapper, EmployeeUserMapper employeeUserMapper, CustomerUserMapper customerUserMapper, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtService jwtService) {
        this.userRepository = userRepository;
        this.restaurantRepository = restaurantRepository;
        this.userMapper = userMapper;
        this.employeeUserMapper = employeeUserMapper;
        this.customerUserMapper = customerUserMapper;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @Override
    public TokenDTO registerAndAuthenticateRestaurateur(UserRequestDTO userRequestDTO) {
        UserResponseDTO restaurateurDTO = registerRestaurateur(userRequestDTO);
        return authenticate(userRequestDTO.getEmail(), userRequestDTO.getPassword());
    }

    @Override
    public TokenDTO registerAndAuthenticateCustomer(UserRequestDTO userRequestDTO) {
        UserResponseDTO customerDTO = registerCustomer(userRequestDTO);
        return authenticate(userRequestDTO.getEmail(), userRequestDTO.getPassword());
    }

    public UserResponseDTO registerAdmin(UserRequestDTO userRequestDTO) {
        userRequestDTO.setRole(Role.ADMIN.name());

        AdminUser admin = (AdminUser) userMapper.userRequestDTOToUser(userRequestDTO);
        admin = (AdminUser) register(admin);

        return userMapper.userToUserResponseDTO(admin);
    }

    public UserResponseDTO registerModerator(UserRequestDTO userRequestDTO) {
        userRequestDTO.setRole(Role.MODERATOR.name());

        ModeratorUser moderator = (ModeratorUser) userMapper.userRequestDTOToUser(userRequestDTO);
        moderator = (ModeratorUser) register(moderator);

        return userMapper.userToUserResponseDTO(moderator);
    }

    public UserResponseDTO registerRestaurateur(UserRequestDTO userRequestDTO) {
        userRequestDTO.setRole(Role.RESTAURATEUR.name());

        RestaurateurUser restaurateur = (RestaurateurUser) userMapper.userRequestDTOToUser(userRequestDTO);
        restaurateur = (RestaurateurUser) register(restaurateur);

        return userMapper.userToUserResponseDTO(restaurateur);
    }

    public EmployeeUserResponseDTO registerEmployee(long restaurantId, UserRequestDTO userRequestDTO) {
        userRequestDTO.setRole(Role.COOK.name());
        EmployeeUser employee = (EmployeeUser) userMapper.userRequestDTOToUser(userRequestDTO);

        // Check if the principal is the owner of the restaurant or an admin
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Restaurant restaurant = restaurantRepository
                .findByIdAndDeletedAtIsNull(restaurantId)
                .orElseThrow(() -> new EntityNotFoundException("restaurant", "id", restaurantId));

        // todo check if I can use instanceof instead of getRole
        if (restaurant.getRestaurateur().getId() != principal.getId() && !principal.getRole().equals(Role.ADMIN)) {
            throw new ForbiddenRestaurantAccessException();
        }

        employee.setEmployerRestaurant(restaurant);
        employee = (EmployeeUser) register(employee);

        return employeeUserMapper.employeeUserToEmployeeUserResponseDTO(employee);
    }

    public CustomerUserResponseDTO registerCustomer(UserRequestDTO userRequestDTO) {
        userRequestDTO.setRole(Role.CUSTOMER.name());

        CustomerUser customer = (CustomerUser) userMapper.userRequestDTOToUser(userRequestDTO);
        customer = (CustomerUser) register(customer);

        return customerUserMapper.customerUserToCustomerUserResponseDTO(customer);
    }

    private <T extends User> User register(T user) {
        // Encode the password
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

    public TokenDTO authenticate(UserLoginDTO userLoginDTO) {
        return authenticate(userLoginDTO.getEmail(), userLoginDTO.getPassword());
    }

    public TokenDTO authenticate(String email, String password) {
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

        String accessToken = jwtService.generateToken(user.getEmail());

        return new TokenDTO(accessToken);
    }

    // todo implement the refresh token mechanism
    // todo implement the logout mechanism
}