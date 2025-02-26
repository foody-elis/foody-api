package com.example.foody.dto.request;

import com.example.foody.utils.validator.base64.Base64;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Data Transfer Object for user update requests.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateRequestDTO {

    @NotBlank(message = "name cannot be blank")
    @Size(min = 1, max = 30, message = "name cannot be less than 1 character or more than 30 characters long")
    private String name;

    @NotBlank(message = "surname cannot be blank")
    @Size(min = 1, max = 30, message = "surname cannot be less than 1 character or more than 30 characters long")
    private String surname;

    /**
     * The birth date of the user.
     * <p>
     * Must be a past date.
     */
    @NotNull(message = "birthDate cannot be null")
    @Past(message = "birthDate cannot be in the future")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;

    @NotBlank(message = "phoneNumber cannot be blank")
    @Size(min = 1, max = 16, message = "phoneNumber cannot be less than 1 character or more than 16 characters long")
    @Pattern(regexp = "^\\+?[0-9\\s\\-()]{7,15}$", message = "phoneNumber must be a valid format, including optional country code, and contain only digits, spaces, dashes, or brackets")
    private String phoneNumber;

    @Base64(message = "avatarBase64 must be null or a valid non-empty Base64 string")
    private String avatarBase64;
}