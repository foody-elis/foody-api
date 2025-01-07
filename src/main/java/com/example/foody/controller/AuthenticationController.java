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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/admins")
    public ResponseEntity<UserResponseDTO> registerAdmin(@Valid @RequestBody UserRequestDTO userRequestDTO)
            throws GoogleDriveFileUploadException, EntityDuplicateException, EntityCreationException {
        return new ResponseEntity<>(authenticationService.registerAdmin(userRequestDTO), HttpStatus.CREATED);
    }

    @PostMapping("/moderators")
    public ResponseEntity<UserResponseDTO> registerModerator(@Valid @RequestBody UserRequestDTO userRequestDTO)
            throws GoogleDriveFileUploadException, EntityDuplicateException, EntityCreationException {
        return new ResponseEntity<>(authenticationService.registerModerator(userRequestDTO), HttpStatus.CREATED);
    }

    @PostMapping("/restaurateurs")
    public ResponseEntity<TokenResponseDTO> registerAndAuthenticateRestaurateur(@Valid @RequestBody UserRequestDTO userRequestDTO)
            throws GoogleDriveFileUploadException, EntityDuplicateException, EntityCreationException, EntityNotFoundException, UserNotActiveException, InvalidCredentialsException {
        return new ResponseEntity<>(authenticationService.registerAndAuthenticateRestaurateur(userRequestDTO), HttpStatus.CREATED);
    }

    @PostMapping("/restaurant/{restaurant-id}/cooks")
    public ResponseEntity<EmployeeUserResponseDTO> registerCook(@PathVariable("restaurant-id") long restaurantId, @Valid @RequestBody UserRequestDTO userRequestDTO)
            throws GoogleDriveFileUploadException, EntityDuplicateException, EntityCreationException, EntityNotFoundException, ForbiddenRestaurantAccessException {
        return new ResponseEntity<>(authenticationService.registerCook(restaurantId, userRequestDTO), HttpStatus.CREATED);
    }

    @PostMapping("/restaurant/{restaurant-id}/waiters")
    public ResponseEntity<EmployeeUserResponseDTO> registerWaiter(@PathVariable("restaurant-id") long restaurantId, @Valid @RequestBody UserRequestDTO userRequestDTO)
            throws GoogleDriveFileUploadException, EntityDuplicateException, EntityCreationException, EntityNotFoundException, ForbiddenRestaurantAccessException {
        return new ResponseEntity<>(authenticationService.registerWaiter(restaurantId, userRequestDTO), HttpStatus.CREATED);
    }

    @PostMapping("/customers")
    public ResponseEntity<TokenResponseDTO> registerAndAuthenticateCustomer(@Valid @RequestBody UserRequestDTO userRequestDTO)
            throws GoogleDriveFileUploadException, EntityDuplicateException, EntityCreationException, EntityNotFoundException, UserNotActiveException, InvalidCredentialsException {
        return new ResponseEntity<>(authenticationService.registerAndAuthenticateCustomer(userRequestDTO), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDTO> authenticateUser(@Valid @RequestBody UserLoginRequestDTO userLoginRequestDTO)
            throws EntityNotFoundException, UserNotActiveException, InvalidCredentialsException {
        return new ResponseEntity<>(authenticationService.authenticate(userLoginRequestDTO), HttpStatus.OK);
    }

    @GetMapping("/user")
    public ResponseEntity<UserResponseDTO> getAuthenticatedUser() {
        return new ResponseEntity<>(authenticationService.getAuthenticatedUser(), HttpStatus.OK);
    }

    @PatchMapping(path = "/change-password")
    public ResponseEntity<Void> changePassword(@Valid @RequestBody UserChangePasswordRequestDTO userChangePasswordRequestDTO)
            throws InvalidPasswordException, EntityEditException {
        authenticationService.changePassword(userChangePasswordRequestDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}