package com.example.foody.mapper.impl;

import com.example.foody.builder.UserBuilder;
import com.example.foody.dto.request.UserRequestDTO;
import com.example.foody.dto.request.UserUpdateRequestDTO;
import com.example.foody.dto.response.CustomerUserResponseDTO;
import com.example.foody.dto.response.EmployeeUserResponseDTO;
import com.example.foody.dto.response.UserResponseDTO;
import com.example.foody.mapper.UserMapper;
import com.example.foody.model.CreditCard;
import com.example.foody.model.Restaurant;
import com.example.foody.model.user.*;
import com.example.foody.utils.enums.Role;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Implementation of the {@link UserMapper} interface.
 * <p>
 * Provides methods to convert between {@link User} entities and DTOs.
 */
@Component
@AllArgsConstructor
public class UserMapperImpl implements UserMapper {

    private final UserBuilder<AdminUser> adminUserBuilder;
    private final UserBuilder<ModeratorUser> moderatorUserBuilder;
    private final UserBuilder<RestaurateurUser> restaurateurUserBuilder;
    private final UserBuilder<CookUser> cookUserBuilder;
    private final UserBuilder<WaiterUser> waiterUserBuilder;
    private final UserBuilder<CustomerUser> customerUserBuilder;

    /**
     * {@inheritDoc}
     * <p>
     * Converts a {@link User} entity to a {@link UserResponseDTO}.
     *
     * @param user                the User entity to convert
     * @param firebaseCustomToken the Firebase custom token associated with the user
     * @return the converted UserResponseDTO
     */
    @Override
    public UserResponseDTO userToUserResponseDTO(User user, String firebaseCustomToken) {
        return switch (user) {
            case null -> null;
            case EmployeeUser employeeUser -> buildEmployeeUserResponseDTO(employeeUser, firebaseCustomToken);
            case CustomerUser customerUser -> buildCustomerUserResponseDTO(customerUser, firebaseCustomToken);
            default -> buildUserResponseDTO(user, firebaseCustomToken);
        };
    }

    /**
     * {@inheritDoc}
     * <p>
     * Converts a {@link UserRequestDTO} to a {@link User} entity.
     *
     * @param userRequestDTO the UserRequestDTO to convert
     * @return the converted User entity
     */
    @Override
    public User userRequestDTOToUser(UserRequestDTO userRequestDTO) {
        if (userRequestDTO == null) {
            return null;
        }

        UserBuilder<?> userBuilder = switch (userRequestDTO.getRole()) {
            case Role.Constants.ADMIN_VALUE -> adminUserBuilder;
            case Role.Constants.MODERATOR_VALUE -> moderatorUserBuilder;
            case Role.Constants.RESTAURATEUR_VALUE -> restaurateurUserBuilder;
            case Role.Constants.COOK_VALUE -> cookUserBuilder;
            case Role.Constants.WAITER_VALUE -> waiterUserBuilder;
            case Role.Constants.CUSTOMER_VALUE -> customerUserBuilder;
            default -> throw new IllegalArgumentException("Invalid role: " + userRequestDTO.getRole());
        };
        userBuilder
                .email(userRequestDTO.getEmail())
                .password(userRequestDTO.getPassword())
                .name(userRequestDTO.getName())
                .surname(userRequestDTO.getSurname())
                .birthDate(userRequestDTO.getBirthDate())
                .phoneNumber(userRequestDTO.getPhoneNumber());

        if (userRequestDTO.getRole() != null) {
            userBuilder.role(Enum.valueOf(Role.class, userRequestDTO.getRole()));
        }

        return userBuilder.build();
    }

    /**
     * {@inheritDoc}
     * <p>
     * Updates a {@link User} entity from a {@link UserUpdateRequestDTO}.
     *
     * @param user                 the User entity to update
     * @param userUpdateRequestDTO the UserUpdateRequestDTO with updated information
     */
    @Override
    public void updateUserFromUserUpdateRequestDTO(User user, UserUpdateRequestDTO userUpdateRequestDTO) {
        if (user == null || userUpdateRequestDTO == null) {
            return;
        }

        user.setName(userUpdateRequestDTO.getName());
        user.setSurname(userUpdateRequestDTO.getSurname());
        user.setBirthDate(userUpdateRequestDTO.getBirthDate());
        user.setPhoneNumber(userUpdateRequestDTO.getPhoneNumber());
    }

    /**
     * Builds a {@link UserResponseDTO} from a {@link User} entity.
     *
     * @param user                the User entity
     * @param firebaseCustomToken the Firebase custom token associated with the user
     * @return the built UserResponseDTO
     */
    private UserResponseDTO buildUserResponseDTO(User user, String firebaseCustomToken) {
        UserResponseDTO userResponseDTO = new UserResponseDTO();

        mapCommonFields(user, firebaseCustomToken, userResponseDTO);
        userResponseDTO.setFirebaseCustomToken(firebaseCustomToken);

        return userResponseDTO;
    }

    /**
     * Builds an {@link EmployeeUserResponseDTO} from an {@link EmployeeUser} entity.
     *
     * @param employeeUser        the EmployeeUser entity
     * @param firebaseCustomToken the Firebase custom token associated with the user
     * @return the built EmployeeUserResponseDTO
     */
    private EmployeeUserResponseDTO buildEmployeeUserResponseDTO(
            EmployeeUser employeeUser,
            String firebaseCustomToken
    ) {
        EmployeeUserResponseDTO employeeUserResponseDTO = new EmployeeUserResponseDTO();

        mapCommonFields(employeeUser, firebaseCustomToken, employeeUserResponseDTO);
        employeeUserResponseDTO.setEmployerRestaurantId(employerRestaurantId(employeeUser));

        return employeeUserResponseDTO;
    }

    /**
     * Builds a {@link CustomerUserResponseDTO} from a {@link CustomerUser} entity.
     *
     * @param customerUser        the CustomerUser entity
     * @param firebaseCustomToken the Firebase custom token associated with the user
     * @return the built CustomerUserResponseDTO
     */
    private CustomerUserResponseDTO buildCustomerUserResponseDTO(
            CustomerUser customerUser,
            String firebaseCustomToken
    ) {
        CustomerUserResponseDTO customerUserResponseDTO = new CustomerUserResponseDTO();

        mapCommonFields(customerUser, firebaseCustomToken, customerUserResponseDTO);
        customerUserResponseDTO.setCreditCardId(creditCardId(customerUser));

        return customerUserResponseDTO;
    }

    /**
     * Maps common fields from a {@link User} entity to a {@link UserResponseDTO}.
     *
     * @param user                the User entity
     * @param firebaseCustomToken the Firebase custom token associated with the user
     * @param userResponseDTO     the UserResponseDTO to populate
     * @param <T>                 the type of UserResponseDTO
     */
    private <T extends UserResponseDTO> void mapCommonFields(
            User user,
            String firebaseCustomToken,
            T userResponseDTO
    ) {
        userResponseDTO.setId(user.getId());
        userResponseDTO.setEmail(user.getEmail());
        userResponseDTO.setName(user.getName());
        userResponseDTO.setSurname(user.getSurname());
        userResponseDTO.setBirthDate(user.getBirthDate());
        userResponseDTO.setPhoneNumber(user.getPhoneNumber());
        userResponseDTO.setAvatarUrl(user.getAvatarUrl());
        userResponseDTO.setRole(user.getRole());
        userResponseDTO.setActive(user.isActive());
        userResponseDTO.setFirebaseCustomToken(user.getFirebaseCustomToken());
        userResponseDTO.setFirebaseCustomToken(firebaseCustomToken);
    }

    /**
     * Retrieves the employer restaurant ID from an {@link EmployeeUser} entity.
     *
     * @param employee the EmployeeUser entity
     * @return the employer restaurant ID, or null if not available
     */
    private Long employerRestaurantId(EmployeeUser employee) {
        if (employee == null) {
            return null;
        }
        Restaurant employerRestaurant = employee.getEmployerRestaurant();
        if (employerRestaurant == null) {
            return null;
        }
        return employerRestaurant.getId();
    }

    /**
     * Retrieves the credit card ID from a {@link CustomerUser} entity.
     *
     * @param customerUser the CustomerUser entity
     * @return the credit card ID, or null if not available
     */
    private Long creditCardId(CustomerUser customerUser) {
        if (customerUser == null) {
            return null;
        }
        CreditCard creditCard = customerUser.getCreditCard();
        if (creditCard == null) {
            return null;
        }
        return creditCard.getId();
    }
}