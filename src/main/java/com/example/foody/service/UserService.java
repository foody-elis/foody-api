package com.example.foody.service;

import com.example.foody.dto.request.UserChangePasswordRequestDTO;
import com.example.foody.dto.request.UserUpdateRequestDTO;
import com.example.foody.dto.response.UserResponseDTO;
import com.example.foody.utils.enums.Role;

import java.util.List;

public interface UserService {
    List<UserResponseDTO> findAll();
    UserResponseDTO findById(long id);
    UserResponseDTO findByEmail(String email);
    List<UserResponseDTO> findByRole(Role role);
    UserResponseDTO update(UserUpdateRequestDTO userUpdateRequestDTO);
    void changePassword(UserChangePasswordRequestDTO userChangePasswordRequestDTO);
    boolean remove(long id);
}