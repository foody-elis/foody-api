package com.example.foody.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseDTO {
    private long id;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    private String tableCode;
    private List<OrderDishResponseDTO> orderDishes = new ArrayList<>();
    private UserResponseDTO buyer; // CustomerUserResponseDTO or EmployeeUserResponseDTO (CookUser case)
    private RestaurantResponseDTO restaurant;
    private String status;
}