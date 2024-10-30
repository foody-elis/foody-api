package com.example.foody.service.impl;

import com.example.foody.dto.request.RestaurantRequestDTO;
import com.example.foody.dto.response.RestaurantResponseDTO;
import com.example.foody.exceptions.entity.EntityCreationException;
import com.example.foody.exceptions.entity.EntityDeletionException;
import com.example.foody.exceptions.entity.EntityEditException;
import com.example.foody.exceptions.entity.EntityNotFoundException;
import com.example.foody.exceptions.restaurant.RestaurateurAlreadyHasRestaurantException;
import com.example.foody.mapper.RestaurantMapper;
import com.example.foody.model.Address;
import com.example.foody.model.Category;
import com.example.foody.model.Restaurant;
import com.example.foody.model.user.RestaurateurUser;
import com.example.foody.model.user.User;
import com.example.foody.repository.CategoryRepository;
import com.example.foody.repository.RestaurantRepository;
import com.example.foody.service.*;
import com.example.foody.utils.enums.Role;
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
    private final CategoryService categoryService;
    private final WeekDayInfoService weekDayInfoService;
    private final BookingService bookingService;
    private final AddressService addressService;
    private final DishService dishService;

    public RestaurantServiceImpl(RestaurantRepository restaurantRepository, CategoryRepository categoryRepository, RestaurantMapper restaurantMapper, CategoryService categoryService, WeekDayInfoService weekDayInfoService, BookingService bookingService, AddressService addressService, DishService dishService) {
        this.restaurantRepository = restaurantRepository;
        this.categoryRepository = categoryRepository;
        this.restaurantMapper = restaurantMapper;
        this.categoryService = categoryService;
        this.weekDayInfoService = weekDayInfoService;
        this.bookingService = bookingService;
        this.addressService = addressService;
        this.dishService = dishService;
    }

    @Override
    public RestaurantResponseDTO save(RestaurantRequestDTO restaurantDTO) {
        Restaurant restaurant = restaurantMapper.restaurantRequestDTOToRestaurant(restaurantDTO);
        Address address = restaurant.getAddress();
        RestaurateurUser principal = (RestaurateurUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // Check if the restaurateur already has a restaurant
        if (restaurantRepository.existsByDeletedAtIsNullAndRestaurateur_Id(principal.getId())) {
            throw new RestaurateurAlreadyHasRestaurantException(principal.getId());
        }

        // Save the associated address
        address.setRestaurant(restaurant);
        address = addressService.save(address);

        List<Category> categories = new ArrayList<>();

        // Add the restaurant to the categories
        for (long categoryId : restaurantDTO.getCategories()) {
            Category category = categoryRepository.findById(categoryId).orElse(null);

            if (category != null) {
                // Add the restaurant to the category
                category = categoryService.addRestaurant(category.getId(), restaurant);

                // Add the category to the restaurant
                categories.add(category);
            }
        }

        restaurant.setAddress(address);
        restaurant.setCategories(categories);
        restaurant.setRestaurateur(principal);

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
            // Return all the restaurants, approved or not
            restaurants = restaurantRepository.findAllByDeletedAtIsNull();
        } else {
            // Return only the approved restaurants
            restaurants = restaurantRepository.findAllByDeletedAtIsNullAndApproved(true);
        }

        return restaurantMapper.restaurantsToRestaurantResponseDTOs(restaurants);
    }

    @Override
    public RestaurantResponseDTO findById(long id) {
        Restaurant restaurant;
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal.getRole().equals(Role.ADMIN) || principal.getRole().equals(Role.MODERATOR)) {
            // Return a restaurant by id, approved or not
            restaurant = restaurantRepository
                    .findByIdAndDeletedAtIsNull(id)
                    .orElseThrow(() -> new EntityNotFoundException("restaurant", "id", id));
        } else {
            // Return a restaurant by id, only if it is approved
            restaurant = restaurantRepository
                    .findByIdAndDeletedAtIsNullAndApproved(id, true)
                    .orElseThrow(() -> new EntityNotFoundException("restaurant", "id", id));
        }

        return restaurantMapper.restaurantToRestaurantResponseDTO(restaurant);
    }

    @Override
    public List<RestaurantResponseDTO> findAllByCategory(long categoryId) {
        categoryRepository
                .findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("category", "id", categoryId));

        List<Restaurant> restaurants;
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal.getRole().equals(Role.ADMIN) || principal.getRole().equals(Role.MODERATOR)) {
            // Return all the restaurants, approved or not
            restaurants = restaurantRepository.findAllByCategoryAndDeletedAtIsNull(categoryId);
        } else {
            // Return only the approved restaurants
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

        // Remove the restaurant from the categories
        restaurant.getCategories().forEach(
                category -> category.getRestaurants().remove(restaurant)
        );
        categoryRepository.saveAll(restaurant.getCategories());

        // Remove the associated week day infos
        restaurant.getWeekDayInfos().forEach(
                weekDayInfo -> weekDayInfoService.remove(weekDayInfo.getId())
        );

        // Remove the associated bookings
        restaurant.getBookings().forEach(
                booking -> bookingService.remove(booking.getId())
        );

        // Remove the associated dishes
        restaurant.getDishes().forEach(
                dish -> dishService.remove(dish.getId())
        );

        // Remove the associated address
        addressService.remove(restaurant.getAddress().getId());

        // todo remove the associated orders, reviews, owner, employees

        try {
            restaurantRepository.save(restaurant);
        } catch (Exception e) {
            throw new EntityDeletionException("restaurant", "id", id);
        }

        return true;
    }
}
