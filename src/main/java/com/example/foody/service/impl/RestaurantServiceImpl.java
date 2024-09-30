package com.example.foody.service.impl;

import com.example.foody.dto.request.RestaurantRequestDTO;
import com.example.foody.dto.response.RestaurantResponseDTO;
import com.example.foody.exceptions.entity.EntityCreationException;
import com.example.foody.exceptions.entity.EntityDeletionException;
import com.example.foody.exceptions.entity.EntityNotFoundException;
import com.example.foody.mapper.RestaurantMapper;
import com.example.foody.model.Address;
import com.example.foody.model.Restaurant;
import com.example.foody.model.User;
import com.example.foody.repository.RestaurantRepository;
import com.example.foody.service.AddressService;
import com.example.foody.service.RestaurantService;
import jakarta.transaction.Transactional;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(rollbackOn = Exception.class)
public class RestaurantServiceImpl implements RestaurantService {
    private final RestaurantRepository restaurantRepository;
    private final RestaurantMapper restaurantMapper;
    private final AddressService addressService;

    public RestaurantServiceImpl(RestaurantRepository restaurantRepository, RestaurantMapper restaurantMapper, AddressService addressService) {
        this.restaurantRepository = restaurantRepository;
        this.restaurantMapper = restaurantMapper;
        this.addressService = addressService;
    }

    @Override
    public RestaurantResponseDTO save(RestaurantRequestDTO restaurantDTO) {
        Restaurant restaurant = restaurantMapper.restaurantRequestDTOToRestaurant(restaurantDTO);
        Address address = restaurant.getAddress();
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        address.setRestaurant(restaurant);
        address = addressService.save(address);

        restaurant.setAddress(address);
        restaurant.setUser(user);

        try {
            restaurant = restaurantRepository.save(restaurant);
        } catch (Exception e) {
            throw new EntityCreationException("restaurant");
        }

        return restaurantMapper.restaurantToRestaurantResponseDTO(restaurant);
    }

    @Override
    public List<RestaurantResponseDTO> findAll() {
        List<Restaurant> restaurants = restaurantRepository.findAllByDeletedAtIsNull();
        return restaurantMapper.restaurantsToRestaurantResponseDTOs(restaurants);
    }

    @Override
    public RestaurantResponseDTO findById(long id) {
        Restaurant restaurant = restaurantRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new EntityNotFoundException("restaurant", "id", id));
        return restaurantMapper.restaurantToRestaurantResponseDTO(restaurant);
    }

    @Override
    public boolean remove(long id) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("restaurant", "id", id));

        restaurant.setDeletedAt(LocalDateTime.now());
        addressService.remove(restaurant.getAddress().getId());

        try {
            restaurantRepository.save(restaurant);
        } catch (Exception e) {
            throw new EntityDeletionException("restaurant", "id", id);
        }

        return true;
    }
}
