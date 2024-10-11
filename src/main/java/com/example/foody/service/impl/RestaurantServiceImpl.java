package com.example.foody.service.impl;

import com.example.foody.dto.request.RestaurantRequestDTO;
import com.example.foody.dto.response.RestaurantResponseDTO;
import com.example.foody.exceptions.entity.EntityCreationException;
import com.example.foody.exceptions.entity.EntityDeletionException;
import com.example.foody.exceptions.entity.EntityEditException;
import com.example.foody.exceptions.entity.EntityNotFoundException;
import com.example.foody.mapper.RestaurantMapper;
import com.example.foody.model.Address;
import com.example.foody.model.Category;
import com.example.foody.model.Restaurant;
import com.example.foody.model.User;
import com.example.foody.repository.CategoryRepository;
import com.example.foody.repository.RestaurantRepository;
import com.example.foody.service.AddressService;
import com.example.foody.service.CategoryService;
import com.example.foody.service.RestaurantService;
import com.example.foody.utils.Role;
import jakarta.transaction.Transactional;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(rollbackOn = Exception.class)
public class RestaurantServiceImpl implements RestaurantService {
    private final RestaurantRepository restaurantRepository;
    private final CategoryRepository categoryRepository;
    private final RestaurantMapper restaurantMapper;
    private final AddressService addressService;
    private final CategoryService categoryService;

    public RestaurantServiceImpl(RestaurantRepository restaurantRepository, CategoryRepository categoryRepository, RestaurantMapper restaurantMapper, AddressService addressService, CategoryService categoryService) {
        this.restaurantRepository = restaurantRepository;
        this.categoryRepository = categoryRepository;
        this.restaurantMapper = restaurantMapper;
        this.addressService = addressService;
        this.categoryService = categoryService;
    }

    @Override
    public RestaurantResponseDTO save(RestaurantRequestDTO restaurantDTO) {
        Restaurant restaurant = restaurantMapper.restaurantRequestDTOToRestaurant(restaurantDTO);
        Address address = restaurant.getAddress();
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // I save the associated address
        address.setRestaurant(restaurant);
        address = addressService.save(address);

        List<Category> categories = new ArrayList<>();

        // I add the restaurant to the categories
        for (long categoryId : restaurantDTO.getCategories()) {
            Category category = categoryRepository.findById(categoryId).orElse(null);

            if (category != null) {
                // I add the restaurant to the category
                category = categoryService.addRestaurant(category.getId(), restaurant);

                // I add the category to the restaurant
                categories.add(category);
            }
        }

        restaurant.setAddress(address);
        restaurant.setUser(principal);
        restaurant.setCategories(categories);

        try {
            restaurant = restaurantRepository.save(restaurant);
        } catch (Exception e) {
            throw new EntityCreationException("restaurant");
        }

        return restaurantMapper.restaurantToRestaurantResponseDTO(restaurant);
    }

    @Override
    public List<RestaurantResponseDTO> findAll() {
        List<Restaurant> restaurants;
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal.getRole().equals(Role.ADMIN) || principal.getRole().equals(Role.MODERATOR)) {
            // I return all the restaurants, approved or not
            restaurants = restaurantRepository.findAllByDeletedAtIsNull();
        } else {
            // I return only the approved restaurants
            restaurants = restaurantRepository.findAllByDeletedAtIsNullAndApproved(true);
        }

        return restaurantMapper.restaurantsToRestaurantResponseDTOs(restaurants);
    }

    @Override
    public RestaurantResponseDTO findById(long id) {
        Restaurant restaurant;
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal.getRole().equals(Role.ADMIN) || principal.getRole().equals(Role.MODERATOR)) {
            // I return a restaurant by id, approved or not
            restaurant = restaurantRepository
                    .findByIdAndDeletedAtIsNull(id)
                    .orElseThrow(() -> new EntityNotFoundException("restaurant", "id", id));
        } else {
            // I return a restaurant by id, only if it is approved
            restaurant = restaurantRepository
                    .findByIdAndDeletedAtIsNullAndApproved(id, true)
                    .orElseThrow(() -> new EntityNotFoundException("restaurant", "id", id));
        }

        return restaurantMapper.restaurantToRestaurantResponseDTO(restaurant);
    }

    @Override
    public List<RestaurantResponseDTO> findAllByCategory(long categoryId) {
        categoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("category", "id", categoryId));

        List<Restaurant> restaurants;
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal.getRole().equals(Role.ADMIN) || principal.getRole().equals(Role.MODERATOR)) {
            // I return all the restaurants, approved or not
            restaurants = restaurantRepository.findAllByCategoryAndDeletedAtIsNull(categoryId);
        } else {
            // I return only the approved restaurants
            restaurants = restaurantRepository.findAllByCategoryAndDeletedAtIsNullAndApproved(categoryId, true);
        }

        return restaurantMapper.restaurantsToRestaurantResponseDTOs(restaurants);
    }

    @Override
    public RestaurantResponseDTO approveById(long id) {
        Restaurant restaurant = restaurantRepository
                .findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new EntityNotFoundException("restaurant", "id", id));

        restaurant.setApproved(true);

        try {
            restaurant = restaurantRepository.save(restaurant);
        } catch (Exception e) {
            throw new EntityEditException("restaurant", "id", id);
        }

        // todo send email to the restaurateur

        return restaurantMapper.restaurantToRestaurantResponseDTO(restaurant);
    }

    @Override
    public boolean remove(long id) {
        Restaurant restaurant = restaurantRepository
                .findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new EntityNotFoundException("restaurant", "id", id));

        restaurant.setDeletedAt(LocalDateTime.now());

        addressService.remove(restaurant.getAddress().getId());

        // I remove the restaurant from the categories
        restaurant.getCategories().forEach(
                category -> category.getRestaurants().remove(restaurant)
        );
        categoryRepository.saveAll(restaurant.getCategories());

        try {
            restaurantRepository.save(restaurant);
        } catch (Exception e) {
            throw new EntityDeletionException("restaurant", "id", id);
        }

        return true;
    }
}
