package com.example.foody.service.impl;

import com.example.foody.dto.request.DishRequestDTO;
import com.example.foody.dto.response.DishResponseDTO;
import com.example.foody.exceptions.entity.EntityCreationException;
import com.example.foody.exceptions.entity.EntityDeletionException;
import com.example.foody.exceptions.entity.EntityNotFoundException;
import com.example.foody.exceptions.restaurant.ForbiddenRestaurantAccessException;
import com.example.foody.mapper.DishMapper;
import com.example.foody.model.Dish;
import com.example.foody.model.Order;
import com.example.foody.model.Restaurant;
import com.example.foody.model.user.User;
import com.example.foody.repository.DishRepository;
import com.example.foody.repository.OrderRepository;
import com.example.foody.repository.RestaurantRepository;
import com.example.foody.service.DishService;
import com.example.foody.utils.UserRoleUtils;
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
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Dish dish = dishMapper.dishRequestDTOToDish(dishDTO);
        Restaurant restaurant = restaurantRepository
                .findByIdAndDeletedAtIsNull(dishDTO.getRestaurantId()) // Dishes can also be saved if the restaurant is not approved
                .orElseThrow(() -> new EntityNotFoundException("restaurant", "id", dishDTO.getRestaurantId()));

        checkDishCreationOrThrow(principal, restaurant);

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
        List<Dish> dishes = dishRepository
                .findAllByDeletedAtIsNullAndRestaurant(restaurantId);
        return dishMapper.dishesToDishResponseDTOs(dishes);
    }

    @Override
    public Dish addOrder(long id, Order order) {
        Dish dish = dishRepository
                .findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new EntityNotFoundException("dish", "id", id));
        dish.getOrders().add(order);

        try {
            return dishRepository.save(dish);
        } catch (Exception e) {
            throw new EntityCreationException("dish");
        }
    }

    @Override
    public boolean remove(long id) {
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Dish dish = dishRepository
                .findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new EntityNotFoundException("dish", "id", id));

        checkDishAccessOrThrow(principal, dish);

        dish.setDeletedAt(LocalDateTime.now());

        removeAssociatedEntities(dish);

        try {
            dishRepository.save(dish);
        } catch (Exception e) {
            throw new EntityDeletionException("dish", "id", id);
        }

        return true;
    }

    private void checkDishCreationOrThrow(User user, Restaurant restaurant) {
        if (restaurant.getRestaurateur().getId() == user.getId()) return;
        if (UserRoleUtils.isAdmin(user)) return;

        throw new ForbiddenRestaurantAccessException();
    }

    private void checkDishAccessOrThrow(User user, Dish dish) {
        if (dish.getRestaurant().getRestaurateur().getId() == user.getId()) return;
        if (UserRoleUtils.isAdmin(user)) return;

        throw new ForbiddenRestaurantAccessException();
    }

    private void removeAssociatedEntities(Dish dish) {
        removeDishFromOrders(dish);
        // todo remove the associated reviews
    }

    private void removeDishFromOrders(Dish dish) {
        dish.getOrders().forEach(order ->
                order.getDishes().remove(dish)
        );
        orderRepository.saveAll(dish.getOrders());
    }
}
