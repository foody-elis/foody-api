package com.example.foody.service.impl;

import com.example.foody.dto.request.DishRequestDTO;
import com.example.foody.dto.response.DishResponseDTO;
import com.example.foody.exceptions.entity.EntityCreationException;
import com.example.foody.exceptions.entity.EntityDeletionException;
import com.example.foody.exceptions.entity.EntityNotFoundException;
import com.example.foody.exceptions.restaurant.ForbiddenRestaurantAccessException;
import com.example.foody.mapper.DishMapper;
import com.example.foody.model.Dish;
import com.example.foody.model.Restaurant;
import com.example.foody.model.User;
import com.example.foody.repository.DishRepository;
import com.example.foody.repository.OrderRepository;
import com.example.foody.repository.RestaurantRepository;
import com.example.foody.service.DishService;
import com.example.foody.utils.Role;
import jakarta.transaction.Transactional;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(rollbackOn = Exception.class)
public class DishServiceImpl implements DishService {
    private final DishRepository dishRepository;
    private final RestaurantRepository restaurantRepository;
    private final DishMapper dishMapper;
    private final OrderRepository orderRepository;

    public DishServiceImpl(DishRepository dishRepository, RestaurantRepository restaurantRepository, DishMapper dishMapper, OrderRepository orderRepository) {
        this.dishRepository = dishRepository;
        this.restaurantRepository = restaurantRepository;
        this.dishMapper = dishMapper;
        this.orderRepository = orderRepository;
    }

    @Override
    public DishResponseDTO save(DishRequestDTO dishDTO) {
        Dish dish = dishMapper.dishRequestDTOToDish(dishDTO);

        // Dishes can also be saved if the restaurant is not approved
        Restaurant restaurant = restaurantRepository
                .findByIdAndDeletedAtIsNull(dishDTO.getRestaurantId())
                .orElseThrow(() -> new EntityNotFoundException("restaurant", "id", dishDTO.getRestaurantId()));

        // Check if the user is the owner of the dish's restaurant
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (restaurant.getUser().getId() != principal.getId() && !principal.getRole().equals(Role.ADMIN)) {
            throw new ForbiddenRestaurantAccessException();
        }

        dish.setRestaurant(restaurant);

        try {
            dish = dishRepository.save(dish);
        } catch (Exception e) {
            throw new EntityCreationException("dish");
        }

        return dishMapper.dishToDishResponseDTO(dish);
    }

    @Override
    public List<DishResponseDTO> findAll() {
        List<Dish> dishes = dishRepository.findAllByDeletedAtIsNull();
        return dishMapper.dishesToDishResponseDTOs(dishes);
    }

    @Override
    public DishResponseDTO findById(long id) {
        Dish dish = dishRepository
                .findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new EntityNotFoundException("dish", "id", id));

        return dishMapper.dishToDishResponseDTO(dish);
    }

    @Override
    public List<DishResponseDTO> findAllByRestaurant(long restaurantId) {
        Restaurant restaurant = restaurantRepository
                .findByIdAndDeletedAtIsNull(restaurantId)
                .orElseThrow(() -> new EntityNotFoundException("restaurant", "id", restaurantId));

        List<Dish> dishes = dishRepository
                .findAllByDeletedAtIsNullAndRestaurant(restaurantId);

        return dishMapper.dishesToDishResponseDTOs(dishes);
    }

    @Override
    public boolean remove(long id) {
        Dish dish = dishRepository
                .findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new EntityNotFoundException("dish", "id", id));

        // Check if the user is the owner of the dish's restaurant
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (dish.getRestaurant().getUser().getId() != principal.getId() && !principal.getRole().equals(Role.ADMIN)) {
            throw new ForbiddenRestaurantAccessException();
        }

        dish.setDeletedAt(LocalDateTime.now());

        // I remove the dish from the orders
        dish.getOrders().forEach(
                order -> order.getDishes().remove(dish)
        );
        orderRepository.saveAll(dish.getOrders());

        try {
            dishRepository.save(dish);
        } catch (Exception e) {
            throw new EntityDeletionException("dish", "id", id);
        }

        return true;
    }
}
