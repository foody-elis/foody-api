package com.example.foody.service.impl;

import com.example.foody.TestDataUtil;
import com.example.foody.exceptions.entity.EntityCreationException;
import com.example.foody.exceptions.entity.EntityDeletionException;
import com.example.foody.exceptions.entity.EntityEditException;
import com.example.foody.exceptions.entity.EntityNotFoundException;
import com.example.foody.model.Address;
import com.example.foody.repository.AddressRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link AddressServiceImpl} class using mock services.
 */
@ExtendWith(MockitoExtension.class)
public class AddressServiceImplTest {

    @InjectMocks
    private AddressServiceImpl addressService;

    @Mock
    private AddressRepository addressRepository;

    @Test
    void saveWhenAddressIsValidReturnsSavedAddress() {
        // Arrange
        Address address = TestDataUtil.createTestAddress();

        when(addressRepository.save(address)).thenReturn(address);

        // Act
        Address savedAddress = addressService.save(address);

        // Assert
        assertNotNull(savedAddress);
        assertEquals(address, savedAddress);
        verify(addressRepository, times(1)).save(address);
    }

    @Test
    void saveWhenSaveFailsThrowsEntityCreationException() {
        // Arrange
        Address address = TestDataUtil.createTestAddress();

        doThrow(new RuntimeException()).when(addressRepository).save(address);

        // Act & Assert
        assertThrows(EntityCreationException.class, () -> addressService.save(address));
        verify(addressRepository, times(1)).save(address);
    }

    @Test
    void updateWhenAddressExistsUpdatesAndReturnsAddress() {
        // Arrange
        Address existingAddress = TestDataUtil.createTestAddress();
        Address newAddress = TestDataUtil.createTestAddress();
        newAddress.setCity("New City");

        when(addressRepository.findById(existingAddress.getId())).thenReturn(Optional.of(existingAddress));
        when(addressRepository.save(existingAddress)).thenReturn(existingAddress);

        // Act
        Address updatedAddress = addressService.update(existingAddress.getId(), newAddress);

        // Assert
        assertNotNull(updatedAddress);
        assertEquals("New City", updatedAddress.getCity());
        verify(addressRepository, times(1)).findById(existingAddress.getId());
        verify(addressRepository, times(1)).save(existingAddress);
    }

    @Test
    void updateWhenAddressDoesNotExistThrowsEntityNotFoundException() {
        // Arrange
        Address newAddress = TestDataUtil.createTestAddress();

        when(addressRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> addressService.update(newAddress.getId(), newAddress));
        verify(addressRepository, times(1)).findById(newAddress.getId());
        verify(addressRepository, never()).save(any(Address.class));
    }

    @Test
    void updateWhenSaveFailsThrowsEntityEditException() {
        // Arrange
        Address existingAddress = TestDataUtil.createTestAddress();
        Address newAddress = TestDataUtil.createTestAddress();

        when(addressRepository.findById(existingAddress.getId())).thenReturn(Optional.of(existingAddress));
        doThrow(new RuntimeException()).when(addressRepository).save(existingAddress);

        // Act & Assert
        assertThrows(EntityEditException.class, () -> addressService.update(existingAddress.getId(), newAddress));
        verify(addressRepository, times(1)).findById(existingAddress.getId());
        verify(addressRepository, times(1)).save(existingAddress);
    }

    @Test
    void removeWhenAddressExistsMarksAsDeleted() {
        // Arrange
        Address address = TestDataUtil.createTestAddress();

        when(addressRepository.findById(address.getId())).thenReturn(Optional.of(address));

        // Act
        boolean result = addressService.remove(address.getId());

        // Assert
        assertTrue(result);
        verify(addressRepository, times(1)).findById(address.getId());
        verify(addressRepository, times(1)).save(address);
    }

    @Test
    void removeWhenAddressDoesNotExistThrowsEntityNotFoundException() {
        // Arrange
        when(addressRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> addressService.remove(anyLong()));
        verify(addressRepository, times(1)).findById(anyLong());
        verify(addressRepository, never()).save(any(Address.class));
    }

    @Test
    void removeWhenSaveFailsThrowsEntityDeletionException() {
        // Arrange
        Address address = TestDataUtil.createTestAddress();

        when(addressRepository.findById(address.getId())).thenReturn(Optional.of(address));
        doThrow(new RuntimeException()).when(addressRepository).save(address);

        // Act & Assert
        assertThrows(EntityDeletionException.class, () -> addressService.remove(address.getId()));
        verify(addressRepository, times(1)).findById(address.getId());
        verify(addressRepository, times(1)).save(address);
    }
}