package com.example.foody.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewResponseDTO {
    private long id;
    private String title;
    private String description;
    private int rating;
    private long customerId;
    private long restaurantId;
    private Long dishId; // may be null
}