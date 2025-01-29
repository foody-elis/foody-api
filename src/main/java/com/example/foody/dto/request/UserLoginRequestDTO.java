package com.example.foody.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for user login requests.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginRequestDTO {

    @Email(message = "email is not a well-formed email address")
    @NotBlank(message = "email cannot be blank")
    private String email;

    @NotBlank(message = "password cannot be blank")
    private String password;
}