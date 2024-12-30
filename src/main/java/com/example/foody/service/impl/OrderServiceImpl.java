package com.example.foody.service.impl;

import com.example.foody.dto.request.OrderDishRequestDTO;
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
import com.example.foody.model.order_dish.OrderDish;
import com.example.foody.model.user.BuyerUser;
import com.example.foody.model.user.CookUser;
import com.example.foody.model.user.User;
import com.example.foody.observer.listener.impl.CookUserOrderCreatedEventListener;
import com.example.foody.observer.listener.impl.CustomerUserOrderCompletedEventListener;
import com.example.foody.observer.listener.impl.RestaurateurUserOrderCompletedEventListener;
import com.example.foody.observer.manager.EventManager;
import com.example.foody.repository.*;
import com.example.foody.service.EmailService;
import com.example.foody.service.OrderService;
import com.example.foody.state.order.impl.PreparingState;
import com.example.foody.utils.UserRoleUtils;
import com.example.foody.utils.enums.EventType;
import jakarta.transaction.Transactional;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(rollbackOn = Exception.class)
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final RestaurantRepository restaurantRepository;
    private final BookingRepository bookingRepository;
    private final DishRepository dishRepository;
    private final OrderDishRepository orderDishRepository;
    private final OrderMapper orderMapper;
    private final EmailService emailService;
    private final EventManager eventManager;

    public OrderServiceImpl(OrderRepository orderRepository, RestaurantRepository restaurantRepository, BookingRepository bookingRepository, DishRepository dishRepository, OrderDishRepository orderDishRepository, OrderMapper orderMapper, EmailService emailService, EventManager eventManager) {
        this.orderRepository = orderRepository;
        this.restaurantRepository = restaurantRepository;
        this.bookingRepository = bookingRepository;
        this.dishRepository = dishRepository;
        this.orderDishRepository = orderDishRepository;
        this.orderMapper = orderMapper;
        this.emailService = emailService;
        this.eventManager = eventManager;
    }

    @Override
    public OrderResponseDTO save(OrderRequestDTO orderDTO) {
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Order order = orderMapper.orderRequestDTOToOrder(orderDTO);
        Restaurant restaurant = restaurantRepository
                .findByIdAndDeletedAtIsNullAndApproved(orderDTO.getRestaurantId(), true)
                .orElseThrow(() -> new EntityNotFoundException("restaurant", "id", orderDTO.getRestaurantId()));

        order.setBuyer(new BuyerUser(principal.getId(), new ArrayList<>()));
        order.setRestaurant(restaurant);
        order.setState(new PreparingState());

        checkOrderCreationOrThrow(principal, order);

        try {
            order = orderRepository.save(order);
        } catch (Exception e) {
            throw new EntityCreationException("order");
        }

        order.setOrderDishes(addDishesToOrder(order, orderDTO.getOrderDishes()));
        order.getBuyer().setUser(principal);

        notifyOrderCreatedListeners(order);

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
        List<Order> orders = orderRepository
                .findAllByDeletedAtIsNullAndBuyer_IdOrderByCreatedAtDesc(buyerId);
        return orderMapper.ordersToOrderResponseDTOs(orders);
    }

    @Override
    public List<OrderResponseDTO> findAllByRestaurant(long restaurantId) {
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Restaurant restaurant = restaurantRepository
                .findByIdAndDeletedAtIsNullAndApproved(restaurantId, true)
                .orElseThrow(() -> new EntityNotFoundException("restaurant", "id", restaurantId));

        checkRestaurantAccessOrThrow(principal, restaurant);

        List<Order> orders = orderRepository
                .findAllByDeletedAtIsNullAndRestaurant_IdOrderByCreatedAtDesc(restaurantId);

        return orderMapper.ordersToOrderResponseDTOs(orders);
    }

    @Override
    public OrderResponseDTO awaitPaymentById(long id) {
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Order order = orderRepository
                .findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new EntityNotFoundException("order", "id", id));

        checkAwaitPaymentAccess(principal, order);

        try {
            order.awaitPayment();
            order = orderRepository.save(order);
        } catch (IllegalStateException e) {
            throw new InvalidOrderStateException(e.getMessage());
        } catch (Exception e) {
            throw new EntityEditException("order", "id", id);
        }

        return orderMapper.orderToOrderResponseDTO(order);
    }

    @Override
    public OrderResponseDTO completeById(long id) {
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Order order = orderRepository
                .findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new EntityNotFoundException("order", "id", id));

        checkCompleteAccess(principal, order);

        try {
            order.complete();
            order = orderRepository.save(order);
        } catch (IllegalStateException e) {
            throw new InvalidOrderStateException(e.getMessage());
        } catch (Exception e) {
            throw new EntityEditException("order", "id", id);
        }

        notifyOrderCompletedListeners(order);

        return orderMapper.orderToOrderResponseDTO(order);
    }

    @Override
    public boolean remove(long id) {
        Order order = orderRepository
                .findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new EntityNotFoundException("order", "id", id));
        order.delete();

        try {
            orderRepository.save(order);
        } catch (Exception e) {
            throw new EntityDeletionException("order", "id", id);
        }

        return true;
    }

    private List<OrderDish> addDishesToOrder(Order order, List<OrderDishRequestDTO> orderDishDTOs) {
        return orderDishDTOs.stream()
                .map(orderDishDTO -> addDishToOrder(order, orderDishDTO))
                .toList();
    }

    private OrderDish addDishToOrder(Order order, OrderDishRequestDTO orderDishDTO) {
        Dish dish = dishRepository
                .findByIdAndDeletedAtIsNull(orderDishDTO.getDishId())
                .orElseThrow(() -> new EntityNotFoundException("dish", "id", orderDishDTO.getDishId()));
        OrderDish orderDish = new OrderDish(order, dish, orderDishDTO.getQuantity());

        try {
            return orderDishRepository.save(orderDish);
        } catch (Exception e) {
            throw new EntityCreationException("order dish");
        }
    }

    private void checkOrderCreationOrThrow(User user, Order order) {
        if (UserRoleUtils.isWaiter(user)) checkIsWaiterOfRestaurant(user, order);
        if (UserRoleUtils.isCustomer(user)) checkActiveBookingOrThrow(order);

        checkDishesBelongToRestaurantOrThrow(order);
    }

    private void checkIsWaiterOfRestaurant(User user, Order order) {
        if (order.getRestaurant().getEmployees().stream().anyMatch(employeeUser -> employeeUser.getId() == user.getId())) return;

        throw new ForbiddenOrderAccessException();
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
    private void checkActiveBookingOrThrow(Order order) {
        if (bookingRepository.existsActiveBookingForOrder(order.getBuyer().getId(), order.getRestaurant().getId())) return;

        throw new OrderNotAllowedException(order.getRestaurant().getId(), "there are no active bookings for the buyer");
    }

    private void checkDishesBelongToRestaurantOrThrow(Order order) {
        if (order.getOrderDishes().stream().allMatch(orderDish -> orderDish.getDish().getRestaurant().getId() == order.getRestaurant().getId())) return;

        throw new OrderNotAllowedException(order.getRestaurant().getId(), "some dishes do not belong to the restaurant");
    }

    private void checkOrderAccessOrThrow(User user, Order order) {
        if (UserRoleUtils.isAdmin(user)) return;
        if (order.getBuyer().getId() == user.getId()) return;
        if (order.getRestaurant().getRestaurateur().getId() == user.getId()) return;
        if (order.getRestaurant().getEmployees().stream().anyMatch(employeeUser -> employeeUser.getId() == user.getId())) return;

        throw new ForbiddenOrderAccessException();
    }

    private void checkRestaurantAccessOrThrow(User user, Restaurant restaurant) {
        if (UserRoleUtils.isAdmin(user)) return;
        if (restaurant.getRestaurateur().getId() == user.getId()) return;
        if (restaurant.getEmployees().stream().anyMatch(employeeUser -> employeeUser.getId() == user.getId())) return;

        throw new ForbiddenRestaurantAccessException();
    }

    private void checkAwaitPaymentAccess(User user, Order order) {
        if (UserRoleUtils.isAdmin(user)) return;
        if (order.getRestaurant().getEmployees().stream().anyMatch(employeeUser -> employeeUser.getId() == user.getId())) return;

        throw new ForbiddenOrderAccessException();
    }

    private void checkCompleteAccess(User user, Order order) {
        if (UserRoleUtils.isAdmin(user)) return;
        if (order.getRestaurant().getEmployees().stream().anyMatch(employeeUser -> employeeUser.getId() == user.getId())) return;

        throw new ForbiddenOrderAccessException();
    }

    private void notifyOrderCreatedListeners(Order order) {
        subscribeOrderCreatedListeners(order);
        eventManager.notifyListeners(EventType.ORDER_CREATED, order);
    }

    private void subscribeOrderCreatedListeners(Order order) {
        order.getRestaurant().getEmployees().stream()
                .filter(UserRoleUtils::isCook)
                .forEach(cookUser -> eventManager.subscribe(
                        EventType.ORDER_CREATED,
                        new CookUserOrderCreatedEventListener(emailService, (CookUser) cookUser)
                ));
    }

    private void notifyOrderCompletedListeners(Order order) {
        subscribeOrderCompletedListeners();
        eventManager.notifyListeners(EventType.ORDER_COMPLETED, order);
    }

    private void subscribeOrderCompletedListeners() {
        subscribeCustomerUserOrderCompletedListener();
        subscribeRestaurateurUserOrderCompletedListener();
    }

    private void subscribeCustomerUserOrderCompletedListener() {
        eventManager.subscribe(
                EventType.ORDER_COMPLETED,
                new CustomerUserOrderCompletedEventListener(emailService)
        );
    }

    private void subscribeRestaurateurUserOrderCompletedListener() {
        eventManager.subscribe(
                EventType.ORDER_COMPLETED,
                new RestaurateurUserOrderCompletedEventListener(emailService, orderRepository)
        );
    }
}