package com.example.foody.service;

import com.example.foody.dto.request.UserChangePasswordRequestDTO;
import com.example.foody.dto.request.UserLoginRequestDTO;
import com.example.foody.dto.request.UserRequestDTO;
import com.example.foody.dto.response.CustomerUserResponseDTO;
import com.example.foody.dto.response.EmployeeUserResponseDTO;
import com.example.foody.dto.response.TokenResponseDTO;
import com.example.foody.dto.response.UserResponseDTO;

public interface AuthenticationService {
    TokenResponseDTO registerAndAuthenticateRestaurateur(UserRequestDTO userRequestDTO);

    TokenResponseDTO registerAndAuthenticateCustomer(UserRequestDTO userRequestDTO);

    UserResponseDTO registerAdmin(UserRequestDTO userRequestDTO);

    UserResponseDTO registerModerator(UserRequestDTO userRequestDTO);

    UserResponseDTO registerRestaurateur(UserRequestDTO userRequestDTO);

    EmployeeUserResponseDTO registerCook(long restaurantId, UserRequestDTO userRequestDTO);

    EmployeeUserResponseDTO registerWaiter(long restaurantId, UserRequestDTO userRequestDTO);

    CustomerUserResponseDTO registerCustomer(UserRequestDTO userRequestDTO);

    TokenResponseDTO authenticate(UserLoginRequestDTO userLoginRequestDTO);

    TokenResponseDTO authenticate(String email, String password);

    UserResponseDTO getAuthenticatedUser();

    void changePassword(UserChangePasswordRequestDTO userChangePasswordRequestDTO);
}