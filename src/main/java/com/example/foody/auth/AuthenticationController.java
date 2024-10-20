package com.example.foody.auth;

import com.example.foody.auth.impl.AuthenticationServiceImpl;
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
    private final AuthenticationServiceImpl authenticationServiceImpl;

    public AuthenticationController(AuthenticationServiceImpl authenticationServiceImpl) {
        this.authenticationServiceImpl = authenticationServiceImpl;
    }

    @PostMapping("/register-moderator")
    public ResponseEntity<UserResponseDTO> registerModerator(@Valid @RequestBody UserRequestDTO userRequestDTO)
            throws EntityDuplicateException, EntityCreationException {
        return new ResponseEntity<>(authenticationServiceImpl.registerModerator(userRequestDTO), HttpStatus.CREATED);
    }

    @PostMapping("/register-restaurateur")
    public ResponseEntity<UserResponseDTO> registerRestaurateur(@Valid @RequestBody UserRequestDTO userRequestDTO)
            throws EntityDuplicateException, EntityCreationException {
        return new ResponseEntity<>(authenticationServiceImpl.registerRestaurateur(userRequestDTO), HttpStatus.CREATED);
    }

    @PostMapping("/register-cook")
    public ResponseEntity<UserResponseDTO> registerCook(@Valid @RequestBody UserRequestDTO userRequestDTO)
            throws EntityDuplicateException, EntityCreationException {
        return new ResponseEntity<>(authenticationServiceImpl.registerCook(userRequestDTO), HttpStatus.CREATED);
    }

    @PostMapping("/register-waiter")
    public ResponseEntity<UserResponseDTO> registerWaiter(@Valid @RequestBody UserRequestDTO userRequestDTO)
            throws EntityDuplicateException, EntityCreationException {
        return new ResponseEntity<>(authenticationServiceImpl.registerWaiter(userRequestDTO), HttpStatus.CREATED);
    }

    @PostMapping("/register-customer")
    public ResponseEntity<UserResponseDTO> registerCustomer(@Valid @RequestBody UserRequestDTO userRequestDTO)
            throws EntityDuplicateException, EntityCreationException {
        return new ResponseEntity<>(authenticationServiceImpl.registerCustomer(userRequestDTO), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenDTO> authenticateUser(@Valid @RequestBody UserLoginDTO userLoginDTO)
            throws EntityNotFoundException, UserNotActiveException, InvalidCredentialsException {
        return new ResponseEntity<>(authenticationServiceImpl.authenticate(userLoginDTO), HttpStatus.OK);
    }
}
