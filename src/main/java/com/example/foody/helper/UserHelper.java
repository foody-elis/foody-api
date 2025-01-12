package com.example.foody.helper;

import com.example.foody.dto.response.UserResponseDTO;
import com.example.foody.model.user.User;

import java.util.List;

public interface UserHelper {
    UserResponseDTO buildUserResponseDTO(User user);
    List<UserResponseDTO> buildUserResponseDTOs(List<? extends User> users);
}