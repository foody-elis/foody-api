package com.example.foody.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressResponseDTO {
    private long id;
    private String city;
    private String province;
    private String street;
    private String civicNumber;
    private String postalCode;
    private long restaurantId;
}
