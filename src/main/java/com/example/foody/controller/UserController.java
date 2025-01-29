package com.example.foody.controller;

import com.example.foody.dto.request.UserUpdateRequestDTO;
import com.example.foody.dto.response.UserResponseDTO;
import com.example.foody.exceptions.entity.EntityDeletionException;
import com.example.foody.exceptions.entity.EntityEditException;
import com.example.foody.exceptions.entity.EntityNotFoundException;
import com.example.foody.exceptions.google_drive.GoogleDriveFileDeleteException;
import com.example.foody.exceptions.google_drive.GoogleDriveFileUploadException;
import com.example.foody.model.user.User;
import com.example.foody.service.UserService;
import com.example.foody.utils.enums.Role;
import com.example.foody.utils.validator.value_of_enum.ValueOfEnum;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for handling user-related requests.
 */
@RestController
@RequestMapping("/api/v1/users")
@Validated
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * Retrieves all users.
     *
     * @return the response entity containing the list of user response data transfer objects
     */
    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getUsers() {
        return ResponseEntity.ok(userService.findAll());
    }

    /**
     * Retrieves a user by their ID.
     *
     * @param id the user ID
     * @return the response entity containing the user response data transfer object
     * @throws EntityNotFoundException if the user is not found
     */
    @GetMapping(path = "/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable long id)
            throws EntityNotFoundException {
        return ResponseEntity.ok(userService.findById(id));
    }

    /**
     * Retrieves a user by their email.
     *
     * @param email the user email
     * @return the response entity containing the user response data transfer object
     * @throws EntityNotFoundException if the user is not found
     */
    @GetMapping(path = "/email/{email}")
    public ResponseEntity<UserResponseDTO> getUserByEmail(@PathVariable String email)
            throws EntityNotFoundException {
        return ResponseEntity.ok(userService.findByEmail(email));
    }

    /**
     * Retrieves users by their role.
     *
     * @param role the user role
     * @return the response entity containing the list of user response data transfer objects
     */
    @GetMapping(path = "/role/{role}")
    public ResponseEntity<List<UserResponseDTO>> getUsersByRole(
            @PathVariable @ValueOfEnum(enumClass = Role.class, message = "Invalid role value") String role
    ) {
        return ResponseEntity.ok(userService.findByRole(Role.valueOf(role)));
    }

    /**
     * Updates a user.
     *
     * @param user                 the authenticated user
     * @param userUpdateRequestDTO the user update request data transfer object
     * @return the response entity containing the updated user response data transfer object
     * @throws GoogleDriveFileUploadException if there is an error uploading a file to Google Drive
     * @throws GoogleDriveFileDeleteException if there is an error deleting a file from Google Drive
     * @throws EntityEditException            if there is an error editing the user
     */
    @PutMapping()
    public ResponseEntity<UserResponseDTO> updateUser(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody UserUpdateRequestDTO userUpdateRequestDTO
    ) throws GoogleDriveFileUploadException, GoogleDriveFileDeleteException, EntityEditException {
        return ResponseEntity.ok(userService.update(user.getId(), userUpdateRequestDTO));
    }

    /**
     * Removes a user by their ID.
     *
     * @param id the user ID
     * @return the response entity with no content
     * @throws EntityNotFoundException        if the user is not found
     * @throws GoogleDriveFileDeleteException if there is an error deleting a file from Google Drive
     * @throws EntityDeletionException        if there is an error deleting the user
     */
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> removeUser(@PathVariable long id)
            throws EntityNotFoundException, GoogleDriveFileDeleteException, EntityDeletionException {
        userService.remove(id);
        return ResponseEntity.ok().build();
    }
}