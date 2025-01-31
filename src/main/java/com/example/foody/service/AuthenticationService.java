package com.example.foody.service;

import com.example.foody.dto.request.UserChangePasswordRequestDTO;
import com.example.foody.dto.request.UserLoginRequestDTO;
import com.example.foody.dto.request.UserRequestDTO;
import com.example.foody.dto.response.CustomerUserResponseDTO;
import com.example.foody.dto.response.EmployeeUserResponseDTO;
import com.example.foody.dto.response.TokenResponseDTO;
import com.example.foody.dto.response.UserResponseDTO;

/**
 * Service interface for authentication-related operations.
 */
public interface AuthenticationService {

    /**
     * Registers and authenticates a restaurateur.
     *
     * @param userRequestDTO the user request data transfer object
     * @return the token response data transfer object
     */
    TokenResponseDTO registerAndAuthenticateRestaurateur(UserRequestDTO userRequestDTO);

    /**
     * Registers and authenticates a customer.
     *
     * @param userRequestDTO the user request data transfer object
     * @return the token response data transfer object
     */
    TokenResponseDTO registerAndAuthenticateCustomer(UserRequestDTO userRequestDTO);

    /**
     * Registers an admin.
     *
     * @param userRequestDTO the user request data transfer object
     * @return the user response data transfer object
     */
    UserResponseDTO registerAdmin(UserRequestDTO userRequestDTO);

    /**
     * Registers a moderator.
     *
     * @param userRequestDTO the user request data transfer object
     * @return the user response data transfer object
     */
    UserResponseDTO registerModerator(UserRequestDTO userRequestDTO);

    /**
     * Registers a restaurateur.
     *
     * @param userRequestDTO the user request data transfer object
     * @return the user response data transfer object
     */
    UserResponseDTO registerRestaurateur(UserRequestDTO userRequestDTO);

    /**
     * Registers a cook for a specific restaurant.
     *
     * @param restaurantId the ID of the restaurant
     * @param userRequestDTO the user request data transfer object
     * @return the employee user response data transfer object
     */
    EmployeeUserResponseDTO registerCook(long restaurantId, UserRequestDTO userRequestDTO);

    /**
     * Registers a waiter for a specific restaurant.
     *
     * @param restaurantId the ID of the restaurant
     * @param userRequestDTO the user request data transfer object
     * @return the employee user response data transfer object
     */
    EmployeeUserResponseDTO registerWaiter(long restaurantId, UserRequestDTO userRequestDTO);

    /**
     * Registers a customer.
     *
     * @param userRequestDTO the user request data transfer object
     * @return the customer user response data transfer object
     */
    CustomerUserResponseDTO registerCustomer(UserRequestDTO userRequestDTO);

    /**
     * Authenticates a user.
     *
     * @param userLoginRequestDTO the user login request data transfer object
     * @return the token response data transfer object
     */
    TokenResponseDTO authenticate(UserLoginRequestDTO userLoginRequestDTO);

    /**
     * Authenticates a user with email and password.
     *
     * @param email the user's email
     * @param password the user's password
     * @return the token response data transfer object
     */
    TokenResponseDTO authenticate(String email, String password);

    /**
     * Retrieves the authenticated user.
     *
     * @return the user response data transfer object
     */
    UserResponseDTO getAuthenticatedUser();

    /**
     * Changes the password of the authenticated user.
     *
     * @param userChangePasswordRequestDTO the user change password request data transfer object
     */
    void changePassword(UserChangePasswordRequestDTO userChangePasswordRequestDTO);
}