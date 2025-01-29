package com.example.foody.controller;

import com.example.foody.dto.request.UserChangePasswordRequestDTO;
import com.example.foody.dto.request.UserLoginRequestDTO;
import com.example.foody.dto.request.UserRequestDTO;
import com.example.foody.dto.response.EmployeeUserResponseDTO;
import com.example.foody.dto.response.TokenResponseDTO;
import com.example.foody.dto.response.UserResponseDTO;
import com.example.foody.exceptions.auth.InvalidCredentialsException;
import com.example.foody.exceptions.entity.EntityCreationException;
import com.example.foody.exceptions.entity.EntityDuplicateException;
import com.example.foody.exceptions.entity.EntityEditException;
import com.example.foody.exceptions.entity.EntityNotFoundException;
import com.example.foody.exceptions.google_drive.GoogleDriveFileUploadException;
import com.example.foody.exceptions.restaurant.ForbiddenRestaurantAccessException;
import com.example.foody.exceptions.user.InvalidPasswordException;
import com.example.foody.exceptions.user.UserNotActiveException;
import com.example.foody.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for handling authentication-related requests.
 */
@RestController
@RequestMapping("/api/v1/auth")
@AllArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    /**
     * Registers a new admin user.
     *
     * @param userRequestDTO the user request data transfer object
     * @return the response entity containing the user response data transfer object
     * @throws GoogleDriveFileUploadException if there is an error uploading a file to Google Drive
     * @throws EntityDuplicateException       if the entity already exists
     * @throws EntityCreationException        if there is an error creating the entity
     */
    @PostMapping("/admins")
    public ResponseEntity<UserResponseDTO> registerAdmin(@Valid @RequestBody UserRequestDTO userRequestDTO)
            throws GoogleDriveFileUploadException, EntityDuplicateException, EntityCreationException {
        UserResponseDTO userResponseDTO = authenticationService.registerAdmin(userRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(userResponseDTO);
    }

    /**
     * Registers a new moderator user.
     *
     * @param userRequestDTO the user request data transfer object
     * @return the response entity containing the user response data transfer object
     * @throws GoogleDriveFileUploadException if there is an error uploading a file to Google Drive
     * @throws EntityDuplicateException       if the entity already exists
     * @throws EntityCreationException        if there is an error creating the entity
     */
    @PostMapping("/moderators")
    public ResponseEntity<UserResponseDTO> registerModerator(@Valid @RequestBody UserRequestDTO userRequestDTO)
            throws GoogleDriveFileUploadException, EntityDuplicateException, EntityCreationException {
        UserResponseDTO userResponseDTO = authenticationService.registerModerator(userRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(userResponseDTO);
    }

    /**
     * Registers and authenticates a new restaurateur user.
     *
     * @param userRequestDTO the user request data transfer object
     * @return the response entity containing the token response data transfer object
     * @throws GoogleDriveFileUploadException if there is an error uploading a file to Google Drive
     * @throws EntityDuplicateException       if the entity already exists
     * @throws EntityCreationException        if there is an error creating the entity
     * @throws EntityNotFoundException        if the entity is not found
     * @throws UserNotActiveException         if the user is not active
     * @throws InvalidCredentialsException    if the credentials are invalid
     */
    @PostMapping("/restaurateurs")
    public ResponseEntity<TokenResponseDTO> registerAndAuthenticateRestaurateur(
            @Valid @RequestBody UserRequestDTO userRequestDTO
    ) throws GoogleDriveFileUploadException, EntityDuplicateException, EntityCreationException, EntityNotFoundException,
            UserNotActiveException, InvalidCredentialsException {
        TokenResponseDTO tokenResponseDTO = authenticationService.registerAndAuthenticateRestaurateur(userRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(tokenResponseDTO);
    }

    /**
     * Registers a new cook for a specific restaurant.
     *
     * @param restaurantId   the restaurant ID
     * @param userRequestDTO the user request data transfer object
     * @return the response entity containing the employee user response data transfer object
     * @throws GoogleDriveFileUploadException     if there is an error uploading a file to Google Drive
     * @throws EntityDuplicateException           if the entity already exists
     * @throws EntityCreationException            if there is an error creating the entity
     * @throws EntityNotFoundException            if the entity is not found
     * @throws ForbiddenRestaurantAccessException if access to the restaurant is forbidden
     */
    @PostMapping("/restaurant/{restaurant-id}/cooks")
    public ResponseEntity<EmployeeUserResponseDTO> registerCook(
            @PathVariable("restaurant-id") long restaurantId,
            @Valid @RequestBody UserRequestDTO userRequestDTO
    ) throws GoogleDriveFileUploadException, EntityDuplicateException, EntityCreationException,
            EntityNotFoundException, ForbiddenRestaurantAccessException {
        EmployeeUserResponseDTO employeeUserResponseDTO = authenticationService.registerCook(restaurantId, userRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(employeeUserResponseDTO);
    }

    /**
     * Registers a new waiter for a specific restaurant.
     *
     * @param restaurantId   the restaurant ID
     * @param userRequestDTO the user request data transfer object
     * @return the response entity containing the employee user response data transfer object
     * @throws GoogleDriveFileUploadException     if there is an error uploading a file to Google Drive
     * @throws EntityDuplicateException           if the entity already exists
     * @throws EntityCreationException            if there is an error creating the entity
     * @throws EntityNotFoundException            if the entity is not found
     * @throws ForbiddenRestaurantAccessException if access to the restaurant is forbidden
     */
    @PostMapping("/restaurant/{restaurant-id}/waiters")
    public ResponseEntity<EmployeeUserResponseDTO> registerWaiter(
            @PathVariable("restaurant-id") long restaurantId,
            @Valid @RequestBody UserRequestDTO userRequestDTO
    ) throws GoogleDriveFileUploadException, EntityDuplicateException, EntityCreationException,
            EntityNotFoundException, ForbiddenRestaurantAccessException {
        EmployeeUserResponseDTO employeeUserResponseDTO = authenticationService.registerWaiter(restaurantId, userRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(employeeUserResponseDTO);
    }

    /**
     * Registers and authenticates a new customer user.
     *
     * @param userRequestDTO the user request data transfer object
     * @return the response entity containing the token response data transfer object
     * @throws GoogleDriveFileUploadException if there is an error uploading a file to Google Drive
     * @throws EntityDuplicateException       if the entity already exists
     * @throws EntityCreationException        if there is an error creating the entity
     * @throws EntityNotFoundException        if the entity is not found
     * @throws UserNotActiveException         if the user is not active
     * @throws InvalidCredentialsException    if the credentials are invalid
     */
    @PostMapping("/customers")
    public ResponseEntity<TokenResponseDTO> registerAndAuthenticateCustomer(
            @Valid @RequestBody UserRequestDTO userRequestDTO
    ) throws GoogleDriveFileUploadException, EntityDuplicateException, EntityCreationException,
            EntityNotFoundException, UserNotActiveException, InvalidCredentialsException {
        TokenResponseDTO tokenResponseDTO = authenticationService.registerAndAuthenticateCustomer(userRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(tokenResponseDTO);
    }

    /**
     * Authenticates a user.
     *
     * @param userLoginRequestDTO the user login request data transfer object
     * @return the response entity containing the token response data transfer object
     * @throws EntityNotFoundException     if the entity is not found
     * @throws UserNotActiveException      if the user is not active
     * @throws InvalidCredentialsException if the credentials are invalid
     */
    @PostMapping("/login")
    public ResponseEntity<TokenResponseDTO> authenticateUser(
            @Valid @RequestBody UserLoginRequestDTO userLoginRequestDTO
    ) throws EntityNotFoundException, UserNotActiveException, InvalidCredentialsException {
        TokenResponseDTO tokenResponseDTO = authenticationService.authenticate(userLoginRequestDTO);
        return ResponseEntity.ok(tokenResponseDTO);
    }

    /**
     * Retrieves the authenticated user.
     *
     * @return the response entity containing the user response data transfer object
     */
    @GetMapping("/user")
    public ResponseEntity<UserResponseDTO> getAuthenticatedUser() {
        UserResponseDTO userResponseDTO = authenticationService.getAuthenticatedUser();
        return ResponseEntity.ok(userResponseDTO);
    }

    /**
     * Changes the password of the authenticated user.
     *
     * @param userChangePasswordRequestDTO the user change password request data transfer object
     * @return the response entity with no content
     * @throws InvalidPasswordException if the password is invalid
     * @throws EntityEditException      if there is an error editing the entity
     */
    @PatchMapping(path = "/change-password")
    public ResponseEntity<Void> changePassword(
            @Valid @RequestBody UserChangePasswordRequestDTO userChangePasswordRequestDTO
    ) throws InvalidPasswordException, EntityEditException {
        authenticationService.changePassword(userChangePasswordRequestDTO);
        return ResponseEntity.ok().build();
    }
}