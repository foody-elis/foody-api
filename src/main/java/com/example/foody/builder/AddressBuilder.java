package com.example.foody.builder;

import com.example.foody.model.Address;
import com.example.foody.model.Restaurant;

public interface AddressBuilder {
    AddressBuilder id(long id);
    AddressBuilder city(String city);
    AddressBuilder province(String province);
    AddressBuilder street(String street);
    AddressBuilder civicNumber(String civicNumber);
    AddressBuilder postalCode(String postalCode);
    AddressBuilder restaurant(Restaurant restaurant);
    Address build();
}
