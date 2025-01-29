package com.example.foody.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for employee user response.
 * <p>
 * Extends {@link UserResponseDTO} to include additional information specific to employee users.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeUserResponseDTO extends UserResponseDTO {
    private Long employerRestaurantId;
}