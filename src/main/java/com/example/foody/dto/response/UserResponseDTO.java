package com.example.foody.dto.response;

import com.example.foody.utils.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {
    private long id;
    private String email;
    private String name;
    private String surname;
    private LocalDate birthDate;
    private String phoneNumber;
    private String avatarUrl;
    private Role role;
    private boolean active;
    private String firebaseCustomToken; // may be null
}