package com.example.foody.service;

import com.example.foody.model.Address;

public interface AddressService {
    Address save(Address address);
    boolean remove(long id);
}
