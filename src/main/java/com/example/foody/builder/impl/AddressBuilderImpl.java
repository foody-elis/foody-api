package com.example.foody.builder.impl;

import com.example.foody.builder.AddressBuilder;
import com.example.foody.model.Address;
import com.example.foody.model.Restaurant;
import org.springframework.stereotype.Component;

@Component
public class AddressBuilderImpl implements AddressBuilder {
    private long id;
    private String city;
    private String province;
    private String street;
    private String civicNumber;
    private String postalCode;
    private Restaurant restaurant;

    @Override
    public AddressBuilder id(long id) {
        this.id = id;
        return this;
    }

    @Override
    public AddressBuilder city(String city) {
        this.city = city;
        return this;
    }

    @Override
    public AddressBuilder province(String province) {
        this.province = province;
        return this;
    }

    @Override
    public AddressBuilder street(String street) {
        this.street = street;
        return this;
    }

    @Override
    public AddressBuilder civicNumber(String civicNumber) {
        this.civicNumber = civicNumber;
        return this;
    }

    @Override
    public AddressBuilder postalCode(String postalCode) {
        this.postalCode = postalCode;
        return this;
    }

    @Override
    public AddressBuilder restaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
        return this;
    }

    @Override
    public Address build() {
        return new Address(
                id,
                city,
                province,
                street,
                civicNumber,
                postalCode,
                restaurant
        );
    }
}
