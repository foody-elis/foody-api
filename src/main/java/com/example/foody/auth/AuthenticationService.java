package com.example.foody.auth;

import com.example.foody.dto.request.UserRequestDTO;
import com.example.foody.dto.response.UserResponseDTO;
import com.example.foody.exceptions.auth.InvalidCredentialsException;
import com.example.foody.exceptions.entity.EntityCreationException;
import com.example.foody.exceptions.entity.EntityDuplicateException;
import com.example.foody.exceptions.entity.EntityNotFoundException;
import com.example.foody.exceptions.user.UserNotActiveException;
import com.example.foody.mapper.UserMapper;
import com.example.foody.model.User;
import com.example.foody.repository.UserRepository;
import com.example.foody.security.JwtService;
import com.example.foody.utils.Role;
import jakarta.transaction.Transactional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional(rollbackOn = Exception.class)
public class AuthenticationService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationService(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    // todo implement CommandLineRunner been
    // another admin cannot be created, so I call the method only in the CommandLineRunner been
    public UserResponseDTO registerAdmin(UserRequestDTO userRequestDTO) {
        userRequestDTO.setRole(Role.ADMIN.name());
        return register(userRequestDTO);
    }

    public UserResponseDTO registerModerator(UserRequestDTO userRequestDTO) {
        userRequestDTO.setRole(Role.MODERATOR.name());
        return register(userRequestDTO);
    }

    public UserResponseDTO registerRestaurateur(UserRequestDTO userRequestDTO) {
        userRequestDTO.setRole(Role.RESTAURATEUR.name());
        return register(userRequestDTO);
    }

    public UserResponseDTO registerCook(UserRequestDTO userRequestDTO) {
        userRequestDTO.setRole(Role.COOK.name());
        return register(userRequestDTO);
    }

    public UserResponseDTO registerWaiter(UserRequestDTO userRequestDTO) {
        userRequestDTO.setRole(Role.WAITER.name());
        return register(userRequestDTO);
    }

    public UserResponseDTO registerCustomer(UserRequestDTO userRequestDTO) {
        userRequestDTO.setRole(Role.CUSTOMER.name());
        return register(userRequestDTO);
    }

    private UserResponseDTO register(UserRequestDTO userRequestDTO) {
        User user = userMapper.userRequestDTOToUser(userRequestDTO);

        // encode the password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        try {
            user = userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new EntityDuplicateException("user", "email", user.getEmail());
        } catch (Exception e) {
            throw new EntityCreationException("user");
        }

        // todo send email confirmation

        return userMapper.userToUserResponseDTO(user);
    }

    public TokenDTO authenticate(UserLoginDTO userLoginDTO) {
        User user = userRepository
                .findByEmail(userLoginDTO.getEmail())
                .orElseThrow(() -> new EntityNotFoundException("user", "email", userLoginDTO.getEmail()));

        if (!user.isActive()) {
            throw new UserNotActiveException(user.getEmail());
        }

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userLoginDTO.getEmail(), userLoginDTO.getPassword())
            );
        } catch (Exception e) {
            throw new InvalidCredentialsException();
        }

        String accessToken = jwtService.generateToken(user.getEmail());

        return new TokenDTO(accessToken);
    }

    // todo implement the refresh token mechanism

    // todo implement the logout mechanism
}
