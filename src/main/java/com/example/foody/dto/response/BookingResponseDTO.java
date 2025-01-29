package com.example.foody.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Data Transfer Object for booking response.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingResponseDTO {
    private long id;
    private LocalDate date;
    private int seats;
    private SittingTimeResponseDTO sittingTime;
    private CustomerUserResponseDTO customer;
    private RestaurantResponseDTO restaurant;
    private String status;
}