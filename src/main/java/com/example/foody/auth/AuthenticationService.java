package com.example.foody.auth;

import com.example.foody.dto.request.UserRequestDTO;
import com.example.foody.dto.response.UserResponseDTO;

public interface AuthenticationService {
    UserResponseDTO registerAdmin(UserRequestDTO userRequestDTO);
    UserResponseDTO registerModerator(UserRequestDTO userRequestDTO);
    UserResponseDTO registerRestaurateur(UserRequestDTO userRequestDTO);
    UserResponseDTO registerCook(UserRequestDTO userRequestDTO);
    UserResponseDTO registerWaiter(UserRequestDTO userRequestDTO);
    UserResponseDTO registerCustomer(UserRequestDTO userRequestDTO);
    TokenDTO authenticate(UserLoginDTO userLoginDTO);
}