package com.example.foody.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    @Future(message = "birthDate cannot be in the past")
    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate birthDate;

    @NotBlank(message = "phoneNumber cannot be blank")
    @Size(min = 1, max = 16, message = "phoneNumber cannot be less than 1 character or more than 16 characters long")
    @JsonProperty("phone_number")
    private String phoneNumber;

    // todo aggiungere eventuale regex per il formato dell'avatar
    @NotBlank(message = "avatar cannot be blank")
    private String avatar;

    // the role is set by the AuthenticationService during the registration process
    @Null(message = "role cannot be specified")
    private String role;
}
