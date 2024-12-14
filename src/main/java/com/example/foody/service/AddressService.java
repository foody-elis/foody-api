package com.example.foody.service;

import com.example.foody.model.Address;

public interface AddressService {
    Address save(Address address);
    Address update(long id, Address newAddress);
    boolean remove(long id);
}