package com.example.foody.mapper;

import com.example.foody.dto.request.UserRequestDTO;
import com.example.foody.dto.response.UserResponseDTO;
import com.example.foody.model.user.User;

import java.util.List;

public interface UserMapper {
    UserResponseDTO userToUserResponseDTO(User user);
    User userRequestDTOToUser(UserRequestDTO userRequestDTO);
    List<UserResponseDTO> usersToUserResponseDTOs(List<User> users);
}
