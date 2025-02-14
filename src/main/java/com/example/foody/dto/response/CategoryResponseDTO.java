package com.example.foody.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for category response.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponseDTO {
    private long id;
    private String name;
}