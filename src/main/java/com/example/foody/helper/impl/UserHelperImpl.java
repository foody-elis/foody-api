package com.example.foody.helper.impl;

import com.example.foody.dto.response.UserResponseDTO;
import com.example.foody.exceptions.entity.EntityEditException;
import com.example.foody.helper.UserHelper;
import com.example.foody.mapper.UserMapper;
import com.example.foody.model.user.User;
import com.example.foody.repository.UserRepository;
import com.example.foody.service.FirebaseService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserHelperImpl implements UserHelper {
    private final FirebaseService firebaseService;
    private final UserMapper userMapper;
    private final UserRepository userRepository;

    public UserHelperImpl(FirebaseService firebaseService, UserMapper userMapper, UserRepository userRepository) {
        this.firebaseService = firebaseService;
        this.userMapper = userMapper;
        this.userRepository = userRepository;
    }

    @Override
    public UserResponseDTO buildUserResponseDTO(User user) {
        String firebaseCustomToken = getFirebaseCustomToken(user);
        return userMapper.userToUserResponseDTO(user, firebaseCustomToken);
    }

    @Override
    public List<UserResponseDTO> buildUserResponseDTOs(List<? extends User> users) {
        if (users == null) {
            return null;
        }

        List<UserResponseDTO> list = new ArrayList<>(users.size());
        users.forEach(user -> list.add(buildUserResponseDTO(user)));

        return list;
    }

    private String getFirebaseCustomToken(User user) {
        if (user == null) {
            return null;
        }
        return user.getFirebaseCustomToken() == null
                ? updateUserFirebaseCustomToken(user)
                : user.getFirebaseCustomToken();
    }

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