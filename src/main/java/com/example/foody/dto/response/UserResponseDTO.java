package com.example.foody.dto.response;

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
    private String password;
    private String name;
    private String surname;
    private LocalDate birthDate;
    private String phoneNumber;
    private String avatar;
    private String role;
    private boolean active;
}
