package com.example.foody.mapper;

import com.example.foody.dto.request.UserRequestDTO;
import com.example.foody.dto.response.UserResponseDTO;
import com.example.foody.model.user.User;

import java.util.List;

public interface UserMapper<U extends User> {
    UserResponseDTO userToUserResponseDTO(U user);
    U userRequestDTOToUser(UserRequestDTO userRequestDTO);
    List<UserResponseDTO> usersToUserResponseDTOs(List<U> users);
}