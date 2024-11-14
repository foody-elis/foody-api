package com.example.foody.service.impl;

import com.example.foody.dto.request.OrderRequestDTO;
import com.example.foody.dto.response.OrderResponseDTO;
import com.example.foody.exceptions.entity.EntityCreationException;
import com.example.foody.exceptions.entity.EntityDeletionException;
import com.example.foody.exceptions.entity.EntityEditException;
import com.example.foody.exceptions.entity.EntityNotFoundException;
import com.example.foody.exceptions.order.ForbiddenOrderAccessException;
import com.example.foody.exceptions.order.InvalidOrderStateException;
import com.example.foody.exceptions.order.OrderNotAllowedException;
import com.example.foody.exceptions.restaurant.ForbiddenRestaurantAccessException;
import com.example.foody.mapper.OrderMapper;
import com.example.foody.model.Dish;
import com.example.foody.model.Order;
import com.example.foody.model.Restaurant;
import com.example.foody.model.user.BuyerUser;
import com.example.foody.model.user.User;
import com.example.foody.repository.*;
import com.example.foody.service.DishService;
import com.example.foody.service.OrderService;
import com.example.foody.state.order.PreparingState;
import com.example.foody.utils.enums.Role;
import jakarta.transaction.Transactional;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(rollbackOn = Exception.class)
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final RestaurantRepository restaurantRepository;
    private final BookingRepository bookingRepository;
    private final DishRepository dishRepository;
    private final UserRepository userRepository;
    private final DishService dishService;
    private final OrderMapper orderMapper;

    public OrderServiceImpl(OrderRepository orderRepository, RestaurantRepository restaurantRepository, BookingRepository bookingRepository, DishRepository dishRepository, UserRepository userRepository, DishService dishService, OrderMapper orderMapper) {
        this.orderRepository = orderRepository;
        this.restaurantRepository = restaurantRepository;
        this.bookingRepository = bookingRepository;
        this.dishRepository = dishRepository;
        this.userRepository = userRepository;
        this.dishService = dishService;
        this.orderMapper = orderMapper;
    }

    @Override
    public OrderResponseDTO save(OrderRequestDTO orderDTO) {
        Order order = orderMapper.orderRequestDTOToOrder(orderDTO);
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Restaurant restaurant = restaurantRepository
                .findByIdAndDeletedAtIsNull(orderDTO.getRestaurantId())
                .orElseThrow(() -> new EntityNotFoundException("restaurant", "id", orderDTO.getRestaurantId()));
        List<Dish> dishes = addOrderToDishesOrThrow(order, orderDTO.getDishes());

        order.setBuyer(new BuyerUser(principal.getId(), new ArrayList<>()));
        order.setRestaurant(restaurant);
        order.setDishes(dishes);
        order.setState(new PreparingState(order));

        checkOrderCreationOrThrow(principal, order);

        try {
            order = orderRepository.save(order);
        } catch (Exception e) {
            throw new EntityCreationException("order");
        }

        // todo notify someone ???

        return orderMapper.orderToOrderResponseDTO(order);
    }

    @Override
    public List<OrderResponseDTO> findAll() {
        List<Order> orders = orderRepository.findAllByDeletedAtIsNull();
        return orderMapper.ordersToOrderResponseDTOs(orders);
    }

    @Override
    public OrderResponseDTO findById(long id) {
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Order order = orderRepository
                .findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new EntityNotFoundException("order", "id", id));

        checkOrderAccessOrThrow(principal, order);

        return orderMapper.orderToOrderResponseDTO(order);
    }

    @Override
    public List<OrderResponseDTO> findAllByBuyer(long buyerId) {
        userRepository
                .findByIdAndDeletedAtIsNull(buyerId)
                .orElseThrow(() -> new EntityNotFoundException("user", "id", buyerId));

        List<Order> orders = orderRepository
                .findAllByDeletedAtIsNullAndBuyer_IdOrderByCreatedAtDesc(buyerId);

        return orderMapper.ordersToOrderResponseDTOs(orders);
    }

    @Override
    public List<OrderResponseDTO> findAllByRestaurant(long restaurantId) {
        Restaurant restaurant = restaurantRepository
                .findByIdAndDeletedAtIsNull(restaurantId)
                .orElseThrow(() -> new EntityNotFoundException("restaurant", "id", restaurantId));
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        checkRestaurantAccessOrThrow(principal, restaurant);

        List<Order> orders = orderRepository
                .findAllByDeletedAtIsNullAndRestaurant_IdOrderByCreatedAtDesc(restaurantId);

        return orderMapper.ordersToOrderResponseDTOs(orders);
    }

    @Override
    public OrderResponseDTO awaitPaymentById(long id) {
        Order order = orderRepository
                .findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new EntityNotFoundException("order", "id", id));
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        checkAwaitPaymentAccess(principal, order);

        try {
            order.awaitPayment();
            order = orderRepository.save(order);
        } catch (IllegalStateException e) {
            throw new InvalidOrderStateException(e.getMessage());
        } catch (Exception e) {
            throw new EntityEditException("order", "id", id);
        }

        // todo notity someone ???

        return orderMapper.orderToOrderResponseDTO(order);
    }

    @Override
    public OrderResponseDTO completeById(long id) {
        Order order = orderRepository
                .findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new EntityNotFoundException("order", "id", id));
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        checkCompleteAccess(principal, order);

        try {
            order.complete();
            order = orderRepository.save(order);
        } catch (IllegalStateException e) {
            throw new InvalidOrderStateException(e.getMessage());
        } catch (Exception e) {
            throw new EntityEditException("order", "id", id);
        }

        // todo notity someone ???

        return orderMapper.orderToOrderResponseDTO(order);
    }

    @Override
    public boolean remove(long id) {
        Order order = orderRepository
                .findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new EntityNotFoundException("order", "id", id));

        order.setDeletedAt(LocalDateTime.now());
        removeOrderFromDishes(order);

        try {
            orderRepository.save(order);
        } catch (Exception e) {
            throw new EntityDeletionException("order", "id", id);
        }

        return true;
    }

    private List<Dish> addOrderToDishesOrThrow(Order order, List<Long> dishIds) {
        List<Dish> dishes = new ArrayList<>();
        for (long dishId : dishIds) {
            Dish dish = dishRepository
                    .findByIdAndDeletedAtIsNull(dishId)
                    .orElseThrow(() -> new EntityNotFoundException("dish", "id", dishId));
            if (dish != null) {
                dish = dishService.addOrder(dish.getId(), order);
                dishes.add(dish);
            }
        }

        return dishes;
    }

    private void removeOrderFromDishes(Order order) {
        order.getDishes().forEach(
                dish -> dish.getOrders().remove(order)
        );
        dishRepository.saveAll(order.getDishes());
    }

    // Check if the order can be created
    private void checkOrderCreationOrThrow(User user, Order order) {
        if (user.getRole().equals(Role.WAITER)) {
            checkIsWaiterOfRestaurant(user, order);
        }
        if (user.getRole().equals(Role.CUSTOMER)) {
            checkActiveReservationOrThrow(order);
        }
        checkDishesBelongToRestaurantOrThrow(order);
    }

    // Check if the user is a waiter of the restaurant
    private void checkIsWaiterOfRestaurant(User user, Order order) {
        if (order.getRestaurant().getEmployees().stream().noneMatch(employeeUser -> employeeUser.getId() == user.getId())) {
            throw new ForbiddenOrderAccessException();
        }
    }

    /*
        Check if there is a booking with:
        - not in the past
        - status ACTIVE
        - customer_id equal to that of the order
        - restaurant_id equal to that of the order
        - sitting_time with:
            - week_day equal to that of the order
            - start <= order time <= end
     */
    private void checkActiveReservationOrThrow(Order order) {
        if (!bookingRepository.existsActiveBookingForOrder(order.getBuyer().getId(), order.getRestaurant().getId())) {
            throw new OrderNotAllowedException(order.getRestaurant().getId(), "there are no active bookings for the buyer");
        }
    }

    // Check if all the dishes belong to the restaurant of the order
    private void checkDishesBelongToRestaurantOrThrow(Order order) {
        if (order.getDishes().stream().anyMatch(dish -> dish.getRestaurant().getId() != order.getRestaurant().getId())) {
            throw new OrderNotAllowedException(order.getRestaurant().getId(), "some dishes do not belong to the restaurant");
        }
    }

    // Check if the user is the buyer of the order or a restaurateur/cook/waiter of the restaurant or an admin
    private void checkOrderAccessOrThrow(User user, Order order) {
        if (order.getBuyer().getId() != user.getId() &&
                order.getRestaurant().getRestaurateur().getId() != user.getId() &&
                order.getRestaurant().getEmployees().stream().noneMatch(employeeUser -> employeeUser.getId() == user.getId()) &&
                !user.getRole().equals(Role.ADMIN)) {
            throw new ForbiddenOrderAccessException();
        }
    }

    // Check if the user is the owner of the restaurant or an admin
    private void checkRestaurantAccessOrThrow(User user, Restaurant restaurant) {
        if (restaurant.getRestaurateur().getId() != user.getId() && !user.getRole().equals(Role.ADMIN)) {
            throw new ForbiddenRestaurantAccessException();
        }
    }

    // Check if the user is a cook of the restaurant or an admin
    private void checkAwaitPaymentAccess(User user, Order order) {
        if (order.getRestaurant().getEmployees().stream().noneMatch(employeeUser -> (employeeUser.getId() == user.getId() && user.getRole().equals(Role.COOK))) &&
                !user.getRole().equals(Role.ADMIN)) {
            throw new ForbiddenOrderAccessException();
        }
    }

    // Check if the user is a waiter of the restaurant or an admin
    private void checkCompleteAccess(User user, Order order) {
        if (order.getRestaurant().getEmployees().stream().noneMatch(employeeUser -> (employeeUser.getId() == user.getId() && user.getRole().equals(Role.WAITER))) &&
                !user.getRole().equals(Role.ADMIN)) {
            throw new ForbiddenOrderAccessException();
        }
    }
}