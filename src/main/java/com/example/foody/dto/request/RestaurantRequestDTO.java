package com.example.foody.dto.request;

import com.example.foody.utils.validator.base64.Base64;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Data Transfer Object for restaurant requests.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantRequestDTO {

    @NotBlank(message = "name cannot be blank")
    @Size(min = 1, max = 100, message = "name cannot be less than 1 character or more than 100 characters long")
    private String name;

    @NotBlank(message = "description cannot be blank")
    @Size(min = 1, max = 65535, message = "description cannot be less than 1 character or more than 65535 characters long")
    private String description;

    @Base64(message = "photoBase64 must be null or a valid non-empty Base64 string")
    private String photoBase64;

    @NotBlank(message = "phoneNumber cannot be blank")
    @Size(min = 1, max = 16, message = "phoneNumber cannot be less than 1 character or more than 16 characters long")
    private String phoneNumber;

    @NotNull(message = "seats cannot be null")
    @Positive(message = "seats cannot be a negative number or zero")
    private Integer seats;

    private List<Long> categories = new ArrayList<>();

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