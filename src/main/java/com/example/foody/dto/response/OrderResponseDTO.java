package com.example.foody.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseDTO {
    private long id;
    private String tableNumber;
    private List<DishResponseDTO> dishes = new ArrayList<>();
    private long customerId;
    private long restaurantId;
    private String status;
}
