package com.example.foody.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressRequestDTO {
    @NotBlank(message = "city cannot be blank")
    @Size(min = 1, max = 20, message = "city cannot be less than 1 character or more than 20 characters long")
    private String city;

    @NotBlank(message = "province cannot be blank")
    @Size(min = 1, max = 2, message = "province cannot be less than 1 character or more than 2 characters long")
    private String province;

    @NotBlank(message = "street cannot be blank")
    @Size(min = 1, max = 30, message = "street cannot be less than 1 character or more than 30 characters long")
    private String street;

    @NotBlank(message = "civicNumber cannot be blank")
    @Size(min = 1, max = 10, message = "civicNumber cannot be less than 1 character or more than 10 characters long")
    private String civicNumber;

    @NotBlank(message = "postalCode cannot be blank")
    @Size(min = 1, max = 5, message = "postalCode cannot be less than 1 character or more than 5 characters long")
    private String postalCode;
}
