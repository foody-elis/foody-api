package com.example.foody.helper.impl;

import com.example.foody.dto.response.UserResponseDTO;
import com.example.foody.exceptions.entity.EntityEditException;
import com.example.foody.helper.UserHelper;
import com.example.foody.mapper.UserMapper;
import com.example.foody.model.user.User;
import com.example.foody.repository.UserRepository;
import com.example.foody.service.FirebaseService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Implementation of the {@link UserHelper} interface.
 * <p>
 * Provides methods to build {@link UserResponseDTO} objects from {@link User} objects.
 */
@Component
@AllArgsConstructor
public class UserHelperImpl implements UserHelper {

    private final FirebaseService firebaseService;
    private final UserMapper userMapper;
    private final UserRepository userRepository;

    /**
     * {@inheritDoc}
     * <p>
     * This method retrieves the Firebase custom token for the user and maps it to a {@link UserResponseDTO}.
     *
     * @param user the User object to convert
     * @return the constructed {@link UserResponseDTO}
     */
    @Override
    public UserResponseDTO buildUserResponseDTO(User user) {
        String firebaseCustomToken = getFirebaseCustomToken(user);
        return userMapper.userToUserResponseDTO(user, firebaseCustomToken);
    }

    /**
     * {@inheritDoc}
     * <p>
     * This method converts a list of {@link User} objects to a list of {@link UserResponseDTO} objects.
     *
     * @param users the list of User objects to convert
     * @return the list of constructed {@link UserResponseDTO} objects
     */
    @Override
    public List<UserResponseDTO> buildUserResponseDTOs(List<? extends User> users) {
        return users.stream()
                .map(this::buildUserResponseDTO)
                .toList();
    }

    /**
     * Retrieves the Firebase custom token for the given user.
     * <p>
     * If the token is invalid or not present, it updates the token.
     *
     * @param user the User object
     * @return the Firebase custom token
     */
    private String getFirebaseCustomToken(User user) {
        if (user == null) {
            return null;
        }

        String customToken = user.getFirebaseCustomToken();

        if (customToken == null || !firebaseService.verifyToken(customToken)) {
            return updateUserFirebaseCustomToken(user);
        }

        return customToken;
    }

    /**
     * Updates the Firebase custom token for the given user and saves the user.
     *
     * @param user the User object
     * @return the new Firebase custom token
     * @throws EntityEditException if there is an error saving the user
     */
    private String updateUserFirebaseCustomToken(User user) {
        if (user == null) {
            return null;
        }

        String firebaseCustomToken = firebaseService.createCustomToken(user.getEmail());
        user.setFirebaseCustomToken(firebaseCustomToken);

        try {
            userRepository.save(user);
        } catch (Exception e) {
            throw new EntityEditException("user", "id", user.getId());
        }

        return firebaseCustomToken;
    }
}