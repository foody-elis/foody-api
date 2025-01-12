package com.example.foody.mapper;

import com.example.foody.dto.request.UserRequestDTO;
import com.example.foody.dto.request.UserUpdateRequestDTO;
import com.example.foody.dto.response.UserResponseDTO;
import com.example.foody.model.user.User;

public interface UserMapper {
    UserResponseDTO userToUserResponseDTO(User user, String firebaseCustomToken);
    User userRequestDTOToUser(UserRequestDTO userRequestDTO);
    void updateUserFromUserUpdateRequestDTO(User user, UserUpdateRequestDTO userUpdateRequestDTO);
}