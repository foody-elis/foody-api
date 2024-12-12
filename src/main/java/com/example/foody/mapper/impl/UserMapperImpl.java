package com.example.foody.mapper.impl;

import com.example.foody.builder.UserBuilder;
import com.example.foody.dto.request.UserRequestDTO;
import com.example.foody.dto.response.CustomerUserResponseDTO;
import com.example.foody.dto.response.EmployeeUserResponseDTO;
import com.example.foody.dto.response.UserResponseDTO;
import com.example.foody.mapper.UserMapper;
import com.example.foody.model.CreditCard;
import com.example.foody.model.Restaurant;
import com.example.foody.model.user.*;
import com.example.foody.utils.enums.Role;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserMapperImpl<U extends User> implements UserMapper<U> {
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
    public UserResponseDTO userToUserResponseDTO(U user) {
        return switch (user) {
            case null -> null;
            case EmployeeUser employeeUser -> buildEmployeeUserResponseDTO(employeeUser);
            case CustomerUser customerUser -> buildCustomerUserResponseDTO(customerUser);
            default -> buildUserResponseDTO(user);
        };
    }

    @Override
    public U userRequestDTOToUser(UserRequestDTO userRequestDTO) {
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

        return (U) userBuilder.build();
    }

    @Override
    public List<UserResponseDTO> usersToUserResponseDTOs(List<U> users) {
        if (users == null) {
            return null;
        }

        List<UserResponseDTO> list = new ArrayList<>(users.size());
        users.forEach(user -> list.add(userToUserResponseDTO(user)));

        return list;
    }

    private UserResponseDTO buildUserResponseDTO(U user) {
        UserResponseDTO userResponseDTO = new UserResponseDTO();
        return mapCommonFields(user, userResponseDTO);
    }

    private EmployeeUserResponseDTO buildEmployeeUserResponseDTO(EmployeeUser employeeUser) {
        EmployeeUserResponseDTO employeeUserResponseDTO = new EmployeeUserResponseDTO();

        mapCommonFields(employeeUser, employeeUserResponseDTO);
        employeeUserResponseDTO.setEmployerRestaurantId(employerRestaurantId(employeeUser));

        return employeeUserResponseDTO;
    }

    private CustomerUserResponseDTO buildCustomerUserResponseDTO(CustomerUser customerUser) {
        CustomerUserResponseDTO customerUserResponseDTO = new CustomerUserResponseDTO();

        mapCommonFields(customerUser, customerUserResponseDTO);
        customerUserResponseDTO.setCreditCardId(creditCardId(customerUser));

        return customerUserResponseDTO;
    }

    private <T extends UserResponseDTO> T mapCommonFields(User user, T userResponseDTO) {
        userResponseDTO.setId(user.getId());
        userResponseDTO.setEmail(user.getEmail());
        userResponseDTO.setName(user.getName());
        userResponseDTO.setSurname(user.getSurname());
        userResponseDTO.setBirthDate(user.getBirthDate());
        userResponseDTO.setPhoneNumber(user.getPhoneNumber());
        userResponseDTO.setAvatarUrl(user.getAvatarUrl());
        userResponseDTO.setRole(user.getRole());
        userResponseDTO.setActive(user.isActive());

        return userResponseDTO;
    }

    private long employerRestaurantId(EmployeeUser employee) {
        if (employee == null) {
            return 0L;
        }
        Restaurant employerRestaurant = employee.getEmployerRestaurant();
        if (employerRestaurant == null) {
            return 0L;
        }
        return employerRestaurant.getId();
    }

    private long creditCardId(CustomerUser customerUser) {
        if (customerUser == null) {
            return 0L;
        }
        CreditCard creditCard = customerUser.getCreditCard();
        if (creditCard == null) {
            return 0L;
        }
        return creditCard.getId();
    }
}