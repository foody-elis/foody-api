package com.example.foody.service.impl;

import com.example.foody.dto.request.UserChangePasswordRequestDTO;
import com.example.foody.dto.request.UserUpdateRequestDTO;
import com.example.foody.dto.response.UserResponseDTO;
import com.example.foody.exceptions.entity.EntityDeletionException;
import com.example.foody.exceptions.entity.EntityEditException;
import com.example.foody.exceptions.entity.EntityNotFoundException;
import com.example.foody.exceptions.user.InvalidPasswordException;
import com.example.foody.mapper.UserMapper;
import com.example.foody.model.user.User;
import com.example.foody.repository.UserRepository;
import com.example.foody.service.EmailService;
import com.example.foody.service.GoogleDriveService;
import com.example.foody.service.UserService;
import com.example.foody.utils.enums.EmailPlaceholder;
import com.example.foody.utils.enums.EmailTemplateType;
import com.example.foody.utils.enums.GoogleDriveFileType;
import com.example.foody.utils.enums.Role;
import jakarta.transaction.Transactional;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional(rollbackOn = Exception.class)
public class UserServiceImp implements UserService {
    private final UserRepository userRepository;
    private final UserMapper<User> userMapper;
    private final PasswordEncoder passwordEncoder;
    private final GoogleDriveService googleDriveService;
    private final EmailService emailService;

    public UserServiceImp(UserRepository userRepository, UserMapper<User> userMapper, PasswordEncoder passwordEncoder, GoogleDriveService googleDriveService, EmailService emailService) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.googleDriveService = googleDriveService;
        this.emailService = emailService;
    }

    @Override
    public List<UserResponseDTO> findAll() {
        List<User> users = userRepository.findAllByDeletedAtIsNull();
        return userMapper.usersToUserResponseDTOs(users);
    }

    @Override
    public UserResponseDTO findById(long id) {
        User user = userRepository
                .findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new EntityNotFoundException("user", "id", id));
        return userMapper.userToUserResponseDTO(user);
    }

    @Override
    public UserResponseDTO findByEmail(String email) {
        User user = userRepository
                .findByEmailAndDeletedAtIsNull(email)
                .orElseThrow(() -> new EntityNotFoundException("user", "email", email));
        return userMapper.userToUserResponseDTO(user);
    }

    @Override
    public List<UserResponseDTO> findByRole(Role role) {
        List<User> users = userRepository.findByRoleAndDeletedAtIsNull(role);
        return userMapper.usersToUserResponseDTOs(users);
    }

    @Override
    public UserResponseDTO update(UserUpdateRequestDTO userUpdateRequestDTO) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String updatedAvatarUrl = updateUserAvatar(user, userUpdateRequestDTO.getAvatarBase64());

        userMapper.updateUserFromUserUpdateRequestDTO(user, userUpdateRequestDTO);
        user.setAvatarUrl(updatedAvatarUrl);

        try {
            user = userRepository.save(user);
        } catch (Exception e) {
            throw new EntityEditException("user", "id", user.getId());
        }

        return userMapper.userToUserResponseDTO(user);
    }

    @Override
    public void changePassword(UserChangePasswordRequestDTO userChangePasswordRequestDTO) {
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!passwordEncoder.matches(userChangePasswordRequestDTO.getCurrentPassword(), principal.getPassword())) {
            throw new InvalidPasswordException();
        }

        principal.setPassword(passwordEncoder.encode(userChangePasswordRequestDTO.getNewPassword()));

        try {
            userRepository.save(principal);
        } catch (Exception e) {
            throw new EntityEditException("user", "id", principal.getId());
        }

        sendChangePasswordEmail(principal);
    }

    @Override
    public boolean remove(long id) {
        User user = userRepository
                .findByIdAndDeletedAtIsNull(id)
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

    private void sendChangePasswordEmail(User user) {
        Map<EmailPlaceholder, Object> variables = Map.of(
                EmailPlaceholder.NAME, user.getName(),
                EmailPlaceholder.SURNAME, user.getSurname()
        );
        emailService.sendTemplatedEmail(
                user.getEmail(),
                EmailTemplateType.CHANGE_PASSWORD,
                variables
        );
    }
}