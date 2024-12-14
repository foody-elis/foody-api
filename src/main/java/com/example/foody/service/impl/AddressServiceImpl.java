package com.example.foody.service.impl;

import com.example.foody.exceptions.entity.EntityCreationException;
import com.example.foody.exceptions.entity.EntityDeletionException;
import com.example.foody.exceptions.entity.EntityEditException;
import com.example.foody.exceptions.entity.EntityNotFoundException;
import com.example.foody.model.Address;
import com.example.foody.repository.AddressRepository;
import com.example.foody.service.AddressService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Transactional(rollbackOn = Exception.class)
public class AddressServiceImpl implements AddressService {
    private final AddressRepository addressRepository;

    public AddressServiceImpl(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    @Override
    public Address save(Address address) {
        try {
            address = addressRepository.save(address);
        } catch (Exception e) {
            throw new EntityCreationException("address");
        }

        return address;
    }

    @Override
    public Address update(long id, Address newAddress) {
        Address address = addressRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("address", "id", id));

        address.setCity(newAddress.getCity());
        address.setProvince(newAddress.getProvince());
        address.setStreet(newAddress.getStreet());
        address.setCivicNumber(newAddress.getCivicNumber());
        address.setPostalCode(newAddress.getPostalCode());

        try {
            return addressRepository.save(address);
        } catch (Exception e) {
            throw new EntityEditException("address", "id", id);
        }
    }

    @Override
    public boolean remove(long id) {
        Address address = addressRepository
                .findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new EntityNotFoundException("address", "id", id));
        address.setDeletedAt(LocalDateTime.now());

        try {
            addressRepository.save(address);
        } catch (Exception e) {
            throw new EntityDeletionException("address", "id", id);
        }

        return true;
    }
}