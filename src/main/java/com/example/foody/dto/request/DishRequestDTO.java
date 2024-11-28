package com.example.foody.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DishRequestDTO {
    @NotBlank(message = "name cannot be blank")
    @Size(min = 1, max = 100, message = "name cannot be less than 1 character or more than 100 characters long")
    private String name;

    @NotBlank(message = "description cannot be blank")
    @Size(min = 1, max = 65535, message = "description cannot be less than 1 character or more than 65535 characters long")
    private String description;

    @NotNull(message = "price cannot be null")
    @Positive(message = "price cannot be a negative number or zero")
    @Digits(integer = 6, fraction = 2, message = "price should have up to 6 integer digits and 2 decimal places")
    private BigDecimal price;

    @NotBlank(message = "avatar cannot be blank")
    @Size(min = 1, max = 255, message = "avatar cannot be less than 1 character or more than 255 characters long")
    private String photo;

    @NotNull(message = "restaurantId cannot be null")
    @Positive(message = "restaurantId cannot be a negative number or zero")
    private Long restaurantId;
}