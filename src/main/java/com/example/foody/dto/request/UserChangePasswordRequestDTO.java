package com.example.foody.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for user change password requests.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserChangePasswordRequestDTO {

    @NotBlank(message = "currentPassword cannot be blank")
    private String currentPassword;

    @NotBlank(message = "newPassword cannot be blank")
    @Size(min = 8, max = 100, message = "newPassword cannot be less than 8 characters or more than 100 characters long")
    private String newPassword;

    @NotBlank(message = "confirmPassword cannot be blank")
    private String confirmPassword;
}