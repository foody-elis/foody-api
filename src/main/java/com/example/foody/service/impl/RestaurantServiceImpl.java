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
import com.example.foody.utils.UserRoleUtils;
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
    private final OrderService orderService;

    // todo is a best practice to inject many services in another service?

    public RestaurantServiceImpl(RestaurantRepository restaurantRepository, CategoryRepository categoryRepository, RestaurantMapper restaurantMapper, CategoryService categoryService, WeekDayInfoService weekDayInfoService, BookingService bookingService, AddressService addressService, DishService dishService, OrderService orderService) {
        this.restaurantRepository = restaurantRepository;
        this.categoryRepository = categoryRepository;
        this.restaurantMapper = restaurantMapper;
        this.categoryService = categoryService;
        this.weekDayInfoService = weekDayInfoService;
        this.bookingService = bookingService;
        this.addressService = addressService;
        this.dishService = dishService;
        this.orderService = orderService;
    }

    @Override
    public RestaurantResponseDTO save(RestaurantRequestDTO restaurantDTO) {
        RestaurateurUser principal = (RestaurateurUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Restaurant restaurant = restaurantMapper.restaurantRequestDTOToRestaurant(restaurantDTO);
        Address address = saveRestaurantAddress(restaurant);
        List<Category> categories = addRestaurantToCategories(restaurant, restaurantDTO.getCategories());

        checkRestaurantCreationOrThrow(principal);

        restaurant.setRestaurateur(principal);
        restaurant.setAddress(address);
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
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Restaurant> restaurants = getRestaurantsBasedOnUserRole(principal);
        return restaurantMapper.restaurantsToRestaurantResponseDTOs(restaurants);
    }

    @Override
    public RestaurantResponseDTO findById(long id) {
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Restaurant restaurant = getRestaurantByIdBasedOnUserRole(principal, id);
        return restaurantMapper.restaurantToRestaurantResponseDTO(restaurant);
    }

    @Override
    public RestaurantResponseDTO findByRestaurateur(long restaurateurId) {
        Restaurant restaurant = restaurantRepository
                .findAllByRestaurateur_IdAndDeletedAtIsNull(restaurateurId)
                .orElseThrow(() -> new EntityNotFoundException("restaurant", "restaurateurId", restaurateurId));
        return restaurantMapper.restaurantToRestaurantResponseDTO(restaurant);
    }

    @Override
    public List<RestaurantResponseDTO> findAllByCategory(long categoryId) {
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Restaurant> restaurants = getRestaurantByCategoryBasedOnUserRole(principal, categoryId);
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

        removeAssociatedEntities(restaurant);

        try {
            restaurantRepository.save(restaurant);
        } catch (Exception e) {
            throw new EntityDeletionException("restaurant", "id", id);
        }

        return true;
    }

    private void checkRestaurantCreationOrThrow(User user) {
        if (!restaurantRepository.existsByDeletedAtIsNullAndRestaurateur_Id(user.getId())) return;

        throw new RestaurateurAlreadyHasRestaurantException(user.getId());
    }

    private Address saveRestaurantAddress(Restaurant restaurant) {
        Address address = restaurant.getAddress();
        address.setRestaurant(restaurant);

        return addressService.save(address);

    }

    private List<Category> addRestaurantToCategories(Restaurant restaurant, List<Long> categoryIds) {
        List<Category> categories = new ArrayList<>();

        categoryIds.forEach(categoryId -> {
            Category category = categoryRepository
                    .findById(categoryId)
                    .orElse(null);
            if (category != null) {
                category = categoryService.addRestaurant(category.getId(), restaurant);
                categories.add(category);
            }
        });

        return categories;
    }

    private List<Restaurant> getRestaurantsBasedOnUserRole(User user) {
        if (UserRoleUtils.isAdmin(user) || UserRoleUtils.isModerator(user)) {
            return restaurantRepository.findAllByDeletedAtIsNull();
        } else {
            return restaurantRepository.findAllByDeletedAtIsNullAndApproved(true);
        }
    }

    private Restaurant getRestaurantByIdBasedOnUserRole(User user, long id) {
        if (UserRoleUtils.isAdmin(user) || UserRoleUtils.isModerator(user)) {
            return restaurantRepository
                    .findByIdAndDeletedAtIsNull(id)
                    .orElseThrow(() -> new EntityNotFoundException("restaurant", "id", id));
        } else {
            return restaurantRepository
                    .findByIdAndDeletedAtIsNullAndApproved(id, true)
                    .orElseThrow(() -> new EntityNotFoundException("restaurant", "id", id));
        }
    }

    private List<Restaurant> getRestaurantByCategoryBasedOnUserRole(User user, long categoryId) {
        if (UserRoleUtils.isAdmin(user) || UserRoleUtils.isModerator(user)) {
            return restaurantRepository
                    .findAllByCategoryAndDeletedAtIsNull(categoryId);
        } else {
            return restaurantRepository
                    .findAllByCategoryAndDeletedAtIsNullAndApproved(categoryId, true);
        }
    }

    private void removeAssociatedEntities(Restaurant restaurant) {
        removeRestaurantFromCategories(restaurant);
        removeWeekDayInfos(restaurant);
        removeBookings(restaurant);
        removeDishes(restaurant);
        removeAddress(restaurant);
        removeOrders(restaurant);
        // todo remove the associated reviews, restaurateur, employees
    }

    private void removeRestaurantFromCategories(Restaurant restaurant) {
        restaurant.getCategories().forEach(category ->
                category.getRestaurants().remove(restaurant)
        );
        categoryRepository.saveAll(restaurant.getCategories());
    }

    private void removeWeekDayInfos(Restaurant restaurant) {
        restaurant.getWeekDayInfos().forEach(weekDayInfo ->
                weekDayInfoService.remove(weekDayInfo.getId())
        );
    }

    private void removeBookings(Restaurant restaurant) {
        restaurant.getBookings().forEach(booking ->
                bookingService.remove(booking.getId())
        );
    }

    private void removeDishes(Restaurant restaurant) {
        restaurant.getDishes().forEach(dish ->
                dishService.remove(dish.getId())
        );
    }

    private void removeAddress(Restaurant restaurant) {
        addressService.remove(restaurant.getAddress().getId());
    }

    private void removeOrders(Restaurant restaurant) {
        restaurant.getOrders().forEach(order ->
                orderService.remove(order.getId())
        );
    }
}
