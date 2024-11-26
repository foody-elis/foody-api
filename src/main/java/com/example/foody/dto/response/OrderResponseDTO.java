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
    private String tableCode;
    private List<OrderDishResponseDTO> orderDishes = new ArrayList<>();
    private long buyerId;
    private long restaurantId;
    private String status;
}
