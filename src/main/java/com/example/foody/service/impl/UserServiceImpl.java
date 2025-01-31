package com.example.foody.service.impl;

import com.example.foody.dto.request.UserUpdateRequestDTO;
import com.example.foody.dto.response.UserResponseDTO;
import com.example.foody.exceptions.entity.EntityDeletionException;
import com.example.foody.exceptions.entity.EntityEditException;
import com.example.foody.exceptions.entity.EntityNotFoundException;
import com.example.foody.helper.UserHelper;
import com.example.foody.mapper.UserMapper;
import com.example.foody.model.user.User;
import com.example.foody.repository.UserRepository;
import com.example.foody.service.GoogleDriveService;
import com.example.foody.service.UserService;
import com.example.foody.utils.enums.GoogleDriveFileType;
import com.example.foody.utils.enums.Role;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of the UserService interface.
 * <p>
 * Provides methods to create, update, and delete {@link User} objects.
 */
@Service
@Transactional(rollbackOn = Exception.class)
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UserHelper userHelper;
    private final GoogleDriveService googleDriveService;

    /**
     * {@inheritDoc}
     * <p>
     * This method retrieves a {@link User} entity from the database.
     *
     * @return a list of user response data transfer objects
     */
    @Override
    public List<UserResponseDTO> findAll() {
        List<User> users = userRepository.findAll();
        return userHelper.buildUserResponseDTOs(users);
    }

    /**
     * {@inheritDoc}
     * <p>
     * This method retrieves a {@link User} entity from the database by ID.
     *
     * @param id the user ID
     * @return the user response data transfer object
     * @throws EntityNotFoundException if the user is not found
     */
    @Override
    public UserResponseDTO findById(long id) {
        User user = userRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("user", "id", id));
        return userHelper.buildUserResponseDTO(user);
    }

    /**
     * {@inheritDoc}
     * <p>
     * This method retrieves a {@link User} entity from the database by email.
     *
     * @param email the user email
     * @return the user response data transfer object
     * @throws EntityNotFoundException if the user is not found
     */
    @Override
    public UserResponseDTO findByEmail(String email) {
        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("user", "email", email));
        return userHelper.buildUserResponseDTO(user);
    }

    /**
     * {@inheritDoc}
     * <p>
     * This method retrieves a list of {@link User} entities from the database by role.
     *
     * @param role the user role
     * @return a list of user response data transfer objects
     */
    @Override
    public List<UserResponseDTO> findByRole(Role role) {
        List<User> users = userRepository.findByRole(role);
        return userHelper.buildUserResponseDTOs(users);
    }

    /**
     * {@inheritDoc}
     * <p>
     * This method updates a new {@link User} entity to the database.
     *
     * @param id                   the user ID
     * @param userUpdateRequestDTO the user update request data transfer object
     * @return the updated user response data transfer object
     * @throws EntityNotFoundException if the user is not found
     * @throws EntityEditException     if there is an error during user update
     */
    @Override
    public UserResponseDTO update(long id, UserUpdateRequestDTO userUpdateRequestDTO) {
        User user = userRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("user", "id", id));

        String updatedAvatarUrl = updateUserAvatar(user, userUpdateRequestDTO.getAvatarBase64());

        userMapper.updateUserFromUserUpdateRequestDTO(user, userUpdateRequestDTO);
        user.setAvatarUrl(updatedAvatarUrl);

        try {
            user = userRepository.save(user);
        } catch (Exception e) {
            throw new EntityEditException("user", "id", user.getId());
        }

        return userHelper.buildUserResponseDTO(user);
    }

    /**
     * {@inheritDoc}
     * <p>
     * This method removes a new {@link User} entity to the database.
     *
     * @param id the user ID
     * @return true if the user was successfully removed, false otherwise
     * @throws EntityNotFoundException if the user is not found
     * @throws EntityDeletionException if there is an error during user deletion
     */
    @Override
    public boolean remove(long id) {
        User user = userRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("user", "id", id));
        user.delete();

        removeUserAvatar(user);

        try {
            userRepository.save(user);
        } catch (Exception e) {
            throw new EntityDeletionException("user", "id", id);
        }

        return true;
    }

    /**
     * Updates the user's avatar.
     *
     * @param user             the user
     * @param userAvatarBase64 the user's avatar in base64 format
     * @return the updated avatar URL
     */
    private String updateUserAvatar(User user, String userAvatarBase64) {
        removeUserAvatar(user);
        return saveUserAvatar(userAvatarBase64);
    }

    /**
     * Saves the user's avatar.
     *
     * @param userAvatarBase64 the user's avatar in base64 format
     * @return the saved avatar URL
     */
    private String saveUserAvatar(String userAvatarBase64) {
        return Optional.ofNullable(userAvatarBase64)
                .map(avatarBase64 -> googleDriveService.uploadBase64Image(avatarBase64, GoogleDriveFileType.USER_AVATAR))
                .orElse(null);
    }

    /**
     * Removes the user's avatar.
     *
     * @param user the user
     */
    private void removeUserAvatar(User user) {
        Optional.ofNullable(user.getAvatarUrl())
                .ifPresent(googleDriveService::deleteImage);
    }
}