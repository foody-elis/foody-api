package com.example.foody.service;

import com.example.foody.model.Address;

/**
 * Service interface for managing addresses.
 */
public interface AddressService {

    /**
     * Saves a new address.
     *
     * @param address the address to save
     * @return the saved address
     */
    Address save(Address address);

    /**
     * Updates an existing address.
     *
     * @param id the ID of the address to update
     * @param newAddress the new address data
     * @return the updated address
     */
    Address update(long id, Address newAddress);

    /**
     * Removes an address by its ID.
     *
     * @param id the ID of the address to remove
     * @return true if the address was removed, false otherwise
     */
    boolean remove(long id);
}