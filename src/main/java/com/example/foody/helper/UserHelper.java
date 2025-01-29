package com.example.foody.helper;

import com.example.foody.dto.response.UserResponseDTO;
import com.example.foody.model.user.User;

import java.util.List;

/**
 * Helper interface for building {@link UserResponseDTO} objects.
 */
public interface UserHelper {

    /**
     * Builds a {@link UserResponseDTO} from a given {@link User}.
     *
     * @param user the User object to convert
     * @return the constructed {@link UserResponseDTO}
     */
    UserResponseDTO buildUserResponseDTO(User user);

    /**
     * Builds a list of {@link UserResponseDTO} objects from a given list of {@link User} objects.
     *
     * @param users the list of User objects to convert
     * @return the list of constructed {@link UserResponseDTO} objects
     */
    List<UserResponseDTO> buildUserResponseDTOs(List<? extends User> users);
}