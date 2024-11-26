package com.example.foody.dto.response;

import com.example.foody.utils.serializer.RoundedDoubleSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantResponseDTO {
    private long id;
    private String name;
    private String description;
    private String phoneNumber;
    private int seats;
    private boolean approved;
    private List<CategoryResponseDTO> categories = new ArrayList<>();
    @JsonSerialize(using = RoundedDoubleSerializer.class)
    private double averageRating;
    private String city;
    private String province;
    private String street;
    private String civicNumber;
    private String postalCode;
    private long restaurateurId;
}