package com.example.foody.service.impl;

import com.example.foody.dto.response.AddressResponseDTO;
import com.example.foody.exceptions.entity.EntityCreationException;
import com.example.foody.exceptions.entity.EntityDeletionException;
import com.example.foody.exceptions.entity.EntityNotFoundException;
import com.example.foody.mapper.AddressMapper;
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
    private final AddressMapper addressMapper;

    public AddressServiceImpl(AddressRepository addressRepository, AddressMapper addressMapper) {
        this.addressRepository = addressRepository;
        this.addressMapper = addressMapper;
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
