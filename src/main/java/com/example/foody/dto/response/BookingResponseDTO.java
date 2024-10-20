package com.example.foody.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingResponseDTO {
    private long id;
    private LocalDate date;
    private int seats;
    private long sittingTimeId;
    private long customerId;
    private long restaurantId;
    private String status;
}
