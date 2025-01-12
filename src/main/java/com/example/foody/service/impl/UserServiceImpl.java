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
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(rollbackOn = Exception.class)
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UserHelper userHelper;
    private final GoogleDriveService googleDriveService;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, UserHelper userHelper, GoogleDriveService googleDriveService) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.userHelper = userHelper;
        this.googleDriveService = googleDriveService;
    }

    @Override
    public List<UserResponseDTO> findAll() {
        List<User> users = userRepository.findAll();
        return userHelper.buildUserResponseDTOs(users);
    }

    @Override
    public UserResponseDTO findById(long id) {
        User user = userRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("user", "id", id));
        return userHelper.buildUserResponseDTO(user);
    }

    @Override
    public UserResponseDTO findByEmail(String email) {
        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("user", "email", email));
        return userHelper.buildUserResponseDTO(user);
    }

    @Override
    public List<UserResponseDTO> findByRole(Role role) {
        List<User> users = userRepository.findByRole(role);
        return userHelper.buildUserResponseDTOs(users);
    }

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

    private String updateUserAvatar(User user, String userAvatarBase64) {
        removeUserAvatar(user);
        return saveUserAvatar(userAvatarBase64);
    }

    private String saveUserAvatar(String userAvatarBase64) {
        return Optional.ofNullable(userAvatarBase64)
                .map(avatarBase64 -> googleDriveService.uploadBase64Image(avatarBase64, GoogleDriveFileType.USER_AVATAR))
                .orElse(null);
    }

    private void removeUserAvatar(User user) {
        Optional.ofNullable(user.getAvatarUrl())
                .ifPresent(googleDriveService::deleteImage);
    }
}