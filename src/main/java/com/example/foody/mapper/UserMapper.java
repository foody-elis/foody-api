package com.example.foody.mapper;

import com.example.foody.dto.request.UserRequestDTO;
import com.example.foody.dto.request.UserUpdateRequestDTO;
import com.example.foody.dto.response.UserResponseDTO;
import com.example.foody.model.user.User;

/**
 * Mapper interface for converting between User entities and DTOs.
 */
public interface UserMapper {

    /**
     * Converts a User entity to a UserResponseDTO.
     *
     * @param user the User entity to convert
     * @param firebaseCustomToken the Firebase custom token associated with the user
     * @return the converted UserResponseDTO
     */
    UserResponseDTO userToUserResponseDTO(User user, String firebaseCustomToken);

    /**
     * Converts a UserRequestDTO to a User entity.
     *
     * @param userRequestDTO the UserRequestDTO to convert
     * @return the converted User entity
     */
    User userRequestDTOToUser(UserRequestDTO userRequestDTO);

    /**
     * Updates a User entity from a UserUpdateRequestDTO.
     *
     * @param user the User entity to update
     * @param userUpdateRequestDTO the UserUpdateRequestDTO with updated information
     */
    void updateUserFromUserUpdateRequestDTO(User user, UserUpdateRequestDTO userUpdateRequestDTO);
}