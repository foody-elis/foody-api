package com.example.foody.service;

import com.example.foody.dto.request.UserUpdateRequestDTO;
import com.example.foody.dto.response.UserResponseDTO;
import com.example.foody.utils.enums.Role;

import java.util.List;

/**
 * Service interface for managing users.
 */
public interface UserService {

    /**
     * Retrieves all users.
     *
     * @return a list of all user response data transfer objects
     */
    List<UserResponseDTO> findAll();

    /**
     * Finds a user by their ID.
     *
     * @param id the ID of the user to find
     * @return the found user response data transfer object
     */
    UserResponseDTO findById(long id);

    /**
     * Finds a user by their email.
     *
     * @param email the email of the user to find
     * @return the found user response data transfer object
     */
    UserResponseDTO findByEmail(String email);

    /**
     * Finds all users by their role.
     *
     * @param role the role of the users to find
     * @return a list of user response data transfer objects for the specified role
     */
    List<UserResponseDTO> findByRole(Role role);

    /**
     * Updates a user by their ID.
     *
     * @param id the ID of the user to update
     * @param userUpdateRequestDTO the user data transfer object containing updated user details
     * @return the updated user response data transfer object
     */
    UserResponseDTO update(long id, UserUpdateRequestDTO userUpdateRequestDTO);

    /**
     * Removes a user by their ID.
     *
     * @param id the ID of the user to remove
     * @return true if the user was successfully removed, false otherwise
     */
    boolean remove(long id);
}