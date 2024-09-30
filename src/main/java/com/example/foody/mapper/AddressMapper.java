package com.example.foody.mapper;

import com.example.foody.dto.request.AddressRequestDTO;
import com.example.foody.dto.response.AddressResponseDTO;
import com.example.foody.model.Address;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AddressMapper {
    @Mapping(source = "restaurant.id", target = "restaurantId")
    AddressResponseDTO addressToAddressResponseDTO(Address address);

    @InheritInverseConfiguration
    Address addressRequestDTOToAddress(AddressRequestDTO addressRequestDTO);

    List<AddressResponseDTO> addressesToAddressResponseDTOs(List<Address> addresses);
}
