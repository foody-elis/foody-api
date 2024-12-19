package com.example.foody.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewRequestDTO {
    @NotBlank(message = "title cannot be blank")
    @Size(min = 1, max = 100, message = "title cannot be less than 1 character or more than 100 characters long")
    private String title;

    @NotBlank(message = "description cannot be blank")
    @Size(min = 1, max = 65535, message = "description cannot be less than 1 character or more than 65535 characters long")
    private String description;

    @NotNull(message = "rating cannot be null")
    @Min(value = 1, message = "rating cannot be less than 1")
    @Max(value = 5, message = "rating cannot be more than 5")
    private Integer rating;

    @NotNull(message = "restaurantId cannot be null")
    @Positive(message = "restaurantId cannot be a negative number or zero")
    private Long restaurantId;

    @Positive(message = "restaurantId cannot be a negative number or zero")
    private Long dishId; // may be null
}