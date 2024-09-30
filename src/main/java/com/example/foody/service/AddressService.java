package com.example.foody.service;

import com.example.foody.dto.request.AddressRequestDTO;
import com.example.foody.dto.response.AddressResponseDTO;
import com.example.foody.model.Address;

public interface AddressService {
    AddressResponseDTO save(AddressRequestDTO addressDTO);
    Address save(Address address);
    boolean remove(long id);
}
