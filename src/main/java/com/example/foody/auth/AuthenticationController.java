package com.example.foody.auth;

import com.example.foody.dto.request.UserRequestDTO;
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

    @PostMapping("/register-moderator")
    public ResponseEntity<UserResponseDTO> registerModerator(@Valid @RequestBody UserRequestDTO userRequestDTO) throws EntityDuplicateException, EntityCreationException {
        return new ResponseEntity<>(authenticationService.registerModerator(userRequestDTO), HttpStatus.CREATED);
    }

    @PostMapping("/register-restaurateur")
    public ResponseEntity<UserResponseDTO> registerRestaurateur(@Valid @RequestBody UserRequestDTO userRequestDTO) throws EntityDuplicateException, EntityCreationException {
        return new ResponseEntity<>(authenticationService.registerRestaurateur(userRequestDTO), HttpStatus.CREATED);
    }

    @PostMapping("/register-cook")
    public ResponseEntity<UserResponseDTO> registerCook(@Valid @RequestBody UserRequestDTO userRequestDTO) throws EntityDuplicateException, EntityCreationException {
        return new ResponseEntity<>(authenticationService.registerCook(userRequestDTO), HttpStatus.CREATED);
    }

    @PostMapping("/register-waiter")
    public ResponseEntity<UserResponseDTO> registerWaiter(@Valid @RequestBody UserRequestDTO userRequestDTO) throws EntityDuplicateException, EntityCreationException {
        return new ResponseEntity<>(authenticationService.registerWaiter(userRequestDTO), HttpStatus.CREATED);
    }

    @PostMapping("/register-customer")
    public ResponseEntity<UserResponseDTO> registerCustomer(@Valid @RequestBody UserRequestDTO userRequestDTO) throws EntityDuplicateException, EntityCreationException {
        return new ResponseEntity<>(authenticationService.registerCustomer(userRequestDTO), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenDTO> authenticateUser(@Valid @RequestBody UserLoginDTO userLoginDTO) throws EntityNotFoundException, UserNotActiveException, InvalidCredentialsException {
        return new ResponseEntity<>(authenticationService.authenticate(userLoginDTO), HttpStatus.OK);
    }

    @GetMapping("/logged-user")
    public ResponseEntity<UserResponseDTO> getLoggedUser() {
        return new ResponseEntity<>(authenticationService.getLoggedUser(), HttpStatus.OK);
    }
}
