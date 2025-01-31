package com.example.foody.service.impl;

import com.example.foody.exceptions.entity.EntityCreationException;
import com.example.foody.exceptions.entity.EntityDeletionException;
import com.example.foody.exceptions.entity.EntityEditException;
import com.example.foody.exceptions.entity.EntityNotFoundException;
import com.example.foody.model.Address;
import com.example.foody.repository.AddressRepository;
import com.example.foody.service.AddressService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Implementation of the {@link AddressService} interface.
 * <p>
 * Provides methods to create, update, and delete {@link Address} objects.
 */
@Service
@Transactional(rollbackOn = Exception.class)
@AllArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;

    /**
     * {@inheritDoc}
     * <p>
     * This method persists a new {@link Address} entity to the database.
     *
     * @param address the address to save
     * @return the saved address
     * @throws EntityCreationException if there is an error during the creation of the address
     */
    @Override
    public Address save(Address address) {
        try {
            address = addressRepository.save(address);
        } catch (Exception e) {
            throw new EntityCreationException("address");
        }

        return address;
    }

    /**
     * {@inheritDoc}
     * <p>
     * This method updates the details of an existing {@link Address} entity identified by its ID.
     *
     * @param id the ID of the address to update
     * @param newAddress the new address data
     * @return the updated address
     * @throws EntityNotFoundException if the address with the specified ID is not found
     * @throws EntityEditException if there is an error during the update of the address
     */
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

    /**
     * {@inheritDoc}
     * <p>
     * This method marks an {@link Address} entity as deleted in the database.
     *
     * @param id the ID of the address to remove
     * @return true if the address was removed, false otherwise
     * @throws EntityNotFoundException if the address with the specified ID is not found
     * @throws EntityDeletionException if there is an error during the deletion of the address
     */
    @Override
    public boolean remove(long id) {
        Address address = addressRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("address", "id", id));
        address.delete();

        try {
            addressRepository.save(address);
        } catch (Exception e) {
            throw new EntityDeletionException("address", "id", id);
        }

        return true;
    }
}