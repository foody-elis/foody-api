package com.example.foody.mapper;

import com.example.foody.dto.request.AddressRequestDTO;
import com.example.foody.dto.response.AddressResponseDTO;
import com.example.foody.model.Address;

import java.util.List;

public interface AddressMapper {
    AddressResponseDTO addressToAddressResponseDTO(Address address);
    Address addressRequestDTOToAddress(AddressRequestDTO addressRequestDTO);
    List<AddressResponseDTO> addressesToAddressResponseDTOs(List<Address> addresses);
}