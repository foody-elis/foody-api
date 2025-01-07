package com.example.foody.mapper;

import com.example.foody.dto.request.UserRequestDTO;
import com.example.foody.dto.request.UserUpdateChatIdRequestDTO;
import com.example.foody.dto.request.UserUpdateRequestDTO;
import com.example.foody.dto.response.UserResponseDTO;
import com.example.foody.model.user.User;

import java.util.List;

public interface UserMapper<U extends User> {
    UserResponseDTO userToUserResponseDTO(U user);
    U userRequestDTOToUser(UserRequestDTO userRequestDTO);
    void updateUserFromUserUpdateRequestDTO(U user, UserUpdateRequestDTO userUpdateRequestDTO);
    void updateUserFromUserUpdateChatIdRequestDTO(U user, UserUpdateChatIdRequestDTO userUpdateChatIdRequestDTO);
    List<UserResponseDTO> usersToUserResponseDTOs(List<U> users);
}