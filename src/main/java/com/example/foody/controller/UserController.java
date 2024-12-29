package com.example.foody.controller;

import com.example.foody.dto.request.UserChangePasswordRequestDTO;
import com.example.foody.dto.request.UserUpdateRequestDTO;
import com.example.foody.dto.response.UserResponseDTO;
import com.example.foody.exceptions.entity.EntityDeletionException;
import com.example.foody.exceptions.entity.EntityEditException;
import com.example.foody.exceptions.entity.EntityNotFoundException;
import com.example.foody.exceptions.google_drive.GoogleDriveFileDeleteException;
import com.example.foody.exceptions.google_drive.GoogleDriveFileUploadException;
import com.example.foody.exceptions.user.InvalidPasswordException;
import com.example.foody.service.UserService;
import com.example.foody.utils.enums.Role;
import com.example.foody.utils.validator.value_of_enum.ValueOfEnum;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getUsers() {
        return new ResponseEntity<>(userService.findAll(), HttpStatus.OK);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable long id)
            throws EntityNotFoundException {
        return new ResponseEntity<>(userService.findById(id), HttpStatus.OK);
    }

    @GetMapping(path = "/email/{email}")
    public ResponseEntity<UserResponseDTO> getUserByEmail(@PathVariable String email)
            throws EntityNotFoundException {
        return new ResponseEntity<>(userService.findByEmail(email), HttpStatus.OK);
    }

    @GetMapping(path = "/role/{role}")
    public ResponseEntity<List<UserResponseDTO>> getUsersByRole(
            @PathVariable @ValueOfEnum(enumClass = Role.class, message = "Invalid role value") String role
    ) {
        return new ResponseEntity<>(userService.findByRole(Role.valueOf(role)), HttpStatus.OK);
    }

    @PutMapping()
    public ResponseEntity<UserResponseDTO> updateUser(@Valid @RequestBody UserUpdateRequestDTO userUpdateRequestDTO)
            throws GoogleDriveFileUploadException, GoogleDriveFileDeleteException, EntityEditException {
        return new ResponseEntity<>(userService.update(userUpdateRequestDTO), HttpStatus.OK);
    }

    @PatchMapping(path = "/change-password")
    public ResponseEntity<Void> changePassword(@Valid @RequestBody UserChangePasswordRequestDTO userChangePasswordRequestDTO)
            throws InvalidPasswordException, EntityEditException {
        userService.changePassword(userChangePasswordRequestDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> removeUser(@PathVariable long id)
            throws EntityNotFoundException, GoogleDriveFileDeleteException, EntityDeletionException {
        userService.remove(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}