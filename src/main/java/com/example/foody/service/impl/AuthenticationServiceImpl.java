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
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

/**
 * Implementation of the {@link AuthenticationService} interface.
 * <p>
 * Provides methods for user registration, authentication, and password management.
 */
@Service
@Transactional(rollbackOn = Exception.class)
@AllArgsConstructor
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

    /**
     * {@inheritDoc}
     * <p>
     * Registers and authenticates a new restaurateur user.
     *
     * @param userRequestDTO the user request data transfer object
     * @return the token response data transfer object
     * @throws EntityDuplicateException if the entity already exists
     * @throws EntityCreationException if there is an error creating the entity
     * @throws InvalidCredentialsException if the credentials are invalid
     * @throws EntityNotFoundException if the entity is not found
     * @throws UserNotActiveException if the user is not active
     */
    @Override
    public TokenResponseDTO registerAndAuthenticateRestaurateur(UserRequestDTO userRequestDTO) {
        registerRestaurateur(userRequestDTO);
        return authenticate(userRequestDTO.getEmail(), userRequestDTO.getPassword());
    }

    /**
     * {@inheritDoc}
     * <p>
     * Registers and authenticates a new customer user.
     *
     * @param userRequestDTO the user request data transfer object
     * @return the token response data transfer object
     * @throws EntityDuplicateException if the entity already exists
     * @throws EntityCreationException if there is an error creating the entity
     * @throws InvalidCredentialsException if the credentials are invalid
     * @throws EntityNotFoundException if the entity is not found
     * @throws UserNotActiveException if the user is not active
     */
    @Override
    public TokenResponseDTO registerAndAuthenticateCustomer(UserRequestDTO userRequestDTO) {
        registerCustomer(userRequestDTO);
        return authenticate(userRequestDTO.getEmail(), userRequestDTO.getPassword());
    }

    /**
     * {@inheritDoc}
     * <p>
     * Registers a new admin user.
     *
     * @param userRequestDTO the user request data transfer object
     * @return the user response data transfer object
     * @throws EntityDuplicateException if the entity already exists
     * @throws EntityCreationException if there is an error creating the entity
     */
    @Override
    public UserResponseDTO registerAdmin(UserRequestDTO userRequestDTO) {
        return registerUserWithRole(userRequestDTO, Role.ADMIN);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Registers a new moderator user.
     *
     * @param userRequestDTO the user request data transfer object
     * @return the user response data transfer object
     * @throws EntityDuplicateException if the entity already exists
     * @throws EntityCreationException if there is an error creating the entity
     */
    @Override
    public UserResponseDTO registerModerator(UserRequestDTO userRequestDTO) {
        return registerUserWithRole(userRequestDTO, Role.MODERATOR);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Registers a new restaurateur user.
     *
     * @param userRequestDTO the user request data transfer object
     * @return the user response data transfer object
     * @throws EntityDuplicateException if the entity already exists
     * @throws EntityCreationException if there is an error creating the entity
     */
    @Override
    public UserResponseDTO registerRestaurateur(UserRequestDTO userRequestDTO) {
        return registerUserWithRole(userRequestDTO, Role.RESTAURATEUR);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Registers a new cook for a specific restaurant.
     *
     * @param restaurantId the restaurant ID
     * @param userRequestDTO the user request data transfer object
     * @return the employee user response data transfer object
     * @throws EntityDuplicateException if the entity already exists
     * @throws EntityCreationException if there is an error creating the entity
     * @throws EntityNotFoundException if the entity is not found
     * @throws ForbiddenRestaurantAccessException if access to the restaurant is forbidden
     */
    @Override
    public EmployeeUserResponseDTO registerCook(long restaurantId, UserRequestDTO userRequestDTO) {
        return registerEmployeeUser(restaurantId, userRequestDTO, Role.COOK);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Registers a new waiter for a specific restaurant.
     *
     * @param restaurantId the restaurant ID
     * @param userRequestDTO the user request data transfer object
     * @return the employee user response data transfer object
     * @throws EntityDuplicateException if the entity already exists
     * @throws EntityCreationException if there is an error creating the entity
     * @throws EntityNotFoundException if the entity is not found
     * @throws ForbiddenRestaurantAccessException if access to the restaurant is forbidden
     */
    @Override
    public EmployeeUserResponseDTO registerWaiter(long restaurantId, UserRequestDTO userRequestDTO) {
        return registerEmployeeUser(restaurantId, userRequestDTO, Role.WAITER);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Registers a new customer user.
     *
     * @param userRequestDTO the user request data transfer object
     * @return the customer user response data transfer object
     * @throws EntityDuplicateException if the entity already exists
     * @throws EntityCreationException if there is an error creating the entity
     */
    @Override
    public CustomerUserResponseDTO registerCustomer(UserRequestDTO userRequestDTO) {
        return (CustomerUserResponseDTO) registerUserWithRole(userRequestDTO, Role.CUSTOMER);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Authenticates a user.
     *
     * @param userLoginRequestDTO the user login request data transfer object
     * @return the token response data transfer object
     * @throws InvalidCredentialsException if the credentials are invalid
     * @throws EntityNotFoundException if the entity is not found
     * @throws UserNotActiveException if the user is not active
     */
    @Override
    public TokenResponseDTO authenticate(UserLoginRequestDTO userLoginRequestDTO) {
        return authenticate(userLoginRequestDTO.getEmail(), userLoginRequestDTO.getPassword());
    }

    /**
     * {@inheritDoc}
     * <p>
     * Authenticates a user by email and password.
     *
     * @param email the user's email
     * @param password the user's password
     * @return the token response data transfer object
     * @throws InvalidCredentialsException if the credentials are invalid
     * @throws EntityNotFoundException if the entity is not found
     * @throws UserNotActiveException if the user is not active
     */
    @Override
    public TokenResponseDTO authenticate(String email, String password) {
        try {
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(email, password);
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

    /**
     * {@inheritDoc}
     * <p>
     * Retrieves the authenticated user.
     *
     * @return the user response data transfer object
     */
    @Override
    public UserResponseDTO getAuthenticatedUser() {
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userHelper.buildUserResponseDTO(principal);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Changes the password of the authenticated user.
     *
     * @param userChangePasswordRequestDTO the user change password request data transfer object
     * @throws InvalidPasswordException if the password is invalid
     * @throws EntityEditException if there is an error editing the entity
     */
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

    /**
     * Registers a user with a specific role.
     * <p>
     * This method maps the {@link UserRequestDTO} to a {@link User} object, sets the role, and saves the user.
     *
     * @param userRequestDTO the user request data transfer object
     * @param role the role to assign to the user
     * @param <T> the type of the user
     * @return the user response data transfer object
     * @throws EntityDuplicateException if the entity already exists
     * @throws EntityCreationException if there is an error creating the entity
     */
    private <T extends User> UserResponseDTO registerUserWithRole(UserRequestDTO userRequestDTO, Role role) {
        userRequestDTO.setRole(role.name());

        T user = (T) userMapper.userRequestDTOToUser(userRequestDTO);
        user = saveUser(user, userRequestDTO.getAvatarBase64());

        return userHelper.buildUserResponseDTO(user);
    }

    /**
     * Registers an employee user for a specific restaurant.
     * <p>
     * This method maps the {@link UserRequestDTO} to an {@link EmployeeUser} object, sets the role, and saves the employee user.
     *
     * @param restaurantId the restaurant ID
     * @param userRequestDTO the user request data transfer object
     * @param role the role to assign to the employee user
     * @param <E> the type of the employee user
     * @return the employee user response data transfer object
     * @throws EntityDuplicateException if the entity already exists
     * @throws EntityCreationException if there is an error creating the entity
     * @throws EntityNotFoundException if the entity is not found
     * @throws ForbiddenRestaurantAccessException if access to the restaurant is forbidden
     */
    private <E extends EmployeeUser> EmployeeUserResponseDTO registerEmployeeUser(
            long restaurantId,
            UserRequestDTO userRequestDTO,
            Role role
    ) {
        userRequestDTO.setRole(role.name());

        E employee = (E) userMapper.userRequestDTOToUser(userRequestDTO);
        employee = saveEmployeeUser(restaurantId, employee, userRequestDTO.getAvatarBase64());

        return (EmployeeUserResponseDTO) userHelper.buildUserResponseDTO(employee);
    }

    /**
     * Saves an employee user for a specific restaurant.
     * <p>
     * This method sets the employer restaurant, saves the employee user, and returns the saved employee user.
     *
     * @param restaurantId the restaurant ID
     * @param employeeUser the employee user to save
     * @param avatarBase64 the base64 encoded avatar image
     * @param <E> the type of the employee user
     * @return the saved employee user
     * @throws EntityNotFoundException if the entity is not found
     * @throws EntityCreationException if there is an error creating the entity
     */
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

    /**
     * Saves a user.
     * <p>
     * This method sets the avatar URL, encodes the password, sets the Firebase custom token, and saves the user.
     *
     * @param user the user to save
     * @param avatarBase64 the base64 encoded avatar image
     * @param <T> the type of the user
     * @return the saved user
     * @throws EntityDuplicateException if the entity already exists
     * @throws EntityCreationException if there is an error creating the entity
     */
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

    /**
     * Saves the user's avatar image.
     * <p>
     * This method uploads the base64 encoded avatar image to Google Drive and returns the URL.
     *
     * @param userAvatarBase64 the base64 encoded avatar image
     * @return the URL of the uploaded avatar image
     */
    private String saveUserAvatar(String userAvatarBase64) {
        return Optional.ofNullable(userAvatarBase64)
                .map(avatarBase64 -> googleDriveService.uploadBase64Image(
                        avatarBase64,
                        GoogleDriveFileType.USER_AVATAR
                ))
                .orElse(null);
    }

    /**
     * Removes the user's avatar image.
     * <p>
     * This method deletes the avatar image from Google Drive.
     *
     * @param user the user whose avatar image to remove
     */
    private void removeUserAvatar(User user) {
        Optional.ofNullable(user.getAvatarUrl())
                .ifPresent(googleDriveService::deleteImage);
    }

    /**
     * Sends a registration email to the user.
     * <p>
     * This method sends a templated email to the user with registration details.
     *
     * @param user the user to send the email to
     */
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

    /**
     * Checks if the user has access to the restaurant.
     * <p>
     * This method throws a {@link ForbiddenRestaurantAccessException} if the user does not have access to the restaurant.
     *
     * @param user the user to check
     * @param restaurant the restaurant to check access for
     * @throws ForbiddenRestaurantAccessException if access to the restaurant is forbidden
     */
    private void checkRestaurantAccessOrThrow(User user, Restaurant restaurant) {
        if (UserRoleUtils.isAdmin(user)) return;
        if (restaurant.getRestaurateur().getId() == user.getId()) return;

        throw new ForbiddenRestaurantAccessException();
    }

    /**
     * Sends a change password email to the user.
     * <p>
     * This method sends a templated email to the user with change password details.
     *
     * @param user the user to send the email to
     */
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
}