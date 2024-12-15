package com.example.foody.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewResponseDTO {
    private long id;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    private String title;
    private String description;
    private int rating;
    private long restaurantId;
    private Long dishId; // may be null
    private long customerId;
    private String customerName;
    private String customerSurname;
    private String customerAvatarUrl;
}