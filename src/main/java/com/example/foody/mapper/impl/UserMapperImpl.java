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
import org.springframework.stereotype.Component;

@Component
public class UserMapperImpl implements UserMapper {
    private final UserBuilder<AdminUser> adminUserBuilder;
    private final UserBuilder<ModeratorUser> moderatorUserBuilder;
    private final UserBuilder<RestaurateurUser> restaurateurUserBuilder;
    private final UserBuilder<CookUser> cookUserBuilder;
    private final UserBuilder<WaiterUser> waiterUserBuilder;
    private final UserBuilder<CustomerUser> customerUserBuilder;

    public UserMapperImpl(
            UserBuilder<AdminUser> adminUserBuilder,
            UserBuilder<ModeratorUser> moderatorUserBuilder,
            UserBuilder<RestaurateurUser> restaurateurUserBuilder,
            UserBuilder<CookUser> cookUserBuilder,
            UserBuilder<WaiterUser> waiterUserBuilder,
            UserBuilder<CustomerUser> customerUserBuilder) {
        this.adminUserBuilder = adminUserBuilder;
        this.moderatorUserBuilder = moderatorUserBuilder;
        this.restaurateurUserBuilder = restaurateurUserBuilder;
        this.cookUserBuilder = cookUserBuilder;
        this.waiterUserBuilder = waiterUserBuilder;
        this.customerUserBuilder = customerUserBuilder;
    }

    @Override
    public UserResponseDTO userToUserResponseDTO(User user, String firebaseCustomToken) {
        return switch (user) {
            case null -> null;
            case EmployeeUser employeeUser -> buildEmployeeUserResponseDTO(employeeUser, firebaseCustomToken);
            case CustomerUser customerUser -> buildCustomerUserResponseDTO(customerUser, firebaseCustomToken);
            default -> buildUserResponseDTO(user, firebaseCustomToken);
        };
    }

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

    private UserResponseDTO buildUserResponseDTO(User user, String firebaseCustomToken) {
        UserResponseDTO userResponseDTO = new UserResponseDTO();

        mapCommonFields(user, firebaseCustomToken, userResponseDTO);
        userResponseDTO.setFirebaseCustomToken(firebaseCustomToken);

        return userResponseDTO;
    }

    private EmployeeUserResponseDTO buildEmployeeUserResponseDTO(EmployeeUser employeeUser, String firebaseCustomToken) {
        EmployeeUserResponseDTO employeeUserResponseDTO = new EmployeeUserResponseDTO();

        mapCommonFields(employeeUser, firebaseCustomToken, employeeUserResponseDTO);
        employeeUserResponseDTO.setEmployerRestaurantId(employerRestaurantId(employeeUser));

        return employeeUserResponseDTO;
    }

    private CustomerUserResponseDTO buildCustomerUserResponseDTO(CustomerUser customerUser, String firebaseCustomToken) {
        CustomerUserResponseDTO customerUserResponseDTO = new CustomerUserResponseDTO();

        mapCommonFields(customerUser, firebaseCustomToken, customerUserResponseDTO);
        customerUserResponseDTO.setCreditCardId(creditCardId(customerUser));

        return customerUserResponseDTO;
    }

    private <T extends UserResponseDTO> void mapCommonFields(User user, String firebaseCustomToken, T userResponseDTO) {
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