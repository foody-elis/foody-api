package com.example.foody.auth;

import com.example.foody.auth.impl.AuthenticationServiceImpl;
import com.example.foody.dto.request.UserRequestDTO;
import com.example.foody.dto.response.EmployeeUserResponseDTO;
import com.example.foody.dto.response.UserResponseDTO;
import com.example.foody.exceptions.auth.InvalidCredentialsException;
import com.example.foody.exceptions.entity.EntityCreationException;
import com.example.foody.exceptions.entity.EntityDuplicateException;
import com.example.foody.exceptions.entity.EntityNotFoundException;
import com.example.foody.exceptions.user.UserNotActiveException;
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

    // todo manage exceptions
    // todo manage in security config

    @PostMapping("/admins")
    public ResponseEntity<UserResponseDTO> registerAdmin(@Valid @RequestBody UserRequestDTO userRequestDTO)
            throws EntityDuplicateException, EntityCreationException {
        return new ResponseEntity<>(authenticationService.registerAdmin(userRequestDTO), HttpStatus.CREATED);
    }

    @PostMapping("/moderators")
    public ResponseEntity<UserResponseDTO> registerModerator(@Valid @RequestBody UserRequestDTO userRequestDTO)
            throws EntityDuplicateException, EntityCreationException {
        return new ResponseEntity<>(authenticationService.registerModerator(userRequestDTO), HttpStatus.CREATED);
    }

    @PostMapping("/restaurateurs")
    public ResponseEntity<TokenDTO> registerRestaurateur(@Valid @RequestBody UserRequestDTO userRequestDTO)
            throws EntityDuplicateException, EntityCreationException {
        return new ResponseEntity<>(authenticationService.registerAndAuthenticateRestaurateur(userRequestDTO), HttpStatus.CREATED);
    }

    @PostMapping("/restaurant/{restaurant-id}/employees")
    public ResponseEntity<EmployeeUserResponseDTO> registerEmployee(@PathVariable("restaurant-id") long restaurantId, @Valid @RequestBody UserRequestDTO userRequestDTO)
            throws EntityDuplicateException, EntityCreationException {
        return new ResponseEntity<>(authenticationService.registerEmployee(restaurantId, userRequestDTO), HttpStatus.CREATED);
    }

    @PostMapping("/customers")
    public ResponseEntity<TokenDTO> registerCustomer(@Valid @RequestBody UserRequestDTO userRequestDTO)
            throws EntityDuplicateException, EntityCreationException {
        return new ResponseEntity<>(authenticationService.registerAndAuthenticateCustomer(userRequestDTO), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenDTO> authenticateUser(@Valid @RequestBody UserLoginDTO userLoginDTO)
            throws EntityNotFoundException, UserNotActiveException, InvalidCredentialsException {
        return new ResponseEntity<>(authenticationService.authenticate(userLoginDTO), HttpStatus.OK);
    }
}
