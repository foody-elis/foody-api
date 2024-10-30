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
        if (user == null) {
            return null;
        }

        if (user instanceof CookUser || user instanceof WaiterUser) {
            return buildEmployeeUserResponseDTO((EmployeeUser) user);
        } else if (user instanceof CustomerUser) {
            return buildCustomerUserResponseDTO((CustomerUser) user);
        } else {
            return buildUserResponseDTO(user);
        }
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
                .phoneNumber(userRequestDTO.getPhoneNumber())
                .avatar(userRequestDTO.getAvatar());

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
        for (U user : users) {
            list.add(userToUserResponseDTO(user));
        }

        return list;
    }

    private UserResponseDTO buildUserResponseDTO(U user) {
        UserResponseDTO userResponseDTO = new UserResponseDTO();

        userResponseDTO.setId(user.getId());
        userResponseDTO.setEmail(user.getEmail());
        userResponseDTO.setName(user.getName());
        userResponseDTO.setSurname(user.getSurname());
        userResponseDTO.setBirthDate(user.getBirthDate());
        userResponseDTO.setPhoneNumber(user.getPhoneNumber());
        userResponseDTO.setAvatar(user.getAvatar());
        userResponseDTO.setRole(user.getRole());
        userResponseDTO.setActive(user.isActive());

        return userResponseDTO;
    }

    private EmployeeUserResponseDTO buildEmployeeUserResponseDTO(EmployeeUser employeeUser) {
        EmployeeUserResponseDTO employeeUserResponseDTO = new EmployeeUserResponseDTO();

        employeeUserResponseDTO.setEmployerRestaurantId(employerRestaurantId(employeeUser));
        employeeUserResponseDTO.setId(employeeUser.getId());
        employeeUserResponseDTO.setEmail(employeeUser.getEmail());
        employeeUserResponseDTO.setName(employeeUser.getName());
        employeeUserResponseDTO.setSurname(employeeUser.getSurname());
        employeeUserResponseDTO.setBirthDate(employeeUser.getBirthDate());
        employeeUserResponseDTO.setPhoneNumber(employeeUser.getPhoneNumber());
        employeeUserResponseDTO.setAvatar(employeeUser.getAvatar());
        employeeUserResponseDTO.setRole(employeeUser.getRole());
        employeeUserResponseDTO.setActive(employeeUser.isActive());

        return employeeUserResponseDTO;
    }

    private CustomerUserResponseDTO buildCustomerUserResponseDTO(CustomerUser customerUser) {
        CustomerUserResponseDTO customerUserResponseDTO = new CustomerUserResponseDTO();

        customerUserResponseDTO.setCreditCardId(creditCardId(customerUser));
        customerUserResponseDTO.setId(customerUser.getId());
        customerUserResponseDTO.setEmail(customerUser.getEmail());
        customerUserResponseDTO.setName(customerUser.getName());
        customerUserResponseDTO.setSurname(customerUser.getSurname());
        customerUserResponseDTO.setBirthDate(customerUser.getBirthDate());
        customerUserResponseDTO.setPhoneNumber(customerUser.getPhoneNumber());
        customerUserResponseDTO.setAvatar(customerUser.getAvatar());
        customerUserResponseDTO.setRole(customerUser.getRole());
        customerUserResponseDTO.setActive(customerUser.isActive());

        return customerUserResponseDTO;
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