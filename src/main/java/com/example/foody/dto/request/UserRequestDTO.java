package com.example.foody.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDTO {
    @Email(message = "email is not a well-formed email address")
    @NotBlank(message = "email cannot be blank")
    @Size(min = 1, max = 320, message = "email cannot be less than 1 character or more than 320 characters long")
    private String email;

    @NotBlank(message = "password cannot be blank")
    @Size(min = 8, max = 100, message = "password cannot be less than 8 characters or more than 100 characters long")
    private String password;

    @NotBlank(message = "name cannot be blank")
    @Size(min = 1, max = 30, message = "name cannot be less than 1 character or more than 30 characters long")
    private String name;

    @NotBlank(message = "surname cannot be blank")
    @Size(min = 1, max = 30, message = "surname cannot be less than 1 character or more than 30 characters long")
    private String surname;

    @NotNull(message = "birthDate cannot be null")
    @Past(message = "birthDate cannot be in the future")
    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate birthDate;

    @NotBlank(message = "phoneNumber cannot be blank")
    @Size(min = 1, max = 16, message = "phoneNumber cannot be less than 1 character or more than 16 characters long")
    @Pattern(regexp = "^\\+?[0-9\\s\\-()]{7,15}$", message = "phoneNumber must be a valid format, including optional country code, and contain only digits, spaces, dashes, or brackets")
    private String phoneNumber;

    @NotBlank(message = "avatar cannot be blank")
    @Size(min = 1, max = 255, message = "avatar cannot be less than 1 character or more than 255 characters long")
    private String avatar;

    // The role is set by the AuthenticationService during the registration process
    @Null(message = "role cannot be specified")
    private String role;
}