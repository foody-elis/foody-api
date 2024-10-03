package com.example.foody.mapper.impl;

import com.example.foody.builder.AddressBuilder;
import com.example.foody.dto.request.AddressRequestDTO;
import com.example.foody.dto.response.AddressResponseDTO;
import com.example.foody.mapper.AddressMapper;
import com.example.foody.model.Address;
import com.example.foody.model.Restaurant;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AddressMapperImpl implements AddressMapper {
    private final AddressBuilder addressBuilder;

    public AddressMapperImpl(AddressBuilder addressBuilder) {
        this.addressBuilder = addressBuilder;
    }

    @Override
    public AddressResponseDTO addressToAddressResponseDTO(Address address) {
        if ( address == null ) {
            return null;
        }

        AddressResponseDTO addressResponseDTO = new AddressResponseDTO();

        addressResponseDTO.setRestaurantId( addressRestaurantId( address ) );
        addressResponseDTO.setId( address.getId() );
        addressResponseDTO.setCity( address.getCity() );
        addressResponseDTO.setProvince( address.getProvince() );
        addressResponseDTO.setStreet( address.getStreet() );
        addressResponseDTO.setCivicNumber( address.getCivicNumber() );
        addressResponseDTO.setPostalCode( address.getPostalCode() );

        return addressResponseDTO;
    }

    @Override
    public Address addressRequestDTOToAddress(AddressRequestDTO addressRequestDTO) {
        if ( addressRequestDTO == null ) {
            return null;
        }

        Address address = addressBuilder
                .city( addressRequestDTO.getCity() )
                .province( addressRequestDTO.getProvince() )
                .street( addressRequestDTO.getStreet() )
                .civicNumber( addressRequestDTO.getCivicNumber() )
                .postalCode( addressRequestDTO.getPostalCode() )
                .build();

        return address;
    }

    @Override
    public List<AddressResponseDTO> addressesToAddressResponseDTOs(List<Address> addresses) {
        if ( addresses == null ) {
            return null;
        }

        List<AddressResponseDTO> list = new ArrayList<AddressResponseDTO>( addresses.size() );
        for ( Address address : addresses ) {
            list.add( addressToAddressResponseDTO( address ) );
        }

        return list;
    }

    private long addressRestaurantId(Address address) {
        if ( address == null ) {
            return 0L;
        }
        Restaurant restaurant = address.getRestaurant();
        if ( restaurant == null ) {
            return 0L;
        }
        long id = restaurant.getId();
        return id;
    }
}
