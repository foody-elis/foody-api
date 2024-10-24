package com.example.foody.auth;

import com.example.foody.dto.request.UserRequestDTO;
import com.example.foody.dto.response.CustomerUserResponseDTO;
import com.example.foody.dto.response.EmployeeUserResponseDTO;
import com.example.foody.dto.response.UserResponseDTO;

public interface AuthenticationService {
    TokenDTO registerAndAuthenticateRestaurateur(UserRequestDTO userRequestDTO);
    TokenDTO registerAndAuthenticateCustomer(UserRequestDTO userRequestDTO);
    UserResponseDTO registerAdmin(UserRequestDTO userRequestDTO);
    UserResponseDTO registerModerator(UserRequestDTO userRequestDTO);
    UserResponseDTO registerRestaurateur(UserRequestDTO userRequestDTO);
    EmployeeUserResponseDTO registerEmployee(long restaurantId, UserRequestDTO userRequestDTO);
    CustomerUserResponseDTO registerCustomer(UserRequestDTO userRequestDTO);
    TokenDTO authenticate(UserLoginDTO userLoginDTO);
    TokenDTO authenticate(String email, String password);
}